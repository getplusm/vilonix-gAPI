package plusm.vilonix.gapi.game.boards;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import plusm.vilonix.api.VilonixNetwork;
import plusm.vilonix.api.game.GameSettings;
import plusm.vilonix.api.game.GameState;
import plusm.vilonix.api.player.BukkitGamer;
import plusm.vilonix.api.player.GamerManager;
import plusm.vilonix.api.scoreboard.Board;
import plusm.vilonix.api.scoreboard.ScoreBoardAPI;
import plusm.vilonix.api.util.StringUtil;
import plusm.vilonix.gapi.game.module.WaitModule;
import plusm.vilonix.gapi.utils.core.CoreUtil;
import plusm.vilonix.gapi.utils.core.PlayerUtil;

@Getter
public class WaitingBoard {

    private static final GamerManager GAMER_MANAGER = VilonixNetwork.getGamerManager();

    private static final ScoreBoardAPI SCORE_BOARD_API = VilonixNetwork.getScoreBoardAPI();
    private int s;

    public WaitingBoard(Player player) {
        BukkitGamer gamer = GAMER_MANAGER.getGamer(player);
        if (gamer == null)
            return;

        Board board = SCORE_BOARD_API.createBoard();
        board.setDynamicDisplayName(GameSettings.displayName);

        board.updater(() -> {
            board.setDynamicLine(7, "Игроков: §e", Bukkit.getOnlinePlayers().size() + "/" + GameSettings.slots);
            if (GameSettings.toStart > Bukkit.getOnlinePlayers().size()
                    && GameState.getCurrent() == GameState.WAITING) {
                board.setDynamicLine(6, "Для старта нужно: §e", String.valueOf(GameSettings.toStart - Bukkit.getOnlinePlayers().size()));
            } else {
                board.setLine(6, StringUtil.getLineCode(6));
            }
            if (GameState.getCurrent() == GameState.WAITING) {
                ++this.s;
                StringBuilder t = new StringBuilder();
                for (int i = 1; i < s; ++i) {
                    t.append(".");
                }
                board.setDynamicLine(3, "§rОжидание", t.toString());
                if (this.s == 4) {
                    s = 0;
                }
            } else {
                board.setDynamicLine(3, "Начало через: §e",
                        StringUtil.getCompleteTime(WaitModule.getTime()));
            }
        });
        board.setLine(9, "§7" + GameSettings.typeGame.getType() + " " + StringUtil.getDate());
        board.setLine(8, StringUtil.getLineCode(8));
        board.setLine(5, "Карта: §a" + CoreUtil.getGameWorld());
        board.setLine(4, StringUtil.getLineCode(4));
        board.setLine(2, StringUtil.getLineCode(2));
        //board.setLine(1, lang.getMessage("BOARD_SERVER") + ": §c" + CoreConnector.getInstance().getServer().getName());

        board.showTo(player);
    }

}
