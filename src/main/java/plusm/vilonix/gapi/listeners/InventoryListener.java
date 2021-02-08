package plusm.vilonix.gapi.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import plusm.vilonix.api.game.GameState;
import plusm.vilonix.gapi.utils.DListener;
import plusm.vilonix.gapi.utils.core.PlayerUtil;

public class InventoryListener extends DListener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        Player player = (Player) e.getWhoClicked();
        if (PlayerUtil.isSpectator(player) || GameState.getCurrent() != GameState.GAME) {
            if (e.getInventory().getType() == InventoryType.CRAFTING) {
                if (e.getClick() == ClickType.NUMBER_KEY) {
                    e.setCancelled(true);
                }
            }
        }
    }

}
