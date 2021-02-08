package plusm.vilonix.gapi.guis.shop;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import plusm.vilonix.api.game.GameState;
import plusm.vilonix.api.inventory.type.DInventory;
import plusm.vilonix.gapi.loader.DartaAPI;
import plusm.vilonix.gapi.utils.DListener;

public class ShopListener extends DListener {

    ShopListener() {
        Bukkit.getServer().getPluginManager().registerEvents(this, DartaAPI.getInstance());
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        if (GameState.getCurrent() == GameState.GAME)
            return;
        if (ShopInventory.shopPlayers.containsKey(e.getPlayer().getName())) {
            if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
                ItemStack item = e.getPlayer().getInventory().getItemInMainHand();
                if (item.getType() == ShopInventory.shopPlayers.get(e.getPlayer().getName()).getTrigger()) {
                    ShopInventory shopPlayer = ShopInventory.shopPlayers.get(e.getPlayer().getName());
                    if (shopPlayer.shopPages.size() == 0)
                        shopPlayer.fillShopPages(0);
                    e.setCancelled(true);
                    DInventory dInventory = shopPlayer.shopPages.get(0);
                    if (dInventory != null) {
                        dInventory.openInventory(e.getPlayer());
                    }
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onQuit(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        ShopInventory.shopPlayers.remove(player.getName());
    }

}
