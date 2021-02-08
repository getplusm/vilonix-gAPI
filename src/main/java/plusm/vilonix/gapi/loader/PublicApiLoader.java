package plusm.vilonix.gapi.loader;

import org.bukkit.Bukkit;
import plusm.vilonix.libraries.*;
import plusm.vilonix.libraries.command.CommandsAPIImpl;
import plusm.vilonix.libraries.effect.ParticleAPIImpl;
import plusm.vilonix.libraries.entity.EntityAPIImpl;
import plusm.vilonix.libraries.entity.ServerGamerManager;
import plusm.vilonix.libraries.hologram.HologramAPIImpl;
import plusm.vilonix.libraries.inventory.InventoryAPIImpl;
import plusm.vilonix.libraries.scoreboard.ScoreBoardAPIImpl;
import plusm.vilonix.libraries.usableItem.UsableAPIImpl;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

final class PublicApiLoader {

    static void init(DartaAPI javaplugin) {
        Object api = null;
        try {
            Class<?> apiClass = Class.forName("plusm.vilonix.api.VilonixNetwork");
            Constructor<?> constructor = apiClass.getConstructor();
            api = constructor.newInstance();
            javaplugin.getServer().getConsoleSender().sendMessage("§amGAPI äîáàâèë â ðååñòð §9" + api.getClass().getSimpleName().toUpperCase());
        } catch (Exception e) {
            e.printStackTrace();
            javaplugin.getServer().getConsoleSender().sendMessage("§cmAPI not found, shutting down");
            Bukkit.getServer().shutdown();
            Bukkit.setWhitelist(true);

        }
        if (api == null) {
            return;
        }

        try {
            setStaticField(api, "jsonMessageAPI", new JSONMessageAPIImpl());
            setStaticField(api, "gamerManagerAPI", new ServerGamerManager(javaplugin));
            setStaticField(api, "soundAPI", new SoundAPIImpl());
            setStaticField(api, "usableAPI", new UsableAPIImpl(javaplugin));
            setStaticField(api, "entityAPI", new EntityAPIImpl(javaplugin));
            setStaticField(api, "hologramAPI", new HologramAPIImpl(javaplugin));
            setStaticField(api, "scoreBoardAPI", new ScoreBoardAPIImpl(javaplugin));
            setStaticField(api, "commandsAPI", new CommandsAPIImpl());
            setStaticField(api, "inventoryAPI", new InventoryAPIImpl(javaplugin));
            setStaticField(api, "titleAPI", new TitleAPIImpl());
            setStaticField(api, "borderAPI", new BorderAPIImpl());
            setStaticField(api, "actionBarAPI", new ActionBarAPIImpl());
            setStaticField(api, "particleAPI", new ParticleAPIImpl(javaplugin));
            setStaticField(api, "coreAPI", new CoreAPIImpl(javaplugin));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void setStaticField(Object instance, String fieldName, Object value) throws Exception {
        Class<?> clazz = instance.getClass();
        Field f = clazz.getDeclaredField(fieldName);
        f.setAccessible(true);
        f.set(null, value);
        f.setAccessible(false);
    }
}
