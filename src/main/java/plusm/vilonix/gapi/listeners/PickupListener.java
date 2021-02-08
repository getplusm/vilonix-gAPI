package plusm.vilonix.gapi.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerPickupItemEvent;
import plusm.vilonix.gapi.utils.DListener;

@Deprecated
public class PickupListener extends DListener {

    @EventHandler
    public void onPickup(PlayerPickupItemEvent e) {
        e.setCancelled(true);
    }

}
