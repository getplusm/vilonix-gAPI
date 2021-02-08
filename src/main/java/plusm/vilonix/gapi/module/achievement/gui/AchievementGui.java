package plusm.vilonix.gapi.module.achievement.gui;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import plusm.vilonix.api.VilonixNetwork;
import plusm.vilonix.api.inventory.DItem;
import plusm.vilonix.api.inventory.InventoryAPI;
import plusm.vilonix.api.inventory.action.ClickAction;
import plusm.vilonix.api.inventory.type.MultiInventory;
import plusm.vilonix.api.player.BukkitGamer;
import plusm.vilonix.api.player.GamerManager;
import plusm.vilonix.api.util.InventoryUtil;
import plusm.vilonix.api.util.ItemUtil;
import plusm.vilonix.gapi.guis.CustomItems;
import plusm.vilonix.gapi.module.achievement.achievement.Achievement;
import plusm.vilonix.gapi.module.achievement.achievement.AchievementPlayer;
import plusm.vilonix.gapi.module.achievement.manager.AchievementManager;

public class AchievementGui {
    protected static final InventoryAPI API = VilonixNetwork.getInventoryAPI();
    protected static final GamerManager GAMER_MANAGER = VilonixNetwork.getGamerManager();
    private final Player player;
    private MultiInventory inventory;

    public AchievementGui(final Player player, final String key) {
        this.player = player;
        final BukkitGamer gamer = AchievementGui.GAMER_MANAGER.getGamer(player);
        if (gamer == null) {
            return;
        }
        this.inventory = AchievementGui.API.createMultiInventory(player, key, 5);
    }

    public void setItems(final AchievementManager achievementManager) {
        if (this.player == null || this.inventory == null || achievementManager == null) {
            return;
        }
        final AchievementPlayer achievementPlayer = achievementManager.getPlayerManager().getAchievementPlayer(this.player);
        if (achievementPlayer == null) {
            return;
        }
        int slot = 10;
        int page = 0;
        for (final Achievement achievement : achievementManager.getAchievements().values()) {
            ItemStack itemStack = ItemUtil.getBuilder(achievement.getItemStack())
                    .setName("§a" + achievement.getName())
                    .addLore("", achievementPlayer.getCompleted().containsKey(achievement.getId()) ? "§eВыполнено" : "§eВ процессе выполнения").build();
            if (!achievementPlayer.hasAchievement(achievement)) {
                itemStack = ItemUtil.getBuilder(Material.STAINED_GLASS_PANE)
                        .setDurability((short) 14)
                        .setName("§c" + achievement.getName())
                        .setLore(achievement.getLore())
                        .addLore("", "§7Выполнено: §c" + achievement.getPercent(achievementPlayer) + "%", "", "§cНе выполнено")
                        .build();
            }
            this.inventory.setItem(page, slot, new DItem(itemStack));
            if ((++slot - 8) % 9 == 0) {
                slot += 2;
            }
            if (slot >= 35) {
                slot = 10;
                ++page;
            }
        }
        final int pagesCount = InventoryUtil.getPagesCount(achievementManager.getAchievements().size(), 21);
        AchievementGui.API.pageButton(pagesCount, this.inventory, 38, 42);
    }

    public void addBackItem(final ClickAction clickAction) {
        if (this.inventory == null) {
            return;
        }
        this.inventory.setItem(40, new DItem(CustomItems.getBack(), clickAction));
    }

    public void open() {
        if (this.player == null || this.inventory == null) {
            return;
        }
        this.inventory.openInventory(this.player);
    }
}
