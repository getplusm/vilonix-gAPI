package plusm.vilonix.gapi.guis.playerinventory;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import plusm.vilonix.gapi.utils.DListener;
import plusm.vilonix.gapi.utils.core.PlayerUtil;

public class PlayerInventoryListener extends DListener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        for (Inventory inventory : PlayerInventory.getInventorys()) {
            if (e.getInventory().getName().equals(inventory.getName())) {
                e.setCancelled(true);
                break;
            }
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        if (PlayerUtil.isAlive(player))
            PlayerInventory.removePlayerInventory(player);

    }

}
