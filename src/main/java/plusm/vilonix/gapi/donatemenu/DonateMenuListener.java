package plusm.vilonix.gapi.donatemenu;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerQuitEvent;
import plusm.vilonix.gapi.donatemenu.commands.DonateMenuCommand;
import plusm.vilonix.gapi.donatemenu.commands.FastMessageCommand;
import plusm.vilonix.gapi.donatemenu.commands.PrefixCommand;
import plusm.vilonix.gapi.donatemenu.event.AsyncGamerSendFastMessageEvent;
import plusm.vilonix.gapi.donatemenu.guis.DonateMenuGui;
import plusm.vilonix.api.player.BukkitGamer;
import plusm.vilonix.api.player.GamerEntity;
import plusm.vilonix.api.util.ConfigUtil;
import plusm.vilonix.gapi.listeners.DListener;
import plusm.vilonix.gapi.loader.DartaAPI;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class DonateMenuListener extends DListener<DartaAPI> {

    private final Map<String, DonateMenuData> data = new ConcurrentHashMap<>();

    public DonateMenuListener(DartaAPI dartaAPI) {
        super(dartaAPI);

        new DonateMenuCommand(this);
        //new PrefixCommand(this);
        new FastMessageCommand(this);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        data.remove(player.getName().toLowerCase());
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onSendFM(AsyncGamerSendFastMessageEvent e) {
        BukkitGamer gamer = e.getGamer();

        FastMessage fastMessage = e.getFastMessage();
        e.getRecipients().forEach(otherGamer -> sendFastMessage(gamer, otherGamer, fastMessage));
        sendFastMessage(gamer, GAMER_MANAGER.getSpigot(), fastMessage);
    }

    public void open(Player player, Class<? extends DonateMenuGui> clazz) {
        String name = player.getName().toLowerCase();
        DonateMenuData data = this.data.get(name);
        if (data == null) {
            data = new DonateMenuData(player);
            this.data.put(name, data);
        }

        DonateMenuGui gui = data.get(clazz);
        if (gui == null) {
            return;
        }

        gui.open();
    }

    private void sendFastMessage(BukkitGamer gamer, GamerEntity gamerEntity, FastMessage fastMessage) {
        gamerEntity.sendMessage("§7(/fm)§r " + gamer.getDisplayName() + " §8\u00bb§b "
                + ConfigUtil.getMessage(fastMessage.getKey()) + " " + fastMessage.getSmile());
    }
}
