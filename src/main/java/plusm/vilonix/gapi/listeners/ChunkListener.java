package plusm.vilonix.gapi.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.world.ChunkUnloadEvent;
import plusm.vilonix.api.game.GameSettings;
import plusm.vilonix.api.game.MiniGameType;
import plusm.vilonix.gapi.utils.DListener;

public class ChunkListener extends DListener {

    @EventHandler
    public void onChunkUnload(ChunkUnloadEvent e) {
        if (GameSettings.minigame == MiniGameType.SURVIVAL)
            return;
        e.setCancelled(true);
    }

}
