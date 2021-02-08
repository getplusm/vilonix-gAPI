package plusm.vilonix.gapi.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import plusm.vilonix.gapi.utils.DListener;

public class FallListener extends DListener {

    @EventHandler
    public void onFallDamage(EntityDamageEvent e) {
        if (e.getCause() == EntityDamageEvent.DamageCause.FALL) {
            e.setCancelled(true);
        }
    }

}
