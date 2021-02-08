package plusm.vilonix.gapi.utils.bukkit;

import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import plusm.vilonix.gapi.loader.DartaAPI;

@UtilityClass
public class BukkitUtil {

    public static void callEvent(Event event) {
        Bukkit.getPluginManager().callEvent(event);
    }

    public static void runTask(Runnable runnable) {
        Bukkit.getScheduler().runTask(DartaAPI.getInstance(), runnable);
    }

    public static void runTaskAsync(Runnable runnable) {
        Bukkit.getScheduler().runTaskAsynchronously(DartaAPI.getInstance(), runnable);
    }

    public static void runTaskLater(long delay, Runnable runnable) {
        Bukkit.getScheduler().runTaskLater(DartaAPI.getInstance(), runnable, delay);
    }

    public static void runTaskLaterAsync(long delay, Runnable runnable) {
        Bukkit.getScheduler().runTaskLaterAsynchronously(DartaAPI.getInstance(), runnable, delay);
    }

    public static void runTaskTimerAsync(long timer, long timer2, Runnable runnable) {
        Bukkit.getScheduler().runTaskTimerAsynchronously(DartaAPI.getInstance(), runnable, timer, timer2);
    }
}