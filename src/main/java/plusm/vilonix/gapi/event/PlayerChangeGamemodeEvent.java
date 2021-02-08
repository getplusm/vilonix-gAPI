package plusm.vilonix.gapi.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.entity.Player;
import plusm.vilonix.api.event.DEvent;
import plusm.vilonix.api.game.GameModeType;

@AllArgsConstructor
@Getter
public class PlayerChangeGamemodeEvent extends DEvent {

    private final GameModeType gameModeType;
    private final Player player;

    public PlayerChangeGamemodeEvent(final Player player, final GameModeType gameModeType) {
        this.player = player;
        this.gameModeType = gameModeType;
    }

    public GameModeType getGameModeType() {
        return this.gameModeType;
    }

    public Player getPlayer() {
        return this.player;
    }
}
