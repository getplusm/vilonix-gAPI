package plusm.vilonix.gapi.donatemenu.guis;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import plusm.vilonix.api.VilonixNetwork;
import plusm.vilonix.api.inventory.DItem;
import plusm.vilonix.api.inventory.InventoryAPI;
import plusm.vilonix.api.inventory.type.MultiInventory;
import plusm.vilonix.api.player.BukkitGamer;
import plusm.vilonix.api.player.GamerManager;
import plusm.vilonix.api.sound.SoundAPI;
import plusm.vilonix.api.sound.SoundType;
import plusm.vilonix.api.util.ItemUtil;
import plusm.vilonix.gapi.donatemenu.DonateMenuData;
import plusm.vilonix.gapi.guis.CustomItems;

public abstract class DonateMenuGui {

    protected static final GamerManager GAMER_MANAGER = VilonixNetwork.getGamerManager();
    protected static final InventoryAPI INVENTORY_API = VilonixNetwork.getInventoryAPI();
    protected static final ItemStack NO_PERMS = ItemUtil.getBuilder(Material.STAINED_GLASS_PANE)
            .setDurability((short) 14).build();

    protected static final SoundAPI SOUND_API = VilonixNetwork.getSoundAPI();

    protected final DonateMenuData donateMenuData;
    protected final MultiInventory inventory;
    protected final Player player;

    DonateMenuGui(Player player, DonateMenuData donateMenuData, String name) {
        this.player = player;
        this.donateMenuData = donateMenuData;
        this.inventory = INVENTORY_API.createMultiInventory(player, name, 5);
    }

    DonateMenuGui(Player player, DonateMenuData donateMenuData, String name, int size) {
        this.player = player;
        this.donateMenuData = donateMenuData;
        this.inventory = INVENTORY_API.createMultiInventory(player, name, size);
    }

    public final void open() {
        if (inventory == null || player == null) {
            return;
        }

        BukkitGamer gamer = GAMER_MANAGER.getGamer(player);
        if (gamer == null) {
            return;
        }

        setItems(gamer);
        inventory.openInventory(player);
    }


    protected final void setBack(int slotType, DonateMenuGui gui) {
        BukkitGamer gamer = GAMER_MANAGER.getGamer(player);
        if (gamer == null)
            return;
        inventory.setItem(0, slotType, new DItem(CustomItems.getBack2(),
                (clicker, clickType, slot) -> {
                    if (gui == null) {
                        return;
                    }
                    SOUND_API.play(player, SoundType.PICKUP);
                    gui.open();
                }));
    }

    protected abstract void setItems(BukkitGamer gamer);
}
