package plusm.vilonix.gapi.game.perk.type.health;

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

public class Health extends Perk {

    private final String name = "Здоровье";

    public Health() {
        super();
    }

    public Health(Rarity rarity, int cost) {
        super(rarity, cost);
    }

    @Override
    public boolean canAfford(Player player) {
        return Objects.requireNonNull(GAMER_MANAGER.getGamer(player)).isLite();
    }

    @Override
    public String getErrorMessage() {
        return Perk.getPrefix() + "§cУ вас нет прав на это умение, купите §r" + Objects.requireNonNull(mAPI.getLuckPerms().getGroupManager().getGroup("LITE")).getDisplayName()
                + "§cили выше";
    }

    public ItemStack getItem(Player player) {
        int level = Objects.requireNonNull(GAMER_MANAGER.getGamer(player)).getGroup().getWeight().getAsInt();
        return new ItemStack(ItemUtil.createItemStack(Material.POTION, (byte) 373, "§e" + name,
                Arrays.asList("§7Увеличивает максимальное", "§7здоровье игрока на:",
                        " §8▪§7 1 " + (level == 2 ? "§a" : "§c") + "❤ §7для §f[§LITE§f]",
                        " §8▪§7 1.5 " + (level == 3 ? "§a" : "§c") + "❤ §7для §f[§cANGEL§f]",
                        " §8▪§7 2 " + (level >= 4 ? "§a" : "§c") + "❤ §7для §f[§bDEMON§f] §7и выше")));
    }

    @Override
    public ItemStack getItemPublic() {
        return new ItemStack(Material.POTION, 1, (byte) 373);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean has(Player player) {
        return canAfford(player);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onUse(Player player) {
        int level = Objects.requireNonNull(GAMER_MANAGER.getGamer(player)).getGroup().getWeight().getAsInt();
        if (level > 4)
            level = 4;
        player.setMaxHealth(20 + level);
    }
}
