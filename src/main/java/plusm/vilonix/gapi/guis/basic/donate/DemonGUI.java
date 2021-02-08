package plusm.vilonix.gapi.guis.basic.donate;

import org.bukkit.entity.Player;
import plusm.vilonix.api.inventory.DItem;
import plusm.vilonix.api.sound.SoundType;
import plusm.vilonix.api.util.ConfigUtil;
import plusm.vilonix.api.util.Head;
import plusm.vilonix.api.util.ItemUtil;
import plusm.vilonix.gapi.guis.CustomItems;
import plusm.vilonix.gapi.guis.GuiDefaultContainer;
import plusm.vilonix.gapi.guis.basic.DonateGui;
import plusm.vilonix.gapi.guis.basic.Gui;

public class DemonGUI extends Gui {

    public DemonGUI(Player p, GuiDefaultContainer container) {
        super(p, container, "Возможности §c§lDEMON");
    }

    @Override
    protected void setItems() {
        dInventory.setItem(40, new DItem(CustomItems.getBack2(), (player, clickType, slot) -> {
            SOUND_API.play(player, SoundType.PICKUP);
            listener.getGui(DonateGui.class);
        }));

        dInventory.setItem(4, 3,
                new DItem(ItemUtil.getBuilder(Head.HOUSE).setName(ConfigUtil.getMessage("GUI_DONATE_GUI_ITEM_NAME"))
                        .setLore(ConfigUtil.getList("GUI_DONATE_DEMON_ITEM_LORE")).build()));
        dInventory.setItem(6, 3, new DItem(ItemUtil.getBuilder(Head.FIRE).setName("§bGRIEF")
                .setLore(ConfigUtil.getList("GUI_DONATE_DEMON_GRIEF_LORE")).build()));
    }
}