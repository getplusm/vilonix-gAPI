package plusm.vilonix.gapi.game.spectator;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockCanBuildEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.*;
import org.bukkit.event.vehicle.VehicleDamageEvent;
import org.bukkit.event.vehicle.VehicleEntityCollisionEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;
import plusm.vilonix.api.ActionBarAPI;
import plusm.vilonix.api.VilonixNetwork;
import plusm.vilonix.api.event.game.EndGameEvent;
import plusm.vilonix.api.event.gamer.async.AsyncGamerJoinEvent;
import plusm.vilonix.api.game.GameModeType;
import plusm.vilonix.api.game.GameSettings;
import plusm.vilonix.api.player.BukkitGamer;
import plusm.vilonix.api.player.GamerManager;
import plusm.vilonix.api.scoreboard.PlayerTag;
import plusm.vilonix.api.scoreboard.ScoreBoardAPI;
import plusm.vilonix.gapi.event.PlayerChangeGamemodeEvent;
import plusm.vilonix.gapi.game.GameManager;
import plusm.vilonix.gapi.gamemodes.GameModeScoreBoardTeam;
import plusm.vilonix.gapi.guis.playerinventory.PlayerInventory;
import plusm.vilonix.gapi.loader.DartaAPI;
import plusm.vilonix.gapi.utils.DListener;
import plusm.vilonix.gapi.utils.core.PlayerUtil;
import plusm.vilonix.libraries.LibAPI;
import plusm.vilonix.libraries.interfaces.NmsManager;
import plusm.vilonix.libraries.interfaces.packet.PacketContainer;
import plusm.vilonix.libraries.interfaces.packet.entityplayer.PacketCamera;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@SuppressWarnings("deprecation")
public class SpectatorListener extends DListener {
    private static final Map<String, BukkitTask> mapTaskClosestPlayer = new ConcurrentHashMap<>();
    private final ActionBarAPI actionBarAPI = VilonixNetwork.getActionBarAPI();
    private final PacketContainer container = LibAPI.getManager().getPacketContainer();
    private final GamerManager gamerManager = VilonixNetwork.getGamerManager();
    private final NmsManager nmsManager = LibAPI.getManager();

    private static double round(double value) {
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(1, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    private void findNearPlayer(Player player) {
        Iterator<? extends Player> iterator = Bukkit.getOnlinePlayers().iterator();
        Player nearPlayer = null;
        double minDistance = 1.8D;

        while (iterator.hasNext()) {
            Player newPlayer = iterator.next();
            if (newPlayer != player) {
                BukkitGamer gamer = gamerManager.getGamer(player);
                BukkitGamer ndPlayer = gamerManager.getGamer(newPlayer);
                if (gamer != null) {
                    assert ndPlayer != null;
                    if (ndPlayer.getGameMode() != GameModeType.SPECTATOR) {
                        try {
                            double newDistance = player.getLocation().distance(newPlayer.getLocation());
                            if (newDistance <= minDistance) {
                                minDistance = newDistance;
                                nearPlayer = newPlayer;
                            }
                        } catch (IllegalArgumentException ignored) {
                        }
                    }
                }
            }
        }

        if (nearPlayer != null) {
            BukkitGamer gamer = gamerManager.getGamer(player);
            SPlayer sPlayer = SPlayer.getSPlayer(player);
            BukkitGamer dnPlayer = gamerManager.getGamer(nearPlayer);
            if (gamer != null && dnPlayer != null && dnPlayer.getGameMode() != GameModeType.SPECTATOR) {
                if (gamer.getGameMode() == GameModeType.SPECTATOR) {
                    VilonixNetwork.getActionBarAPI().sendBar(player, "§fБлижайший игрок: " + nearPlayer.getDisplayName() + "      §fРасстояние: §a" + round(minDistance));
                }

                player.setCompassTarget(nearPlayer.getLocation());
                if (sPlayer != null)
                    sPlayer.setNearPlayer(nearPlayer);
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onAsyncJoin(AsyncGamerJoinEvent e) {
        Player joiner = e.getGamer().getPlayer();
        if (joiner == null) {
            return;
        }
        GameModeScoreBoardTeam.onJoin(joiner);

        final ScoreBoardAPI API = VilonixNetwork.getScoreBoardAPI();

        PlayerTag playerTag = API.createTag("lol");
        playerTag.setPrefix("§c");
        playerTag.addPlayersToTeam(PlayerUtil.getAlivePlayers());
        playerTag.sendTo(joiner);

        for (Player player : PlayerUtil.getSpectators()) {
            if (player == joiner)
                continue;

            playerTag = API.createTag("lol");
            playerTag.setPrefix("§c");
            playerTag.addPlayersToTeam(PlayerUtil.getAlivePlayers());
            playerTag.sendTo(player);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onBlockBreak(BlockBreakEvent e) {
        Player player = e.getPlayer();
        if (PlayerUtil.isSpectator(player)) {
            e.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onBlockCanBuild(BlockCanBuildEvent e) {
        Block block = e.getBlock();
        if (block != null) {
            Location blockLoc = block.getLocation();
            for (Player spectator : PlayerUtil.getSpectators()) {
                Location spectatorLoc = spectator.getLocation();
                if (blockLoc.getWorld() == spectatorLoc.getWorld() && blockLoc.distance(spectatorLoc) <= 2) {
                    e.setBuildable(true);
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onBlockPlace(BlockPlaceEvent e) {
        Player player = e.getPlayer();
        if (PlayerUtil.isSpectator(player)) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onBoat(VehicleDamageEvent e) { // запретить ломать лодки
        Entity attacker = e.getAttacker();
        if (!(attacker instanceof Player)) {
            return;
        }

        e.setCancelled(PlayerUtil.isSpectator((Player) attacker));
    }

    @EventHandler
    public void onBoat(VehicleEntityCollisionEvent e) { // запретить двигать лодку
        Entity entity = e.getEntity();
        if (!(entity instanceof Player)) {
            return;
        }

        if (PlayerUtil.isSpectator((Player) entity)) {
            e.setCollisionCancelled(true);
            e.setCancelled(true);
            e.setPickupCancelled(true);
        }

    }

    @EventHandler
    public void onChangeGamemode(PlayerChangeGamemodeEvent e) {
        Player player = e.getPlayer();
        GameModeType gameModeType = e.getGameModeType();

        if (gameModeType != GameModeType.SPECTATOR)
            return;

        String spectatorName = SpectatorMenu.CAMERA.remove(player.getName());
        if (spectatorName == null)
            return;
        Player spectator = Bukkit.getPlayer(spectatorName);
        if (spectator == null)
            return;

        spectator.teleport(player);
        removeSpectator(spectator);
    }

    @EventHandler // в режиме спектра запрещаем менять слот
    public void onChangeSlot(PlayerItemHeldEvent e) {
        String name = e.getPlayer().getName();
        if (SpectatorMenu.CAMERA.containsValue(name)) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onEndGame(EndGameEvent e) {
        for (Player player : PlayerUtil.getSpectators()) {
            if (!SpectatorMenu.CAMERA.containsValue(player.getName()))
                continue;
            removeSpectator(player);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onEntityDamage(EntityDamageEvent e) {
        if (e.getEntity() instanceof Player) {
            Player player = (Player) e.getEntity();
            if (!PlayerUtil.isSpectator(player))
                return;
            if (e.getCause() == EntityDamageEvent.DamageCause.FIRE
                    || e.getCause() == EntityDamageEvent.DamageCause.FIRE_TICK) {
                nmsManager.disableFire(player);
            }
            e.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onEntityDamageByEntity(EntityDamageByEntityEvent e) {
        if (e.getDamager() instanceof Player) {
            Player player = (Player) e.getDamager();
            if (PlayerUtil.isSpectator(player)) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onEntityTarget(EntityTargetEvent e) {
        Entity entity = e.getTarget();
        if (entity instanceof Player) {
            if (PlayerUtil.isSpectator((Player) entity)) {
                e.setCancelled(true);
                e.setTarget(null);
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onFoodLevelChange(FoodLevelChangeEvent e) {
        if (e.getEntity() instanceof Player) {
            Player player = (Player) e.getEntity();
            if (PlayerUtil.isSpectator(player)) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onItemHeld(PlayerItemHeldEvent event) {
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItem(event.getNewSlot());
        this.runClosestPlayer(player, item);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerDropItem(PlayerDropItemEvent e) {
        Player player = e.getPlayer();
        if (PlayerUtil.isSpectator(player)) {
            e.setCancelled(true);
        }
    }

    // Исправить (ADVENTURE мод!)
    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerInteract(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        if (PlayerUtil.isSpectator(player)) {
            e.setCancelled(true);
            ItemStack hand = e.getItem();
            if (hand == null)
                return;
            if (e.getAction() == Action.LEFT_CLICK_BLOCK || e.getAction() == Action.LEFT_CLICK_AIR) {
                if (hand.getType() == Material.COMPASS) {
                    SPlayer sPlayer = SPlayer.getSPlayer(player);
                    Player nearPlayer = sPlayer.getNearPlayer();
                    if (nearPlayer != null)
                        player.teleport(nearPlayer.getLocation());
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerInteractEntity(PlayerInteractEntityEvent e) {
        Player player = e.getPlayer();
        if (PlayerUtil.isSpectator(player)) {
            e.setCancelled(true);
            if (e.getRightClicked() instanceof Player) {
                Player victim = (Player) e.getRightClicked();
                if (PlayerUtil.isAlive(victim)) {
                    Inventory inventory = PlayerInventory.getInventory(victim);
                    if (inventory == null)
                        return;
                    player.openInventory(inventory);
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        BukkitGamer gamer = gamerManager.getGamer(player);
        if (gamer == null) {
            return;
        }

        gamer.setGameMode(GameModeType.SPECTATOR);
        player.teleport(GameSettings.spectatorLoc);

        SPlayer sPlayer = SPlayer.getSPlayer(player);

        if (sPlayer.getHideSpectators() == 1) {
            sPlayer.setHideSpectators(1);
        } else if (sPlayer.getHideSpectators() == 0) {
            sPlayer.setHideSpectators(0);
        }

        sPlayer.getSpectatorSettings().updateInventory();
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {
        Player player = e.getPlayer();
        String whoName = SpectatorMenu.CAMERA.get(player.getName());
        if (whoName != null) {
            Player who = Bukkit.getPlayer(whoName);
            if (who != null) {
                if (player.getWorld() == who.getWorld() && who.getLocation().distance(player.getLocation()) > 10) {
                    who.teleport(player);
                }
            }
        }

        if (!PlayerUtil.isSpectator(player))
            return;

        try {
            if (SPlayer.getSPlayer(player).getAlwaysFly() == 1) {
                player.setFlying(true);
            }
            if (player.getLocation().getBlockY() <= 0) {
                player.setFlying(true);
                player.teleport(GameSettings.spectatorLoc);
            }
        } catch (Exception ignored) {
        }

    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerPickupItem(PlayerPickupItemEvent e) {
        Player player = e.getPlayer();
        if (PlayerUtil.isSpectator(player)) {
            e.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerQuit(PlayerQuitEvent e) {
        Player player = e.getPlayer();

        if (PlayerUtil.isSpectator(player)) {
            GameManager.getInstance().getSpectatorLoaders().addSetting(player);
            SPlayer.removeSPlayer(player);
            for (String alivePlayerName : SpectatorMenu.CAMERA.values()) {
                String name = SpectatorMenu.CAMERA.get(alivePlayerName);
                if (name == null)
                    continue;
                if (name.equals(player.getName())) {
                    SpectatorMenu.CAMERA.remove(player.getName());
                }
            }
        } else {
            String whoName = SpectatorMenu.CAMERA.remove(player.getName());
            if (whoName != null) {
                Player who = Bukkit.getPlayer(whoName);
                if (who != null) {
                    removeSpectator(who);
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPotionSplash(PotionSplashEvent e) {
        for (LivingEntity entity : e.getAffectedEntities()) {
            if (entity instanceof Player) {
                if (PlayerUtil.isSpectator((Player) entity)) {
                    e.setIntensity(entity, 0);
                }
            }
        }
    }

    @EventHandler
    public void onPressShift(PlayerToggleSneakEvent e) {
        Player player = e.getPlayer();
        if (!e.isSneaking())
            return;

        for (String aliveName : SpectatorMenu.CAMERA.keySet()) {
            Player alive = Bukkit.getPlayer(aliveName);
            if (alive == null)
                continue;
            if (!SpectatorMenu.CAMERA.get(aliveName).equals(player.getName()))
                continue;
            SpectatorMenu.CAMERA.remove(aliveName);
            player.teleport(alive);
            removeSpectator(player);
            break;
        }
    }

    @EventHandler
    public void onTarget(EntityTargetLivingEntityEvent e) {
        if (e.getTarget() instanceof Player) {
            Player player = (Player) e.getTarget();
            if (PlayerUtil.isSpectator(player)) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onTeleport(PlayerTeleportEvent event) {
        Player player = event.getPlayer();
        this.runClosestPlayer(player, player.getInventory().getItemInMainHand());
    }

    private void removeSpectator(Player spectator) {
        for (SPlayer sPlayer : SPlayer.sPlayers.values()) {
            if (sPlayer.getHideSpectators() == 0) {
                Player player = sPlayer.getPlayer();
                player.showPlayer(DartaAPI.getInstance(), spectator);
            }
        }
        BukkitGamer gamer = gamerManager.getGamer(spectator);
        if (gamer == null)
            return;
        actionBarAPI.sendBar(spectator, "§cВы покинули режим наблюдателя");
        PacketCamera packet = container.getCameraPacket(spectator);
        packet.sendPacket(spectator);
    }

    private void runClosestPlayer(final Player player, ItemStack itemInHand) {
        if (itemInHand != null && itemInHand.getType() == Material.COMPASS) {
            if (Bukkit.getOnlinePlayers().size() > 0) {
                if (mapTaskClosestPlayer.get(player.getName()) == null) {
                    BukkitTask bt = Bukkit.getScheduler().runTaskTimer(DartaAPI.getInstance(),
                            () -> findNearPlayer(player), 0L, 10L);
                    mapTaskClosestPlayer.put(player.getName(), bt);
                }
            }
        } else {
            if (mapTaskClosestPlayer.get(player.getName()) != null) {
                mapTaskClosestPlayer.get(player.getName()).cancel();
            }

            mapTaskClosestPlayer.remove(player.getName());
            actionBarAPI.sendBar(player, "");
        }
    }
}