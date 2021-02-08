package plusm.vilonix.gapi.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import plusm.vilonix.gapi.utils.DListener;

public class PlayerInteractListener extends DListener {

    @EventHandler
    public void onArmorInterract(PlayerArmorStandManipulateEvent e) {
        e.setCancelled(true);
    }

    @EventHandler
    public void onPlayerFrame(PlayerInteractEntityEvent e) {
        e.setCancelled(true);
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {
        e.setCancelled(true);
    }

}
