package plusm.vilonix.gapi.guis;

import gnu.trove.map.TIntObjectMap;
import gnu.trove.map.hash.TIntObjectHashMap;
import lombok.experimental.UtilityClass;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import plusm.vilonix.api.util.Head;
import plusm.vilonix.api.util.ItemUtil;

@UtilityClass
public class CustomItems {

    private static final TIntObjectMap<ItemStack> BACK_ITEMS = new TIntObjectHashMap<>();
    private static final TIntObjectMap<ItemStack> BACK_ITEMS2 = new TIntObjectHashMap<>();
    private static final TIntObjectMap<ItemStack> DISABLE_ITEM = new TIntObjectHashMap<>();
    private static final TIntObjectMap<ItemStack> ENABLE_ITEM = new TIntObjectHashMap<>();

    static {
        init();
    }


    public static ItemStack getBack() {
        ItemStack itemStack = BACK_ITEMS.get(0);
        if (itemStack != null)
            return itemStack.clone();

        return BACK_ITEMS.get(0).clone();
    }

    public static ItemStack getBack2() {
        ItemStack itemStack = BACK_ITEMS2.get(0);
        if (itemStack != null)
            return itemStack.clone();

        return BACK_ITEMS2.get(0).clone();
    }

    public static ItemStack getDisable() {
        ItemStack itemStack = DISABLE_ITEM.get(0);
        if (itemStack != null)
            return itemStack.clone();

        return DISABLE_ITEM.get(0).clone();
    }

    public static ItemStack getEnable() {
        ItemStack itemStack = ENABLE_ITEM.get(0);
        if (itemStack != null)
            return itemStack.clone();

        return ENABLE_ITEM.get(0).clone();
    }

    private static void init() {
        BACK_ITEMS.put(0, ItemUtil.getBuilder(Head.BACK).setName("§cНазад")
                .setLore("§7Щелкните, чтобы вернуться в главное меню").build());

        BACK_ITEMS2.put(0, ItemUtil.getBuilder(Head.BACK).setName("§cНазад")
                .setLore("§7Щелкните, чтобы вернуться на шаг назад").build());

        ENABLE_ITEM.put(0, ItemUtil.getBuilder(Material.INK_SACK).setDurability((short) 10)
                .setName("§aВключено")
                .setLore("§7Щелкните, чтобы отключить").build());

        DISABLE_ITEM.put(0, ItemUtil.getBuilder(Material.INK_SACK).setDurability((short) 8)
                .setName("§cОтключено")
                .setLore("§7Щелкните, чтобы включить").build());
    }
}
