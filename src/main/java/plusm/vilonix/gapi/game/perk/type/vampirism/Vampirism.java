package plusm.vilonix.gapi.game.perk.type.vampirism;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import plusm.vilonix.api.VilonixNetwork;
import plusm.vilonix.api.mAPI;
import plusm.vilonix.api.sound.SoundAPI;
import plusm.vilonix.api.sound.SoundType;
import plusm.vilonix.api.util.Rarity;
import plusm.vilonix.gapi.game.perk.Perk;
import plusm.vilonix.gapi.utils.inventory.ItemUtil;

import java.util.Arrays;
import java.util.Objects;

public class Vampirism extends Perk {

    private final String name = "Вампиризм";

    public Vampirism() {
        super();
    }

    public Vampirism(Rarity rarity, int cost) {
        super(rarity, cost);
    }

    @Override
    public boolean canAfford(Player player) {
        return GAMER_MANAGER.getGamer(player).isLite();
    }

    @Override
    public String getErrorMessage() {
        return (Perk.getPrefix() + "§cУ вас нет прав на это умение, купите §r" + Objects.requireNonNull(mAPI.getLuckPerms().getGroupManager().getGroup("LITE")).getDisplayName()
                + "§cили выше");
    }

    @Override
    public ItemStack getItem(Player player) {
        int level = GAMER_MANAGER.getGamer(player).getGroup().getWeight().getAsInt();
        return ItemUtil.createItemStack(Material.SKULL_ITEM, (byte) 1, "§e" + name,
                Arrays.asList("§7Восстанавливает ваше здоровье", "§7при убийстве другого игрока на:",
                        " §8▪§7 1 " + (level == 2 ? "§a" : "§c") + "❤ §7для §f[§bDiamond§f]",
                        " §8▪§7 2 " + (level == 3 ? "§a" : "§c") + "❤ §7для §f[§aEmerald§f]",
                        " §8▪§7 3 " + (level >= 4 ? "§a" : "§c") + "❤ §7для §f[§cMegalodon§f] §7и выше"));
    }

    @Override
    public ItemStack getItemPublic() {
        return new ItemStack(Material.SKULL, 1, (byte) 1);
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean has(Player player) {
        return canAfford(player);
    }

    @Override
    public void onUse(Player player) {
        byte count;
        switch (GAMER_MANAGER.getGamer(player).getGroup().getWeight().getAsInt()) {
            case 2:
                count = 2;
                break;
            case 3:
                count = 4;
                break;
            default:
                count = 6;
        }

        new VampListener(player, count);
    }
}