package plusm.vilonix.gapi.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import plusm.vilonix.gapi.utils.DListener;

public class FoodListener extends DListener {

    @EventHandler
    public void onFoodChange(FoodLevelChangeEvent e) {
        e.setCancelled(true);
    }

}
