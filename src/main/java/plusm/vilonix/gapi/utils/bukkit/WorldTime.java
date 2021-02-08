package plusm.vilonix.gapi.utils.bukkit;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitRunnable;
import plusm.vilonix.gapi.loader.DartaAPI;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Getter
public class WorldTime extends BukkitRunnable {

    private static final Map<String, Integer> WORLD = new ConcurrentHashMap<>();

    public WorldTime() {
        this.runTaskTimer(DartaAPI.getInstance(), 0, 200);
    }

    public static void addWorld(String worldName, int worldTime) {
        WORLD.put(worldName.toLowerCase(), worldTime);
    }

    public static void removeWorld(String worldName) {
        WORLD.remove(worldName.toLowerCase());
    }

    @Override
    public void run() {
        for (Map.Entry<String, Integer> pair : WORLD.entrySet()) {
            World world = Bukkit.getWorld(pair.getKey());
            if (world == null)
                continue;

            world.setTime(pair.getValue());
        }
    }
}
