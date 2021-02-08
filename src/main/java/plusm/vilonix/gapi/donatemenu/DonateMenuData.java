package plusm.vilonix.gapi.donatemenu;

import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerQuitEvent;
import plusm.vilonix.api.VilonixNetwork;
import plusm.vilonix.api.player.GamerManager;
import plusm.vilonix.gapi.donatemenu.guis.DonateMenuGui;
import plusm.vilonix.gapi.donatemenu.guis.FastMessageGui;
import plusm.vilonix.gapi.donatemenu.guis.JoinMessageGui;
import plusm.vilonix.gapi.donatemenu.guis.MainDonateMenuGui;
import plusm.vilonix.gapi.utils.DListener;

import java.util.HashMap;
import java.util.Map;

@Getter
public final class DonateMenuData extends DListener {

    private static final GamerManager GAMER_MANAGER = VilonixNetwork.getGamerManager();

    public final Map<String, DonateMenuGui> data = new HashMap<>();

    DonateMenuData(Player player) {
        data.remove(player.getName().toLowerCase());
        register(MainDonateMenuGui.class, player);
        register(FastMessageGui.class, player);
        register(JoinMessageGui.class, player);
    }

    @SuppressWarnings("unchecked")
    public <T extends DonateMenuGui> T get(Class<T> clazz) {
        return (T) data.get(clazz.getSimpleName().toLowerCase());
    }

    private void register(Class<? extends DonateMenuGui> clazz, Player player) {
        try {
            DonateMenuGui gui = clazz.getConstructor(Player.class, DonateMenuData.class)
                    .newInstance(player, this);
            this.data.put(clazz.getSimpleName().toLowerCase(), gui);
        } catch (Exception ignored) {
        }
    }

    public void updateClass(DonateMenuGui donateMenuGui, Player player) {
        this.data.remove(donateMenuGui.getClass().getSimpleName().toLowerCase());
        register(donateMenuGui.getClass(), player);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        data.remove(player.getName().toLowerCase());
    }
}
