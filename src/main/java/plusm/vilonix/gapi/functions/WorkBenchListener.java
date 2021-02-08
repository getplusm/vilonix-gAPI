package plusm.vilonix.gapi.functions;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import plusm.vilonix.api.game.GameModeType;
import plusm.vilonix.api.mAPI;
import plusm.vilonix.api.player.BukkitGamer;
import plusm.vilonix.api.util.ConfigUtil;
import plusm.vilonix.gapi.utils.DListener;

import java.util.Objects;

public class WorkBenchListener extends DListener {

    @EventHandler
    public void onWorkBenchCommand(PlayerCommandPreprocessEvent e) {
        if (e.getMessage().equalsIgnoreCase("/wb") || e.getMessage().equalsIgnoreCase("/workbench")) {
            Player player = e.getPlayer();
            BukkitGamer gamer = GAMER_MANAGER.getGamer(player);
            assert gamer != null;
            if (gamer.getGameMode() != GameModeType.SPECTATOR) {
                if (gamer.isPrimo()) {
                    player.openWorkbench(null, true);
                } else {
                    gamer.sendMessage(ConfigUtil.getMessage("NO_PERMS_GROUP", f -> f.replace("%minimal_group%", Objects.requireNonNull(mAPI.getLuckPerms().getGroupManager().getGroup("PRIMO").getDisplayName()))));
                }
                e.setCancelled(true);
            } else {
                gamer.sendMessage("§6Vilonix §8> §cДанная команда не доступна во время игры");
                e.setCancelled(true);
            }
        }
    }
}
