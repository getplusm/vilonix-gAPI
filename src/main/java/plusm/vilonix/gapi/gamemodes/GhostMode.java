package plusm.vilonix.gapi.gamemodes;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import plusm.vilonix.gapi.loader.DartaAPI;
import plusm.vilonix.gapi.utils.core.PlayerUtil;

public class GhostMode {

    public static void removeGhostMode(Player player) {
        GameModeScoreBoardTeam.removePlayerGhostTeam(player);
        player.setCollidable(true);

        for (Player bPlayer : Bukkit.getOnlinePlayers()) {
            if (PlayerUtil.isGhost(bPlayer)) {
                bPlayer.hidePlayer(DartaAPI.getInstance(), player);
            } else {
                bPlayer.showPlayer(DartaAPI.getInstance(), player);
            }
        }

    }

    public static void setGhostMode(Player player) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 0));

        GameModeScoreBoardTeam.addPlayerGhostTeam(player);
        player.setCollidable(false);

        player.setGameMode(GameMode.ADVENTURE);

        for (Player bPlayer : Bukkit.getOnlinePlayers()) {
            if (PlayerUtil.isGhost(bPlayer)) {
                bPlayer.showPlayer(DartaAPI.getInstance(), player);
            } else {
                bPlayer.hidePlayer(DartaAPI.getInstance(), player);
            }
        }
    }

}
