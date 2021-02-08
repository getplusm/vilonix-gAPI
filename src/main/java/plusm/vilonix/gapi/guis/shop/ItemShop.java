package plusm.vilonix.gapi.guis.shop;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public interface ItemShop {

    boolean canBuy(Player player);

    void choose(Player player);

    ItemShop getByIcon(ItemStack icon);

    ItemStack getIcon();

    void giveToPlayer(Player player, boolean buy);

    boolean have(Player Player);
}
