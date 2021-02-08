package plusm.vilonix.gapi.game.boards;

import lombok.Getter;
import plusm.vilonix.api.VilonixNetwork;
import plusm.vilonix.api.game.GameSettings;
import plusm.vilonix.api.player.BukkitGamer;
import plusm.vilonix.api.scoreboard.Board;
import plusm.vilonix.api.scoreboard.ScoreBoardAPI;
import plusm.vilonix.api.util.StringUtil;

@Getter
public class EndBoard {

    private static final ScoreBoardAPI SCORE_BOARD_API = VilonixNetwork.getScoreBoardAPI();

    public static void createBoard(BukkitGamer gamer, String line1, String line2) {
        if (gamer == null) {
            return;
        }

        Board board = SCORE_BOARD_API.createBoard();
        board.setDynamicDisplayName(GameSettings.displayName);

        board.setLine(7, "§7" + GameSettings.typeGame.getType() + " " + StringUtil.getDate());
        board.setLine(6, StringUtil.getLineCode(6));
        board.setLine(5, StringUtil.getLineCode(5));
        board.setLine(4, line1);
        board.setLine(3, line2);
        board.setLine(2, StringUtil.getLineCode(2));
        board.setLine(1, StringUtil.getLineCode(1));

        board.showTo(gamer);
    }
}
