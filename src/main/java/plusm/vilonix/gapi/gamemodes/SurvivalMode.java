package plusm.vilonix.gapi.gamemodes;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;

public class SurvivalMode {
    public static void setSurvivalMode(Player player) {
        player.setGameMode(GameMode.SURVIVAL);
    }
}
