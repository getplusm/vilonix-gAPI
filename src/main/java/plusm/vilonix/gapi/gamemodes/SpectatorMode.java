package plusm.vilonix.gapi.gamemodes;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import plusm.vilonix.api.VilonixNetwork;
import plusm.vilonix.api.game.GameModeType;
import plusm.vilonix.api.player.BukkitGamer;
import plusm.vilonix.api.player.GamerManager;
import plusm.vilonix.gapi.game.GameManager;
import plusm.vilonix.gapi.game.ItemsListener;
import plusm.vilonix.gapi.game.boards.SpectatorBoard;
import plusm.vilonix.gapi.game.spectator.SPlayer;
import plusm.vilonix.gapi.loader.DartaAPI;
import plusm.vilonix.gapi.utils.core.PlayerUtil;

import java.util.Objects;

public class SpectatorMode {

    private static final GamerManager GAMER_MANAGER = VilonixNetwork.getGamerManager();

    public static void removeSpectatorMode(Player player) {
        GameModeScoreBoardTeam.removePlayerSpectatorTeam(player);
        player.setCollidable(true);

        player.setAllowFlight(false);
        player.setFlying(false);

        GameManager.getInstance().getSpectatorLoaders().addSetting(player);

        for (Player all : Bukkit.getOnlinePlayers()) {
            if (all == player)
                continue;
            if (Objects.requireNonNull(GAMER_MANAGER.getGamer(all)).getGameMode() == GameModeType.SPECTATOR) {
                SPlayer splayer = SPlayer.getSPlayer(all);
                if (splayer == null)
                    continue;
                if (splayer.getHideSpectators() == 1)
                    all.showPlayer(DartaAPI.getInstance(), player);
                player.hidePlayer(DartaAPI.getInstance(), all);
            } else {
                all.showPlayer(DartaAPI.getInstance(), player);
            }
        }

        SPlayer.removeSPlayer(player);
    }

    public static void setSpectatorMode(Player player) {
        if (player == null || !player.isOnline())
            return;

        player.getInventory().setItem(0, ItemsListener.getTeleporter(player));
        player.getInventory().setItem(4, ItemsListener.getSpectatorSettings(player));
        player.getInventory().setItem(8, ItemsListener.getHub(player));

        try {
            player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 0));
        } catch (Exception ignored) {

        }

        GameModeScoreBoardTeam.addPlayerSpectatorTeam(player);
        player.setCollidable(false);

        player.setGameMode(GameMode.ADVENTURE);

        player.setAllowFlight(true);
        player.setFlying(true);

        SPlayer sPlayer = SPlayer.getSPlayer(player);

        switch (sPlayer.getSpeedSpec()) {
            case 0:
                PlayerUtil.removePotionEffect(player, PotionEffectType.SPEED);
                sPlayer.getSpectatorSettings().updateInventory();
                break;
            case 1:
            case 2:
            case 3:
            case 4:
                PlayerUtil.addPotionEffect(player, PotionEffectType.SPEED, (sPlayer.getSpeedSpec() - 1));
                sPlayer.getSpectatorSettings().updateInventory();
                break;
        }

        for (Player p : Bukkit.getOnlinePlayers()) {
            if (p == player)
                continue;
            BukkitGamer dp = GAMER_MANAGER.getGamer(p);
            if (dp == null)
                continue;
            if (dp.getGameMode() == GameModeType.SPECTATOR) {
                SPlayer splayer = SPlayer.getSPlayer(p);

                if (splayer == null)
                    continue;

                if (splayer.getHideSpectators() == 1) {
                    p.hidePlayer(DartaAPI.getInstance(), player);
                }
                player.showPlayer(DartaAPI.getInstance(), p);
                splayer.getSpectatorSettings().updateInventory();
            } else {
                p.hidePlayer(DartaAPI.getInstance(), player);
            }
        }

        if (sPlayer.getAlwaysFly() == 1) {
            sPlayer.setAlwaysFly(1);
        } else if (sPlayer.getAlwaysFly() == 0) {
            sPlayer.setAlwaysFly(0);
        }
        if (sPlayer.getVision() == 1) {
            sPlayer.setVision(1);
        } else if (sPlayer.getVision() == 0) {
            sPlayer.setVision(0);
        }
        if (sPlayer.getHideSpectators() == 1) {
            sPlayer.setHideSpectators(1);
        } else if (sPlayer.getHideSpectators() == 0) {
            sPlayer.setHideSpectators(0);
        }
        sPlayer.getSpectatorSettings().updateInventory();

        SpectatorBoard.createBoard(player);
    }

}
