package plusm.vilonix.gapi.module.stats;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import plusm.vilonix.api.player.BukkitGamer;
import plusm.vilonix.api.sql.GlobalLoader;

import java.util.LinkedHashMap;
import java.util.Map;

public class StatsPlayer {

    private final Player player;
    private final int playerID;
    private final Map<String, Integer> stats = new LinkedHashMap<>();

    public StatsPlayer(final Player player, final int playerID) {
        this.player = player;
        this.playerID = playerID;
    }

    public void loadStats(Stats mainStats, boolean global) {
        for (final String column : Stats.getStatsList()) {
            this.stats.put(column, global ? mainStats.getMainStats(playerID).get(column) : 0);
        }
    }

    public void addStats(final String column, final int amount) {
        this.stats.replace(column, this.stats.get(column) + amount);
    }

    public Player getPlayer() {
        return this.player;
    }

    public int getPlayerID() {
        return this.playerID;
    }

    public Map<String, Integer> getStats() {
        return this.stats;
    }

    public int getStats(final String column) {
        return this.stats.get(column);
    }

    public static StatsPlayer getPlayer(int playerID, Stats mainStats, boolean global) {
        String name = GlobalLoader.getName(playerID);
        if (Stats.getStatsPlayers().containsKey(playerID)) {
            StatsPlayer sp = new StatsPlayer(Bukkit.getPlayerExact(name), playerID);
            sp.loadStats(mainStats, global);
            return sp;
        }
        return Stats.getStatsPlayers().get(playerID);
    }

    public static StatsPlayer getPlayer(BukkitGamer gamer, Stats mainStats, boolean global) {
        if (Stats.getStatsPlayers().containsKey(gamer.getPlayerID())) {
            StatsPlayer sp = new StatsPlayer(gamer.getPlayer(), gamer.getPlayerID());
            sp.loadStats(mainStats, global);
            return sp;
        }
        return Stats.getStatsPlayers().get(gamer.getPlayerID());
    }

    public static StatsPlayer getPlayer(Player p, Stats mainStats, boolean global) {
        int playerID = GlobalLoader.getPlayerID(p.getName());
        if (Stats.getStatsPlayers().containsKey(playerID)) {
            StatsPlayer sp = new StatsPlayer(p, GlobalLoader.getPlayerID(p.getName()));
            sp.loadStats(mainStats, global);
            return sp;
        }
        return Stats.getStatsPlayers().get(playerID);
    }

    public static StatsPlayer getCachedPlayer(int playerID) {
        return Stats.getStatsPlayers().get(playerID);
    }
}
