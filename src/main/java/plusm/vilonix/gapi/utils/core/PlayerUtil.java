package plusm.vilonix.gapi.utils.core;

import lombok.Getter;
import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import plusm.vilonix.api.BorderAPI;
import plusm.vilonix.api.TitleAPI;
import plusm.vilonix.api.VilonixNetwork;
import plusm.vilonix.api.game.GameModeType;
import plusm.vilonix.api.game.GameSettings;
import plusm.vilonix.api.game.GameState;
import plusm.vilonix.api.player.BukkitGamer;
import plusm.vilonix.api.player.GamerAPI;
import plusm.vilonix.api.player.GamerManager;
import plusm.vilonix.api.sound.SoundAPI;
import plusm.vilonix.api.sound.SoundType;
import plusm.vilonix.api.util.LocationUtil;
import plusm.vilonix.api.util.StringUtil;
import plusm.vilonix.gapi.loader.DartaAPI;
import plusm.vilonix.gapi.utils.bukkit.BukkitUtil;
import plusm.vilonix.gapi.utils.games.TitleUtil;
import plusm.vilonix.libraries.LibAPI;
import plusm.vilonix.libraries.interfaces.NmsManager;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@UtilityClass
@Getter
public class PlayerUtil {

    private final static BorderAPI BOARDER_API = VilonixNetwork.getBorderAPI();
    private final static GamerManager GAMER_MANAGER = VilonixNetwork.getGamerManager();
    private final static NmsManager NMS_MANAGER = LibAPI.getManager();
    private final static SoundAPI SOUND_API = VilonixNetwork.getSoundAPI();
    private final static TitleAPI TITLE_API = VilonixNetwork.getTitlesAPI();

    public static void addPotionEffect(Player player, PotionEffectType potionEffectType, int level) {
        if (player == null) {
            return;
        }

        if (havePotionEffectType(player, potionEffectType))
            removePotionEffect(player, potionEffectType);

        player.addPotionEffect(new PotionEffect(potionEffectType, Integer.MAX_VALUE, level));
    }

    public static void death(Player death) {
        Player killer = death.getKiller();
        reset(death);
        if (killer == null)
            return;

        SOUND_API.play(killer, SoundType.POSITIVE);
    }

    public static Collection<Player> getAlivePlayers() {
        Set<Player> players = new HashSet<>();
        for (String playerName : GamerAPI.getGamers().keySet()) {
            Player player = Bukkit.getPlayerExact(playerName);
            if (isAlive(player))
                players.add(player);

        }
        return players;
    }

    public static Collection<Player> getGhosts() {
        Set<Player> players = new HashSet<>();
        for (String playerName : GamerAPI.getGamers().keySet()) {
            Player player = Bukkit.getPlayerExact(playerName);
            if (player == null)
                continue;
            if (isGhost(player))
                players.add(player);

        }
        return players;
    }

    public static Collection<Player> getNearbyPlayers(Location location, int radius) {
        return Bukkit.getOnlinePlayers().stream()
                .filter(player -> LocationUtil.distance(player.getLocation(), location) <= radius
                        && LocationUtil.distance(player.getLocation(), location) != -1)
                .collect(Collectors.toCollection(HashSet::new));
    }

    public static Collection<Player> getNearbyPlayers(Player player, int radius) {
        return player.getNearbyEntities(radius, radius, radius).stream().filter(entity -> entity instanceof Player)
                .map(entity -> (Player) entity).collect(Collectors.toCollection(HashSet::new));
    }

    public static int getPotionEffectLevel(Player player, PotionEffectType potionEffectType) {
        PotionEffect effect = player.getActivePotionEffects().stream()
                .filter(potionEffect -> potionEffect.getType() == potionEffectType).findFirst().orElse(null);
        return effect != null ? effect.getAmplifier() : -1;
    }

    public static Collection<Player> getSpectators() {
        Set<Player> players = new HashSet<>();
        for (String playerName : GamerAPI.getGamers().keySet()) {
            Player player = Bukkit.getPlayerExact(playerName);
            if (player == null)
                continue;
            if (isSpectator(player))
                players.add(player);

        }
        return players;
    }

    public static boolean havePotionEffectType(Player player, PotionEffectType potionEffectType) {
        return player.getActivePotionEffects().stream()
                .filter(potionEffect -> potionEffect.getType() == potionEffectType).collect(Collectors.toList())
                .size() > 0;
    }

    public static boolean isAlive(Player player) {
        if (player != null && player.isOnline()) {
            BukkitGamer gamer = GAMER_MANAGER.getGamer(player);
            if (gamer != null) {
                GameModeType gameModeType = gamer.getGameMode();
                return gameModeType != GameModeType.SPECTATOR;
            }
        }
        return false;
    }

    public static boolean isGhost(Player player) {
        if (player != null && player.isOnline()) {
            BukkitGamer gamer = GAMER_MANAGER.getGamer(player);
            if (gamer != null) {
                GameModeType gameModeType = gamer.getGameMode();
                return gameModeType == GameModeType.GHOST;
            }
        }
        return false;
    }

    public static boolean isSpectator(Player player) {
        if (player != null && player.isOnline()) {
            BukkitGamer gamer = GAMER_MANAGER.getGamer(player);
            if (gamer != null) {
                GameModeType gameModeType = gamer.getGameMode();
                return gameModeType == GameModeType.SPECTATOR;
            }
        }
        return false;
    }

    public static void redirectToHub(Player player) {
        CoreUtil.getRandom(GameSettings.hubs); //CoreUtil.getRandom(GameSettings.hubs)
    }
	
	public static void playerAgain(Player player) {
		//CoreConnector.getInstance().sendPacket(new PlayerAction(player.getName(), GameSettings.channel, PlayerAction.Type.JOINARENA));
	}

    public static void removePotionEffect(Player player, PotionEffectType potionEffectType) {
        player.removePotionEffect(potionEffectType);
    }

    @SuppressWarnings("deprecation")
    public static void reset(Player player) {
        if (player == null || !player.isOnline())
            return;

        try {
            player.getActivePotionEffects().forEach(potionEffect -> player.removePotionEffect(potionEffect.getType()));

            player.setHealth(player.getMaxHealth());
            player.setFoodLevel(20);
            player.setExp(0.0f);
            player.setLevel(0);
            player.setFireTicks(0);

            player.closeInventory();
            player.getInventory().clear();
            player.getInventory().setArmorContents(null);
        } catch (Exception ignored) {
        }

        BukkitUtil.runTask(() -> {
            if (!player.isOnline())
                return;
            NMS_MANAGER.disableFire(player);
            NMS_MANAGER.removeArrowFromPlayer(player);
        });
    }

    public static void setPrepare(Player player, Location loc) {
        BukkitGamer gamer = GAMER_MANAGER.getGamer(player);
        if (gamer == null)
            return;
        new BukkitRunnable() {
            int time = 10 * 20;

            @Override
            public void run() {
                if (!player.isOnline()) {
                    cancel();
                    return;
                }
                if (GameState.END == GameState.getCurrent()) {
                    Bukkit.getOnlinePlayers().forEach(p -> p.showPlayer(DartaAPI.getInstance(), player));
                    cancel();
                    return;
                }
                BOARDER_API.sendRedScreen(player);
                if (time == 0) {
                    Bukkit.getOnlinePlayers().forEach(p -> p.hidePlayer(DartaAPI.getInstance(), player));
                    Bukkit.getOnlinePlayers().forEach(p -> p.showPlayer(DartaAPI.getInstance(), player));
                    player.removePotionEffect(PotionEffectType.INVISIBILITY);
                    BukkitUtil.runTask(() -> NMS_MANAGER.disableFire(player));
                    player.setAllowFlight(false);
                    player.setFlying(false);
                    player.setFallDistance(0);
                    TitleUtil.StartGameTitle(player, "§b§l" + GameSettings.displayName + " §7" + GameSettings.typeGame.getType(), "§7Защитите свои кровати!");
                    cancel();
                    return;
                }
                TITLE_API.sendTitleAll(StringUtil.getUTFNumber(time / 20), StringUtil.getStateNumber(time / 20), 0, 20, 0);
                player.teleport(loc);
                time--;
            }
        }.runTaskTimer(DartaAPI.getInstance(), 5, 1);
    }

    public static void setRespawn(Player player, int respawnTime, Runnable runnable) {
        BukkitGamer gamer = GAMER_MANAGER.getGamer(player);
        if (gamer == null)
            return;
        reset(player);

        player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 0));

        Bukkit.getOnlinePlayers().forEach(p -> p.hidePlayer(DartaAPI.getInstance(), player));

        new BukkitRunnable() {

            String subTitle = "§7Вы возродитесь через " + respawnTime + " " + StringUtil.getCorrectWord(respawnTime, "TIME_SECOND_1");
            int time = respawnTime * 20;

            String title = "§cВы погибли";

            @Override
            public void run() {
                if (!player.isOnline()) {
                    cancel();
                    return;
                }
                if (GameState.END == GameState.getCurrent()) {
                    Bukkit.getOnlinePlayers().forEach(p -> p.showPlayer(DartaAPI.getInstance(), player));
                    cancel();
                    return;
                }
                BOARDER_API.sendRedScreen(player);
                player.setAllowFlight(true);
                player.setFlying(true);
                player.teleport(GameSettings.spectatorLoc);
                if (time == 0) {
                    Bukkit.getOnlinePlayers().forEach(p -> p.showPlayer(DartaAPI.getInstance(), player));

                    player.removePotionEffect(PotionEffectType.INVISIBILITY);
                    title = "§aВы возродились";
                    TITLE_API.sendTitle(player, title, " ", 0, 2 * 20, 0);

                    BukkitUtil.runTask(runnable);
                    BukkitUtil.runTask(() -> NMS_MANAGER.disableFire(player));

                    player.setAllowFlight(false);
                    player.setFlying(false);
                    player.setFallDistance(0);
                    cancel();
                    return;
                }
                if (time % 20 == 0) {
                    subTitle = "§7Вы возродитесь через " + (time / 20) + " " + StringUtil.getCorrectWord(time / 20, "TIME_SECOND_1");
                    TITLE_API.sendTitle(player, title, subTitle, 0, 2 * 20, 0);
                }
                time--;
            }
        }.runTaskTimer(DartaAPI.getInstance(), 5L, 1);

    }

    public void addPotionEffect(Player player, PotionEffectType potionEffectType, int level, int seconds) {
        if (havePotionEffectType(player, potionEffectType))
            removePotionEffect(player, potionEffectType);

        player.addPotionEffect(new PotionEffect(potionEffectType, seconds * 20, level));
    }
}
