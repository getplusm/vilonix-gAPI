package plusm.vilonix.gapi.donatemenu.event;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.event.Cancellable;
import plusm.vilonix.api.event.gamer.GamerEvent;
import plusm.vilonix.api.player.BukkitGamer;
import plusm.vilonix.gapi.donatemenu.FastMessage;

import java.util.Set;

@Getter
@Setter
public class AsyncGamerSendFastMessageEvent extends GamerEvent implements Cancellable {

    private final Set<BukkitGamer> recipients;
    private boolean cancelled;
    private FastMessage fastMessage;

    public AsyncGamerSendFastMessageEvent(BukkitGamer gamer, FastMessage fastMessage, Set<BukkitGamer> gamers) {
        super(gamer, true);
        this.setFastMessage(fastMessage);
        this.recipients = gamers;
    }

    public FastMessage getFastMessage() {
        return fastMessage;
    }

    public void setFastMessage(FastMessage fastMessage) {
        this.fastMessage = fastMessage;
    }

    public Set<BukkitGamer> getRecipients() {
        return recipients;
    }

    @Override
    public boolean isCancelled() {
        // TODO Auto-generated method stub
        return cancelled;
    }

    @Override
    public void setCancelled(boolean arg0) {
        this.cancelled = arg0;

    }
}
