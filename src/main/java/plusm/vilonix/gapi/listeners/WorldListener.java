package plusm.vilonix.gapi.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.world.WorldLoadEvent;
import plusm.vilonix.api.game.GameSettings;
import plusm.vilonix.api.game.MiniGameType;
import plusm.vilonix.gapi.utils.DListener;

public class WorldListener extends DListener {

    @EventHandler
    public void onWorldLoad(WorldLoadEvent e) {
        if (GameSettings.minigame != MiniGameType.DEFAULT)
            e.getWorld().setAutoSave(false);
    }

}
