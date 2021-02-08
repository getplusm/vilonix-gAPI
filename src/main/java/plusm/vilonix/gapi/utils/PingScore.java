package plusm.vilonix.gapi.utils;

import lombok.experimental.UtilityClass;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import plusm.vilonix.api.VilonixNetwork;
import plusm.vilonix.api.player.BukkitGamer;
import plusm.vilonix.api.scoreboard.DisplaySlot;
import plusm.vilonix.api.scoreboard.Objective;
import plusm.vilonix.api.scoreboard.ScoreBoardAPI;

@UtilityClass
public class PingScore {

    private final static ScoreBoardAPI SCORE_BOARD_API = VilonixNetwork.getScoreBoardAPI();
    private static Objective objectivesPing;

    public static void setPingScore(BukkitGamer gamer) {
        Player player = gamer.getPlayer();
        if (player == null || !player.isOnline()) {
            return;
        }

        if (objectivesPing == null)
            registerPingObjectives();

        new BukkitRunnable() {

            @Override
            public void run() {
                objectivesPing.setScore(player, ((CraftPlayer) player).getHandle().ping);
            }
        }.runTaskTimer(VilonixNetwork.getGamerManager().getSpigot().getMainPlugin(), 20 * 10, 20 * 10);
    }

    private static void registerPingObjectives() {
        objectivesPing = SCORE_BOARD_API.createObjective("ping", "dummy");
        objectivesPing.setDisplaySlot(DisplaySlot.LIST);
        objectivesPing.setDisplayName("ms");
        objectivesPing.setPublic(true);
    }

}