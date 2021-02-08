package plusm.vilonix.gapi.module.stats;

import org.bukkit.entity.Player;
import plusm.vilonix.api.VilonixNetwork;
import plusm.vilonix.api.player.BukkitGamer;
import plusm.vilonix.api.player.GamerManager;
import plusm.vilonix.api.sql.ConnectionConstants;
import plusm.vilonix.api.sql.GlobalLoader;
import plusm.vilonix.api.sql.api.MySqlDatabase;
import plusm.vilonix.api.sql.api.query.MysqlQuery;
import plusm.vilonix.api.sql.api.query.QuerySymbol;
import plusm.vilonix.gapi.utils.bukkit.BukkitUtil;
import plusm.vilonix.gapi.utils.core.CoreUtil;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Stats {
    private static final GamerManager GAMER_MANAGER = VilonixNetwork.getGamerManager();
    private static final List<String> stats = new ArrayList<>();
    private static final Map<Integer, StatsPlayer> statsPlayers = new ConcurrentHashMap<>();
    private final String table;

    public static final MySqlDatabase MYSQL_DATABASE = MySqlDatabase.newBuilder()
            .host(ConnectionConstants.DOMAIN.getValue())
            .password(ConnectionConstants.PASSWORD.getValue())
            .user(ConnectionConstants.USER.getValue())
            .data("stats")
            .create();

    public Stats(final String table, final Collection<String> columns) {
        stats.clear();
        this.table = table;
        System.out.println("§b§lВ реестер статы добавлено " + columns.size() + " столбцов");
        stats.addAll(columns);

        final StringBuilder req = new StringBuilder("CREATE TABLE IF NOT EXISTS `" + this.table + "` (`ID` INT AUTO_INCREMENT PRIMARY KEY, `PlayerID` INT NOT NULL UNIQUE");
        for (final String column : Stats.stats)
            req.append(",`").append(column).append("` MEDIUMINT DEFAULT '0'");
        req.append(");");
        MYSQL_DATABASE.execute(req.toString());

        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(this::save, 0, 5, TimeUnit.MINUTES);
    }

    public static List<String> getStatsList() {
        return stats;
    }

    public void createPlayerStats(final Player player, boolean global) {
        final int playerID = GlobalLoader.getPlayerID(player.getName());
        StatsPlayer sp = new StatsPlayer(player, playerID);
        statsPlayers.put(playerID, sp);
        MYSQL_DATABASE.executeQuery("SELECT * FROM `" + this.table + "` WHERE `PlayerID`= ? LIMIT 1;", rs -> {
            if (!rs.next())
                MYSQL_DATABASE.execute(MysqlQuery.insertTo(table).set("PlayerID", playerID));
            return Void.TYPE;
        }, playerID);
        sp.loadStats(this, global);
    }

    public void createPlayerStats(final BukkitGamer gamer, boolean global) {
        final int playerID = gamer.getPlayerID();
        StatsPlayer sp = new StatsPlayer(gamer.getPlayer(), playerID);
        statsPlayers.put(playerID, sp);
        MYSQL_DATABASE.executeQuery("SELECT * FROM `" + this.table + "` WHERE `PlayerID`= ? LIMIT 1;", rs -> {
            if (!rs.next())
                MYSQL_DATABASE.execute(MysqlQuery.insertTo(table).set("PlayerID", playerID));
            return Void.TYPE;
        }, playerID);
        sp.loadStats(this, global);
    }

    public Map<String, Integer> getMainStats(final int playerId) {
        final Map<String, Integer> stats = new HashMap<>();
        return MYSQL_DATABASE.executeQuery(MysqlQuery.selectFrom(table).where("PlayerID", QuerySymbol.EQUALLY, playerId).limit(1).toString(), rs -> {
            if (rs.next()) {
                for (final String string : getStatsList()) {
                    stats.put(string, rs.getInt(string));
                }
            }
            return stats;
        });
    }

    public int getPlayerStats(final Player player, final String column) {
        return statsPlayers.get(GlobalLoader.getPlayerID(player.getName())).getStats(column);
    }

    public int getPlayerStats(final BukkitGamer gamer, final String column) {
        return statsPlayers.get(gamer.getPlayerID()).getStats(column);
    }

    public int getPlayerStats(final StatsPlayer player, final String column) {
        return statsPlayers.get(player.getPlayerID()).getStats(column);
    }

    public Map<Integer, Integer> getPlayersTop(final String column) {
        final Map<Integer, Integer> playersTop = new HashMap<>();
        for (final Map.Entry<Integer, StatsPlayer> statsPlayer : statsPlayers.entrySet()) {
            playersTop.put(statsPlayer.getKey(), statsPlayer.getValue().getStats(column));
        }
        return CoreUtil.sortByValue(playersTop);
    }

    public static Map<Integer, StatsPlayer> getStatsPlayers() {
        return statsPlayers;
    }

    public void save() {
        BukkitUtil.runTaskAsync(() -> {
            for (final StatsPlayer stats : statsPlayers.values()) {
                for (final Map.Entry<String, Integer> column : stats.getStats().entrySet()) {
                    MYSQL_DATABASE.execute(MysqlQuery
                            .update(table)
                            .set(column.getKey(), column.getValue())
                            .where("PlayerID", QuerySymbol.EQUALLY, stats.getPlayerID()));
                }
            }
        });
    }

    public void save(StatsPlayer sp) {
        BukkitUtil.runTaskAsync(() -> {
            for (final Map.Entry<String, Integer> column : sp.getStats().entrySet()) {
                MYSQL_DATABASE.execute(MysqlQuery
                        .update(table)
                        .set(column.getKey(), column.getValue())
                        .where("PlayerID", QuerySymbol.EQUALLY, sp.getPlayerID()));
            }
        });
    }

    public static int[] getStats(final String table, final int playerID) {
        return MYSQL_DATABASE.executeQuery("SELECT * FROM `" + table + "` WHERE `PlayerID` = ? LIMIT 1;", rs -> {
            int[] stats = null;
            if (rs.next()) {
                stats = new int[15];
                if (rs.next()) {
                    for (int i = 3; i <= 15; ++i) {
                        stats[i - 3] = rs.getInt(i);
                    }
                } else {
                    for (int i = 0; i < 15; ++i) {
                        stats[i] = 0;
                    }
                }
            }
            return stats;
        }, playerID);
    }
}
