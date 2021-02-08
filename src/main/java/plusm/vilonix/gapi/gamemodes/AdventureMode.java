package plusm.vilonix.gapi.gamemodes;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;

public class AdventureMode {
    public static void setAdventureMode(Player player) {
        player.setGameMode(GameMode.ADVENTURE);
    }
}
