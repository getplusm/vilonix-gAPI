package plusm.vilonix.gapi.donatemenu.guis;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import plusm.vilonix.api.inventory.DItem;
import plusm.vilonix.api.player.BukkitGamer;
import plusm.vilonix.api.sound.SoundType;
import plusm.vilonix.api.util.InventoryUtil;
import plusm.vilonix.api.util.ItemUtil;
import plusm.vilonix.gapi.donatemenu.DonateMenuData;
import plusm.vilonix.gapi.donatemenu.FastMessage;

import java.util.Map;

public final class FastMessageGui extends DonateMenuGui {

    public FastMessageGui(Player player, DonateMenuData donateMenuData) {
        super(player, donateMenuData, "Донатерские обн. > Быстрые сообщения");
    }

    @Override
    protected void setItems(BukkitGamer gamer) {
        setBack(40, donateMenuData.get(MainDonateMenuGui.class));

        boolean enable = gamer.isPrimo();

        int slot = 0;
        int page = 0;
        int value = 1;
        for (Map.Entry<String, FastMessage> entry : FastMessage.getMessages().entrySet()) {
            String name = entry.getKey();
            FastMessage fm = entry.getValue();
            inventory.setItem(page, slot++,
                    new DItem(
                            ItemUtil.getBuilder(enable ? new ItemStack(Material.PAINTING) : NO_PERMS.clone())
                                    .setName((enable ? "§b" : "§c") + value)
                                    .setLore("§7Сообщение: " + name + " " + fm.getSmile())
                                    .addLore("", enable ? "§e\u25b8 Щелкните, чтобы отправить сообщение"
                                            : "§cНедоступно", enable ? "" : "§cДоступно от " + fm.getGroup().getDisplayName() + " §cи выше")
                                    .build(),
                            (clicker, clickType, slot1) -> {
                                if (!enable) {
                                    SOUND_API.play(clicker, SoundType.NO);
                                    return;
                                }
                                clicker.chat("/fm " + name);
                                player.closeInventory();
                            }));

            if (slot == 18)
                ++slot;
            if (slot == 20)
                slot += 0;
            if (slot >= 26) {
                slot = 0;
                ++page;
            }
            value++;
        }
        INVENTORY_API.pageButton(InventoryUtil.getPagesCount(FastMessage.getMessages().size(), 21), inventory, InventoryUtil.getSlotByXY(1, 3), InventoryUtil.getSlotByXY(9, 3));
    }
}
