package plusm.vilonix.gapi.module.achievement.achievement;

import lombok.Getter;
import org.bukkit.inventory.ItemStack;
import plusm.vilonix.api.VilonixNetwork;
import plusm.vilonix.api.player.BukkitGamer;
import plusm.vilonix.api.player.GamerManager;
import plusm.vilonix.api.util.ItemUtil;

import java.util.List;

public abstract class Achievement {

    private static final GamerManager GAMER_MANAGER = VilonixNetwork.getGamerManager();

    @Getter
    private final int id;
    private final ItemStack itemStack;
    private final String name;
    private final List<String> lore;

    public Achievement(final int id, final ItemStack item, final String name, final List<String> lore) {
        this.id = id;
        this.itemStack = item;
        this.name = name;
        this.lore = lore;
    }

    public int getId() {
        return id;
    }

    public ItemStack getItem() {
        return itemStack.clone();
    }

    public ItemStack getItemStack() {
        return ItemUtil.getBuilder(itemStack.clone())
                .setName(name)
                .setLore(lore)
                .build();
    }

    public String getName() {
        return name;
    }

    public final List<String> getLore() {
        return lore;
    }

    /**
     * узнать сколько % ачивки выполнено
     *
     * @return - сколько %
     */
    public abstract int getPercent(AchievementPlayer achievementPlayer);

    /**
     * кол-во очков за выполнение ачивки
     *
     * @return - кол-во очков
     */
    public abstract int getPoints();

    /**
     * вызывается когда ачивка была выполнен
     *
     * @param player - кто выполнил
     */
    protected abstract void complete(BukkitGamer gamer);
}