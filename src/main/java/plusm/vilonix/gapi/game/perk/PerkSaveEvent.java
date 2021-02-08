package plusm.vilonix.gapi.game.perk;

import org.bukkit.entity.Player;
import plusm.vilonix.api.event.DEvent;

public class PerkSaveEvent extends DEvent {
    private final Perk perk;
    private final Player player;

    public PerkSaveEvent(Player player, Perk perk) {
        this.player = player;
        this.perk = perk;
    }

    public Perk getPerk() {
        return perk;
    }

    public Player getPlayer() {
        return player;
    }
}
