package plusm.vilonix.gapi.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import plusm.vilonix.api.game.GameSettings;
import plusm.vilonix.api.game.MiniGameType;
import plusm.vilonix.gapi.utils.DListener;

public class EntitySpawnListener extends DListener {

    @EventHandler
    public void onCreatureSpawn(CreatureSpawnEvent e) {
        if (GameSettings.minigame != MiniGameType.DEFAULT)
            e.setCancelled(true);
    }

    @EventHandler
    public void onEntitySpawn(EntitySpawnEvent e) {
        if (GameSettings.minigame != MiniGameType.DEFAULT)
            e.setCancelled(true);
    }
}
