package plusm.vilonix.gapi.event;

import lombok.Getter;
import org.bukkit.event.Cancellable;
import plusm.vilonix.api.event.gamer.GamerEvent;
import plusm.vilonix.api.player.BukkitGamer;
import plusm.vilonix.gapi.module.achievement.achievement.Achievement;

@Getter
public final class GamerAchievementCompleteEvent extends GamerEvent implements Cancellable {
    private final Achievement achievement;
    @Getter
    private boolean cancelled;

    public GamerAchievementCompleteEvent(final BukkitGamer gamer, final Achievement achievement) {
        super(gamer);
        this.achievement = achievement;
    }

    public boolean isCancelled() {
        return this.cancelled;
    }

    public void setCancelled(final boolean arg0) {
        this.cancelled = arg0;
    }

    public Achievement getAchievement() {
        return this.achievement;
    }
}
