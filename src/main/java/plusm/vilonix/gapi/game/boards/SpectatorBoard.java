package plusm.vilonix.gapi.game.boards;

import lombok.Getter;
import org.bukkit.entity.Player;
import plusm.vilonix.api.VilonixNetwork;
import plusm.vilonix.api.game.GameSettings;
import plusm.vilonix.api.player.BukkitGamer;
import plusm.vilonix.api.player.GamerManager;
import plusm.vilonix.api.scoreboard.Board;
import plusm.vilonix.api.scoreboard.ScoreBoardAPI;
import plusm.vilonix.api.util.StringUtil;
import plusm.vilonix.gapi.utils.core.CoreUtil;
import plusm.vilonix.gapi.utils.core.PlayerUtil;

@Getter
public class SpectatorBoard {

    private static final GamerManager GAMER_MANAGER = VilonixNetwork.getGamerManager();
    private static final ScoreBoardAPI SCORE_BOARD_API = VilonixNetwork.getScoreBoardAPI();

    public static void createBoard(Player player) {
        BukkitGamer gamer = GAMER_MANAGER.getGamer(player);
        if (gamer == null)
            return;

        Board board = SCORE_BOARD_API.createBoard();
        board.setDynamicDisplayName(GameSettings.displayName);

        board.setLine(7, "§7" + GameSettings.typeGame.getType() + " " + StringUtil.getDate());
        board.setLine(6, StringUtil.getLineCode(6));
        board.updater(() -> {
            board.setDynamicLine(5, "Игроков: §e",
                    String.valueOf(PlayerUtil.getAlivePlayers().size()));
            board.setDynamicLine(4, "Наблюдателей: §e",
                    String.valueOf(PlayerUtil.getSpectators().size()));
        });
        board.setLine(3, StringUtil.getLineCode(3));
        board.setLine(2, "Карта: §c" + CoreUtil.getGameWorld());
        board.setLine(1, "Сервер: §c" + GAMER_MANAGER.getSpigot().getName());

        board.showTo(player);
    }
}
