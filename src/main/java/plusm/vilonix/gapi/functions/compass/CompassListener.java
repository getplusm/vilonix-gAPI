package plusm.vilonix.gapi.functions.compass;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import plusm.vilonix.gapi.game.ItemsListener;
import plusm.vilonix.gapi.utils.DListener;
import plusm.vilonix.gapi.utils.inventory.ItemUtil;

//todo удалить или переписать потом
public class CompassListener extends DListener {

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {
        if (e.getAction() != Action.RIGHT_CLICK_BLOCK && e.getAction() != Action.RIGHT_CLICK_AIR) {
            return;
        }

        Player player = e.getPlayer();
        ItemStack itemInHand = player.getInventory().getItemInMainHand();

        if (ItemUtil.compareItems(itemInHand, ItemsListener.getCompass())) {

            e.setCancelled(true);

            Compass compass = CompassManager.getCompass(player);
            if (compass != null) {
                compass.setNewPlayer();
            }
        }
    }

}
