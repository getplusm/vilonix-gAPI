package plusm.vilonix.gapi.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockPhysicsEvent;
import plusm.vilonix.gapi.utils.DListener;

public class BlockPhysicsListener extends DListener {

    @EventHandler
    public void onPhysic(BlockPhysicsEvent e) {
        e.setCancelled(true);
    }

}
