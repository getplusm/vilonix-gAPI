package plusm.vilonix.gapi.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import plusm.vilonix.api.game.GameState;
import plusm.vilonix.gapi.utils.DListener;
import plusm.vilonix.libraries.LibAPI;
import plusm.vilonix.libraries.interfaces.NmsManager;

public class DamageListener extends DListener {

    private final NmsManager nmsManager = LibAPI.getManager();

    @EventHandler
    public void onEntityDamage(EntityDamageEvent e) {
        e.setCancelled(true);
        if (e.getEntity() instanceof Player) {
            Player player = (Player) e.getEntity();
            if (GameState.getCurrent() == GameState.GAME)
                return;
            if (e.getCause() == EntityDamageEvent.DamageCause.FIRE
                    || e.getCause() == EntityDamageEvent.DamageCause.FIRE_TICK) {
                nmsManager.disableFire(player);
            }
        }
    }
}
