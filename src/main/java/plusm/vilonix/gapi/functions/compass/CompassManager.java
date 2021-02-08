package plusm.vilonix.gapi.functions.compass;

import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class CompassManager {

    private static Map<String, Compass> compasses = new ConcurrentHashMap<String, Compass>();

    public static void createCompass(Player player) {
        compasses.put(player.getName(), new Compass(player));
    }

    public static Compass getCompass(Player player) {
        return compasses.get(player.getName());
    }

    public static Set<Compass> getCompasses() {
        return new HashSet<>(compasses.values());
    }

    public static void removeCompass(Player player) {
        compasses.remove(player.getName());
    }

}
