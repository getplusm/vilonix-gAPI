package plusm.vilonix.gapi.loader;

import org.bukkit.entity.Player;
import plusm.vilonix.api.VilonixNetwork;
import plusm.vilonix.api.player.BukkitGamer;
import plusm.vilonix.api.player.GamerManager;
import plusm.vilonix.api.sql.ConnectionConstants;
import plusm.vilonix.api.sql.api.MySqlDatabase;
import plusm.vilonix.gapi.game.spectator.SPlayer;

public class SpectatorLoader {

    private static final GamerManager GAMER_MANAGER = VilonixNetwork.getGamerManager();
    private static final MySqlDatabase MYSQL_DATABASE = MySqlDatabase.newBuilder()
            .host(ConnectionConstants.DOMAIN.getValue())
            .password(ConnectionConstants.PASSWORD.getValue())
            .user(ConnectionConstants.USER.getValue())
            .data("network")
            .create();

    public SpectatorLoader() {
        connect();
    }

    public static MySqlDatabase getMysqlDatabase() {
        return MYSQL_DATABASE;
    }

    public void addSetting(Player player) {
        BukkitGamer gamer = GAMER_MANAGER.getGamer(player);
        SPlayer sPlayer = SPlayer.getSPlayer(player);
        assert gamer != null;
        MYSQL_DATABASE.execute("UPDATE `spectator_settings` SET `speed`=" + sPlayer.getSpeedSpec() + ",`fly`="
                + sPlayer.getAlwaysFly() + ",`spectate`=" + sPlayer.getHideSpectators() + ",`vision`="
                + sPlayer.getVision() + " WHERE `id`=" + gamer.getPlayerID() + ";");
    }

    private void connect() {
        MYSQL_DATABASE.execute(
                "CREATE TABLE IF NOT EXISTS `spectator_settings` (`id` INT(11) PRIMARY KEY, `speed` INT, `fly` INT, `spectate` INT, `vision` INT)");
    }

    public int[] getStats(int playerID) {
        return MYSQL_DATABASE.executeQuery("SELECT * FROM spectator_settings WHERE id = ?", rs -> {
            int[] result = {0, 0, 0, 0};
            if (rs.next()) {
                result[0] = rs.getInt("speed");
                result[1] = rs.getInt("fly");
                result[2] = rs.getInt("spectate");
                result[3] = rs.getInt("vision");
            } else
                MYSQL_DATABASE.execute(
                        "INSERT INTO `spectator_settings` (`id`, `speed`, `fly`, `spectate`, `vision`) VALUES ('"
                                + playerID + "', 0, 0, 0, 0);");
            return result;
        }, playerID);
    }
}
