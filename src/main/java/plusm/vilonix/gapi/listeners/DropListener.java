package plusm.vilonix.gapi.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerDropItemEvent;
import plusm.vilonix.gapi.utils.DListener;

public class DropListener extends DListener {

    @EventHandler
    public void onDrop(PlayerDropItemEvent e) {
        e.setCancelled(true);
    }

}
