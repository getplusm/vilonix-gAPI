package plusm.vilonix.gapi.game.perk;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import plusm.vilonix.api.VilonixNetwork;
import plusm.vilonix.api.player.GamerManager;
import plusm.vilonix.api.util.ItemUtil;
import plusm.vilonix.api.util.Rarity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public abstract class Perk {

    protected static final GamerManager GAMER_MANAGER = VilonixNetwork.getGamerManager();

    public static List<Perk> perks = new ArrayList<>();
    public static HashMap<String, Perk> perksNames = new HashMap<>();
    protected int id = 0, cost = 0;
    protected Rarity rarity = Rarity.COMMON;

    public Perk() {
        id = perks.size();
        perks.add(this);
        perksNames.put(getName(), this);
    }

    public Perk(Rarity rarity, int cost) {
        id = perks.size();
        perks.add(this);
        perksNames.put(getName(), this);
        this.rarity = rarity;
        this.cost = cost;
    }

    public static String getPrefix() {
        return "§6Умения §8| ";
    }

    public abstract boolean canAfford(Player player); // Может ли игрок взять данный перк

    public abstract String getErrorMessage();

    public int getId() {
        return id;
    }

    public abstract ItemStack getItem(Player player); // Иконка перка

    public abstract ItemStack getItemPublic(); // Иконка перка

    public abstract String getName(); // Название перка

    public int getPrice() {
        return cost;
    }

    public Rarity getRarity() {
        return rarity;
    }

    public ItemStack getWrongItem(Player player) {
        return ItemUtil.getBuilder(Material.STAINED_GLASS_PANE).setDurability((short) 14)
                .setName("§c" + getItem(player).getItemMeta().getDisplayName().substring(2))
                .setLore("", "§cУмение не доступно")
                .build();
    }

    public abstract boolean has(Player player);

    public abstract void onUse(Player player); // Как используется

}
