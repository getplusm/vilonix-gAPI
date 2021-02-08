package plusm.vilonix.gapi.utils;

import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

@UtilityClass
public class AntiCheatUtils {

    public static void unloadAndLoad() {
        unloadAndLoad("Matrix");
        unloadAndLoad("NoCheatPlus");
    }

    private static void unloadAndLoad(String pluginName) {
        Plugin plugin = Bukkit.getPluginManager().getPlugin(pluginName);
        if (plugin != null) {
            Bukkit.getPluginManager().disablePlugin(plugin);
            Bukkit.getPluginManager().enablePlugin(plugin);
        }
    }
}
