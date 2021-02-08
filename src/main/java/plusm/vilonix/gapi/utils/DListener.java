package plusm.vilonix.gapi.utils;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import plusm.vilonix.api.VilonixNetwork;
import plusm.vilonix.api.player.GamerManager;
import plusm.vilonix.gapi.loader.DartaAPI;
import plusm.vilonix.gapi.utils.core.MessageUtil;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Getter // todo мб удалить или переделать
public abstract class DListener implements Listener {

    protected static final GamerManager GAMER_MANAGER = VilonixNetwork.getGamerManager();
    public static Set<DListener> listeners = Collections.synchronizedSet(new HashSet<>());

    private final String name = getClass().getSimpleName();

    protected DListener() {
        Bukkit.getPluginManager().registerEvents(this, DartaAPI.getInstance());
        MessageUtil.sendConsole("§a" + this.name);
        listeners.add(this);
    }

    @Override
    public String toString() {
        return this.name;
    }

    public void unregisterListener() {
        HandlerList.unregisterAll(this);
        MessageUtil.sendConsole("§c" + this.name);
        listeners.remove(this);
    }
}
