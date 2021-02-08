package plusm.vilonix.gapi.game.perk.type.bowbreakblock;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import plusm.vilonix.api.util.ItemUtil;
import plusm.vilonix.api.util.Rarity;
import plusm.vilonix.gapi.game.perk.Perk;

public class BowBreakBlock extends Perk {

    private final String name = "Лук-Разрушитель";

    public BowBreakBlock() {
        super();
    }

    public BowBreakBlock(Rarity rarity, int cost) {
        super(rarity, cost);
    }

    @Override
    public boolean canAfford(Player player) {
        return true;
    }

    @Override
    public String getErrorMessage() {
        return "Обратитесь к администрации проекта";
    }

    @Override
    public ItemStack getItem(Player player) {
        return ItemUtil.getBuilder(Material.BOW).setName("§e" + name).setLore("§7Уничтожение блоков при выстреле", "§7по ним с любого лука").build();
    }

    @Override
    public ItemStack getItemPublic() {
        return new ItemStack(Material.BOW);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean has(Player player) {
        return true;
    }

    @Override
    public void onUse(Player player) {
        new ArrowBlockBreakListener(player);
    }
}
