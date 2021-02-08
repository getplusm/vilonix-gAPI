package plusm.vilonix.gapi.listeners;

import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import plusm.vilonix.api.VilonixNetwork;
import plusm.vilonix.api.player.GamerManager;

public abstract class DListener<T extends JavaPlugin> implements Listener {

    protected static final GamerManager GAMER_MANAGER = VilonixNetwork.getGamerManager();

    protected final T javaPlugin;

    protected DListener(T javaPlugin) {
        this.javaPlugin = javaPlugin;
        Bukkit.getPluginManager().registerEvents(this, javaPlugin);
    }

    public void unregisterListener() {
        HandlerList.unregisterAll(this);
    }
}
