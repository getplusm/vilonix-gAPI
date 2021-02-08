package plusm.vilonix.gapi.guis.basic;

import org.bukkit.entity.Player;
import plusm.vilonix.api.VilonixNetwork;
import plusm.vilonix.api.inventory.InventoryAPI;
import plusm.vilonix.api.inventory.type.DInventory;
import plusm.vilonix.api.player.GamerManager;
import plusm.vilonix.api.sound.SoundAPI;
import plusm.vilonix.gapi.guis.GuiDefaultContainer;

public abstract class Gui {
    protected static final GamerManager GAMER_MANAGER = VilonixNetwork.getGamerManager();
    protected static final InventoryAPI INVENTORY_API = VilonixNetwork.getInventoryAPI();
    protected static final SoundAPI SOUND_API = VilonixNetwork.getSoundAPI();

    protected Player player;
    protected final DInventory dInventory;
    protected final GuiDefaultContainer listener;

    protected Gui(Player p, GuiDefaultContainer listener, String name) {
        this.player = p;
        this.listener = listener;
        dInventory = INVENTORY_API.createInventory(name, 5);
        this.setItems();
    }

    public DInventory getInventory() {
        return dInventory;
    }

    public void update() {
        if (dInventory == null)
            return;
        if (player == null || !player.isOnline())
            return;
        setItems();
    }

    protected abstract void setItems();

    public Player getPlayer() {
        return player;
    }

    public void open() {
        this.update();
        dInventory.openInventory(player);
    }
}
