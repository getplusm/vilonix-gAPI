package plusm.vilonix.gapi.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import plusm.vilonix.gapi.utils.DListener;

public class BlockBreakListener extends DListener {

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        e.setCancelled(true);
    }

}
