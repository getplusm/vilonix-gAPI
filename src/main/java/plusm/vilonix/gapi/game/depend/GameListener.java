package plusm.vilonix.gapi.game.depend;

import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import plusm.vilonix.api.VilonixNetwork;
import plusm.vilonix.api.game.GameSettings;
import plusm.vilonix.api.game.MiniGameType;
import plusm.vilonix.api.game.TeamManager;
import plusm.vilonix.api.player.BukkitGamer;
import plusm.vilonix.api.player.GamerManager;
import plusm.vilonix.api.player.constans.PurchaseType;
import plusm.vilonix.gapi.game.team.SelectionTeam;
import plusm.vilonix.gapi.utils.DListener;
import plusm.vilonix.gapi.utils.bukkit.BukkitUtil;
import plusm.vilonix.gapi.utils.core.PlayerUtil;
import plusm.vilonix.libraries.LibAPI;
import plusm.vilonix.libraries.interfaces.NmsManager;


@Getter
public class GameListener extends DListener {

    private final GamerManager gamerManager = VilonixNetwork.getGamerManager();
    private final NmsManager nmsManager = LibAPI.getManager();

    @EventHandler
    public void doMobSpawn(CreatureSpawnEvent e) {
        e.setCancelled(true);
    }

    // запрещаем ломать блоки под тиммейтами
    @EventHandler(priority = EventPriority.LOWEST)
    public void onBlockBreak(BlockBreakEvent e) {
        if (!GameSettings.teamMode)
            return;

        Block block = e.getBlock();
        if (block.getType() == Material.BED_BLOCK || block.getType() == Material.BED || block.getType() == Material.DRAGON_EGG || block.getType() == Material.CHEST || block.getType() == Material.TRAPPED_CHEST) {
            return;
        }

        Player player = e.getPlayer();
        BukkitGamer gamer = gamerManager.getGamer(player);
        if (gamer == null)
            return;

        TeamManager team = SelectionTeam.getSelectedTeams().get(player);
        for (Player teamPlayer : SelectionTeam.getPlayersByTeam(team)) {
            if (PlayerUtil.isSpectator(teamPlayer))
                continue;
            if (player == teamPlayer)
                continue;
            if (nmsManager.getBlocksBelow(teamPlayer).contains(block)) {
                e.setCancelled(true);
                player.sendMessage(GameSettings.prefix + "§cВы не можете ломать блоки под своими тиммейтами");
                return;
            }
        }
    }

    @EventHandler
    public void onFish(PlayerFishEvent e) {
        if (GameSettings.minigame == MiniGameType.SURVIVAL) {
            return;
        }
        if (e.getState() == PlayerFishEvent.State.CAUGHT_FISH) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerItemConsume(PlayerItemConsumeEvent e) {
        Player player = e.getPlayer();
        if (e.getItem().getType() != Material.POTION) {
            return;
        }

        BukkitUtil.runTaskLaterAsync(1, () -> {
            PlayerInventory inventory = player.getInventory();

            ItemStack itemInOffHand = inventory.getItemInOffHand();
            if (itemInOffHand != null && itemInOffHand.getType() == Material.GLASS_BOTTLE) {
                inventory.setItemInOffHand(null);
            } else {
                inventory.setItemInMainHand(null);
            }
        });
    }

    // запрещаем наносить урон тиммейтам через зельки
    @EventHandler(priority = EventPriority.MONITOR)
    public void onPotionThrow(PotionSplashEvent e) {
        if (!GameSettings.teamMode)
            return;

        ThrownPotion potion = e.getEntity();
        if (!(potion.getShooter() instanceof Player))
            return;

        Player player = (Player) potion.getShooter();

        TeamManager team = SelectionTeam.getSelectedTeams().get(player);
        for (Player teamPlayer : SelectionTeam.getPlayersByTeam(team)) {
            if (player == teamPlayer)
                continue;
            if (!e.getAffectedEntities().contains(teamPlayer))
                continue;
            for (PotionEffect potionEffect : potion.getEffects()) {
                if (potionEffect.getType().getName().equals("POISON")) {
                    e.setIntensity(teamPlayer, 0L);
                }
            }
        }
    }

    // тут выдавать монеты и тд, когда игрок ливает во время игры
    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        BukkitGamer gamer = gamerManager.getGamer(player);
        if (gamer == null)
            return;

        int keysLocal = gamer.getKeysLocal();
        int moneyLocal = gamer.getMoneyLocal();
        int expLocal = gamer.getExpLocal();

        if (moneyLocal >= 1) {
            gamer.changeMoney(PurchaseType.MONEY, (int) (moneyLocal * gamer.getMultiple()));
            gamer.setMoneyLocal();
        }
        if (expLocal >= 1) {
            gamer.addExp(expLocal);
            gamer.setExpLocal();
        }
        if (keysLocal >= 1) {
            // gamer.changeKeys(KeyType.DEFAULT_KEY, keysLocal); больше не даем ключи
            gamer.setKeysLocal();
        }
        if (!PlayerUtil.isSpectator(player)) {
            for (BukkitGamer all : gamerManager.getGamers().values()) {
                all.sendMessage(gamer.getDisplayName() + "§f покинул игру!");
            }
            if (!GameSettings.teamMode) {
                GAMER_MANAGER.removeGamer(player);
            }

        }
    }
}
