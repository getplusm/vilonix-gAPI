package plusm.vilonix.gapi.guis.basic;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import plusm.vilonix.api.VilonixNetwork;
import plusm.vilonix.api.inventory.DItem;
import plusm.vilonix.api.inventory.action.InventoryAction;
import plusm.vilonix.api.player.BukkitGamer;
import plusm.vilonix.api.sound.SoundType;
import plusm.vilonix.api.util.ConfigUtil;
import plusm.vilonix.api.util.Head;
import plusm.vilonix.api.util.ItemUtil;
import plusm.vilonix.gapi.guis.CustomItems;
import plusm.vilonix.gapi.guis.GuiDefaultContainer;

public class HelpGui extends Gui {

    public HelpGui(Player p, GuiDefaultContainer listener) {
        super(p, listener, "Информация");

        dInventory.createInventoryAction(new InventoryAction() {
            @Override
            public void onOpen(Player player) {
                if (VilonixNetwork.isGame() || VilonixNetwork.isMisc())
                    return;
                dInventory.setItem(40, new DItem(CustomItems.getBack2(), (player1, clickType, slot) -> {
                    SOUND_API.play(player, SoundType.PICKUP);
                    player.chat("/profile");
                }));
            }
        });
    }

    @Override
    protected void setItems() {
        dInventory.setItem(9 + 3 - 1,
                new DItem(ItemUtil.getBuilder(Head.GLOBUS).setName(ConfigUtil.getMessage("GUI_HELP_ITEM_1_NAME"))
                        .setLore(ConfigUtil.getList("GUI_HELP_ITEM_1_LORE")).build()));

        dInventory.setItem(9 + 4 - 1,
                new DItem(
                        ItemUtil.getBuilder(Head.EMERALD).setName(ConfigUtil.getMessage("GUI_HELP_ITEM_2_NAME"))
                                .setLore(ConfigUtil.getList("GUI_HELP_ITEM_2_LORE")).build(),
                        (player, clickType, slot) -> listener.getGui(DonateGui.class)));

        dInventory.setItem(9 + 5 - 1,
                new DItem(ItemUtil.getBuilder(Head.JAKE).setName(ConfigUtil.getMessage("GUI_HELP_ITEM_3_NAME"))
                        .setLore(ConfigUtil.getList("GUI_HELP_ITEM_3_LORE")).build()));

        dInventory.setItem(9 + 6 - 1,
                new DItem(ItemUtil.getBuilder(Material.BARRIER).setName(ConfigUtil.getMessage("GUI_HELP_ITEM_4_NAME"))
                        .setLore(ConfigUtil.getList("GUI_HELP_ITEM_4_LORE")).build()));

        dInventory.setItem(9 + 7 - 1,
                new DItem(ItemUtil.getBuilder(Material.ARMOR_STAND).setName(ConfigUtil.getMessage("GUI_HELP_ITEM_5_NAME"))
                        .setLore(ConfigUtil.getList("GUI_HELP_ITEM_5_LORE")).build()));

        dInventory.setItem(9 * 2 + 3 - 1,
                new DItem(ItemUtil.getBuilder(Material.BOOK_AND_QUILL).setName(ConfigUtil.getMessage("GUI_HELP_ITEM_6_NAME"))
                        .setLore(ConfigUtil.getList("GUI_HELP_ITEM_6_LORE")).build()));

        dInventory
                .setItem(9 * 2 + 4 - 1,
                        new DItem(
                                ItemUtil.getBuilder(Head.COMPUTER).setName(ConfigUtil.getMessage("GUI_HELP_ITEM_7_NAME"))
                                        .setLore(ConfigUtil.getList("GUI_HELP_ITEM_7_LORE")).build(),
                                (clicker, clickType, slot) -> {
                                    BukkitGamer gamer = GAMER_MANAGER.getGamer(clicker);
                                    if (gamer == null)
                                        return;
                                    clicker.closeInventory();
                                    gamer.sendMessage(ConfigUtil.getMessage("GUI_HELP_ITEM_7_LORE"));
                                }));

        dInventory.setItem(9 * 2 + 6 - 1,
                new DItem(ItemUtil.getBuilder(Head.PARTY).setName(ConfigUtil.getMessage("GUI_HELP_ITEM_9_NAME"))
                        .setLore(ConfigUtil.getList("GUI_HELP_ITEM_9_LORE")).build()));

        dInventory.setItem(9 * 2 + 7 - 1,
                new DItem(ItemUtil.getBuilder(Head.MONKEY).setName(ConfigUtil.getMessage("GUI_HELP_ITEM_10_NAME"))
                        .setLore(ConfigUtil.getList("GUI_HELP_ITEM_10_LORE")).build()));

        dInventory.setItem(9 * 2 + 5 - 1,
                new DItem(
                        ItemUtil.getBuilder(Head.SHOP).setName(ConfigUtil.getMessage("GUI_HELP_ITEM_8_NAME"))
                                .setLore(ConfigUtil.getList("GUI_HELP_ITEM_8_LORE")).build(),
                        (player, clickType, slot) -> listener.getGui(RewardHelpGui.class)));

    }
}
