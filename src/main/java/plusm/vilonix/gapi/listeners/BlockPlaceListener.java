package plusm.vilonix.gapi.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockPlaceEvent;
import plusm.vilonix.gapi.utils.DListener;

public class BlockPlaceListener extends DListener {

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e) {
        e.setCancelled(true);
    }

}
