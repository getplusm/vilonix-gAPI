package plusm.vilonix.gapi.guis.basic;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import plusm.vilonix.api.inventory.DItem;
import plusm.vilonix.api.sound.SoundType;
import plusm.vilonix.api.util.ConfigUtil;
import plusm.vilonix.api.util.ItemUtil;
import plusm.vilonix.gapi.guis.CustomItems;
import plusm.vilonix.gapi.guis.GuiDefaultContainer;

public class RewardHelpGui extends Gui {

    public RewardHelpGui(Player p, GuiDefaultContainer listener) {
        super(p, listener, "Награды (детальная информация)");
    }

    @Override
    protected void setItems() {
        dInventory.setItem(40, new DItem(CustomItems.getBack2(), (player, clickType, slot) -> {
            SOUND_API.play(player, SoundType.PICKUP);
            listener.getGui(HelpGui.class);
        }));

        // dInventory.setItem(9 + 3 - 1, new
        // DItem(ItemUtil.getBuilder(Head.BATMAN.getHead())
        // .setName("§bKitWars")
        // .setLore(lang.getList("GUI_REWARDS_ITEM_1_LORE"))
        // .build()));

        // dInventory.setItem(9 + 4 - 1, new
        // DItem(ItemUtil.getBuilder(Material.ENDER_PEARL)
        // .setName( "§bSkyWars")
        // .setLore(lang.getList("GUI_REWARDS_ITEM_2_LORE"))
        // .build()));
        // dInventory.setItem(9 + 5 - 1, new
        // DItem(ItemUtil.getBuilder(Head.LUCKYWARS.getHead())
        // .setName("§bLuckyWars")
        // .setLore(lang.getList( "GUI_REWARDS_ITEM_3_LORE"))
        // .build()));
        // dInventory.setItem(9 + 6 - 1, new DItem(ItemUtil.getBuilder(Material.BED)
        // .setName("§bBedWars")
        // .setLore(lang.getList( "GUI_REWARDS_ITEM_4_LORE"))
        // .build()));
        dInventory.setItem(9 + 5 - 1,
                new DItem(ItemUtil.getBuilder(Material.GLASS_BOTTLE)
                        .setName("§cДобавим в следующем обновлении")
                        .setLore("§cДанная функция отключена")
                        .build()));
        // dInventory.setItem(9 + 7 - 1, new
        // DItem(ItemUtil.getBuilder(Material.DRAGON_EGG)
        // .setName("§bEggWars")
        // .setLore(lang.getList("GUI_REWARDS_ITEM_5_LORE"))
        // .build()));

        dInventory.setItem(9 * 3 + 5 - 1,
                new DItem(ItemUtil.getBuilder(Material.BOOK).setName("§bДополнительная информация")
                        .setLore(ConfigUtil.getList("GUI_REWARDS_ITEM_INFO_LORE")).build()));
    }
}
