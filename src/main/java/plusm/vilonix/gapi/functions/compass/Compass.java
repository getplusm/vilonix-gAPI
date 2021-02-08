package plusm.vilonix.gapi.functions.compass;

import org.bukkit.entity.Player;
import plusm.vilonix.api.util.DList;
import plusm.vilonix.gapi.utils.core.PlayerUtil;

import java.util.Collection;

public class Compass {

    static {
        new CompassListener();
        new CompassTask();
    }

    private Player currentPlayer;
    private Player player;

    private DList<Player> players;

    public Compass(Player player) {
        this.player = player;
        Collection<Player> alive = PlayerUtil.getAlivePlayers();
        alive.remove(player);
        players = new DList<>(alive);
        setNewPlayer();
    }

    public Player getCurrentPlayer() {
        if (!PlayerUtil.isAlive(currentPlayer)) {
            setNewPlayer();
        }
        return currentPlayer;
    }

    public Player getPlayer() {
        return player;
    }

    public void setNewPlayer() {
        if (players.isEmpty()) {
            currentPlayer = null;
        } else {
            for (int i = 0; i < players.size(); i++) {
                Player player = players.getNext();
                if (PlayerUtil.isAlive(player)) {
                    currentPlayer = player;
                    return;
                }
            }
            currentPlayer = null;
        }
    }
}
