package plusm.vilonix.gapi.listeners;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.ItemSpawnEvent;
import plusm.vilonix.api.game.GameSettings;
import plusm.vilonix.api.game.MiniGameType;
import plusm.vilonix.gapi.utils.DListener;

public class ItemSpawnListener extends DListener {

    @EventHandler
    public void onItemSpawn(ItemSpawnEvent e) {
        if (GameSettings.minigame != MiniGameType.DEFAULT)
            e.setCancelled(true);
        else if (e.getEntity().getItemStack().getType() == Material.WEB)
            e.setCancelled(true);
    }
}
