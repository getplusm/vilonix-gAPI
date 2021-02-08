package plusm.vilonix.gapi.guis.basic;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import plusm.vilonix.api.VilonixNetwork;
import plusm.vilonix.api.inventory.DItem;
import plusm.vilonix.api.inventory.action.InventoryAction;
import plusm.vilonix.api.sound.SoundType;
import plusm.vilonix.api.util.ConfigUtil;
import plusm.vilonix.api.util.Head;
import plusm.vilonix.api.util.ItemUtil;
import plusm.vilonix.gapi.guis.CustomItems;
import plusm.vilonix.gapi.guis.GuiDefaultContainer;
import plusm.vilonix.gapi.guis.basic.donate.*;

public class DonateGui extends Gui {

    public DonateGui(Player p, GuiDefaultContainer listener) {
        super(p, listener, "Ïðèâèëåãèè");
        dInventory.createInventoryAction(new InventoryAction() {
            @Override
            public void onOpen(Player player) {
                if (VilonixNetwork.isGame() || VilonixNetwork.isMisc()) {
                    return;
                }
                dInventory.setItem(40, new DItem(CustomItems.getBack2(), (player1, clickType, slot) -> {
                    SOUND_API.play(player, SoundType.PICKUP);
                    player.chat("/profile");
                }));
            }
        });
    }

    @Override
    protected void setItems() {
        dInventory.setItem(20,
                new DItem(
                        ItemUtil.getBuilder(Head.FELIKS_DONATE.getHead())
                                .setName("§f§lFELIKS §f" + player.getName())
                                .setLore("", "§7Ñòîèìîñòü: §6" + 1025 + " Ðóáëåé", "", "§e\u25b8 Îòêðûòü ìåíþ âîçìîæíîñòåé").build(),
                        (clicker, clickType, slot) -> listener.getGui(FeliksGUI.class)));

        dInventory.setItem(22,
                new DItem(
                        ItemUtil.getBuilder(Head.DEMON_DONATE.getHead())
                                .setName("§c§lDEMON §f" + player.getName())
                                .setLore("", "§7Ñòîèìîñòü: §6" + 465 + " Ðóáëåé", "", "§e\u25b8 Îòêðûòü ìåíþ âîçìîæíîñòåé").build(),
                        (clicker, clickType, slot) -> listener.getGui(DemonGUI.class)));

        dInventory.setItem(24,
                new DItem(
                        ItemUtil.getBuilder(Head.ANGEL_DONATE.getHead())
                                .setName("§b§lANGEL §f" + player.getName())
                                .setLore("", "§7Ñòîèìîñòü: §6" + 275 + " Ðóáëåé", "", "§e\u25b8 Îòêðûòü ìåíþ âîçìîæíîñòåé").build(),
                        (clicker, clickType, slot) -> listener.getGui(AngelGUI.class)));

        dInventory.setItem(29,
                new DItem(
                        ItemUtil.getBuilder(Head.ALEGRO_DONATE.getHead())
                                .setName("§e§lALEGRO §f" + player.getName())
                                .setLore("", "§7Ñòîèìîñòü: §6" + 135 + " Ðóáëåé", "", "§e\u25b8 Îòêðûòü ìåíþ âîçìîæíîñòåé").build(),
                        (clicker, clickType, slot) -> listener.getGui(AlegroGUI.class)));

        dInventory.setItem(31,
                new DItem(
                        ItemUtil.getBuilder(Head.LITE_DONATE.getHead())
                                .setName("§3§lLITE §f" + player.getName())
                                .setLore("", "§7Ñòîèìîñòü: §6" + 65 + " Ðóáëåé", "", "§e\u25b8 Îòêðûòü ìåíþ âîçìîæíîñòåé").build(),
                        (clicker, clickType, slot) -> listener.getGui(LiteGUI.class)));
        dInventory.setItem(33,
                new DItem(
                        ItemUtil.getBuilder(Head.PRIMO_DONATE.getHead())
                                .setName("§7§lPRIMO §f" + player.getName())
                                .setLore("", "§7Ñòîèìîñòü: §6" + 15 + " Ðóáëåé", "", "§e\u25b8 Îòêðûòü ìåíþ âîçìîæíîñòåé").build(),
                        (clicker, clickType, slot) -> listener.getGui(PrimoGUI.class)));
        dInventory.setItem(9 * 3 + 5 - 1,
                new DItem(ItemUtil.getBuilder(Material.BOOK).setName(ConfigUtil.getMessage("GUI_DONATE_ITEM_3_NAME"))
                        .setLore(ConfigUtil.getList("GUI_DONATE_ITEM_3_LORE")).build()));
    }
}