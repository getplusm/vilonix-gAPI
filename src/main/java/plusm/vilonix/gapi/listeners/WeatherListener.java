package plusm.vilonix.gapi.listeners;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.weather.WeatherChangeEvent;
import plusm.vilonix.gapi.utils.DListener;

public class WeatherListener extends DListener {

    public WeatherListener() {
        for (World world : Bukkit.getWorlds()) {
            world.setStorm(false);
            world.setThundering(false);
        }
    }

    @EventHandler
    public void onWeatherChange(WeatherChangeEvent e) {
        if (e.toWeatherState()) {
            e.setCancelled(true);
        }
    }

}
