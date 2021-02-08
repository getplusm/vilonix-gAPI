package plusm.vilonix.gapi.functions;

import org.bukkit.DyeColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.inventory.EnchantingInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Dye;
import plusm.vilonix.api.game.GameModeType;
import plusm.vilonix.api.mAPI;
import plusm.vilonix.api.player.BukkitGamer;
import plusm.vilonix.api.util.ConfigUtil;
import plusm.vilonix.gapi.utils.DListener;
import plusm.vilonix.libraries.LibAPI;
import plusm.vilonix.libraries.interfaces.NmsManager;
import plusm.vilonix.libraries.interfaces.gui.DEnchantingTable;
import plusm.vilonix.libraries.types.EnchantingSlot;

import java.util.Objects;

public class EnchantingListener extends DListener {

    private final NmsManager nmsManager = LibAPI.getManager();

    private ItemStack getLapis() {
        Dye d = new Dye();
        d.setColor(DyeColor.BLUE);
        @SuppressWarnings("deprecation")
        ItemStack i = d.toItemStack();
        i.setAmount(3);
        return i;
    }

    @EventHandler
    public void onEnchantingCommand(PlayerCommandPreprocessEvent e) {
        if (e.getMessage().equalsIgnoreCase("/ie")) {
            Player player = e.getPlayer();
            BukkitGamer gamer = GAMER_MANAGER.getGamer(player);
            if (gamer.getGameMode() != GameModeType.SPECTATOR) {
                if (gamer.isLite()) {
                    DEnchantingTable table = nmsManager.getEnchantingTable(player);
                    table.addItem(EnchantingSlot.LAPIS, getLapis());
                    table.openGui();
                    e.setCancelled(true);
                } else {
                    gamer.sendMessage(ConfigUtil.getMessage("NO_PERMS_GROUP", f->f.replace("%minimal_group%", Objects.requireNonNull(mAPI.getLuckPerms().getGroupManager().getGroup("LITE").getDisplayName()))));
                    e.setCancelled(true);
                }
            } else {
                gamer.sendMessage("§6Vilonix §8> §cДанная команда не доступна во время игры");
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onEnchantment(EnchantItemEvent e) {
        Inventory inventory = e.getInventory();
        inventory.setItem(EnchantingSlot.LAPIS.getSlot(), getLapis());
    }

    @EventHandler
    public void onInventoryClickEvent(InventoryClickEvent e) {
        if (e.getInventory().getType() == InventoryType.ENCHANTING) {
            int slot = e.getRawSlot();
            if (slot == 1) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent e) {
        if (e.getInventory().getType() == InventoryType.ENCHANTING) {
            EnchantingInventory inventory = (EnchantingInventory) e.getInventory();
            inventory.setSecondary(null);
        }
    }

    @EventHandler
    public void onOpenInventory(InventoryOpenEvent e) {
        if (e.getInventory().getType() == InventoryType.ENCHANTING) {
            Inventory inventory = e.getInventory();
            inventory.setItem(EnchantingSlot.LAPIS.getSlot(), getLapis());
        }
    }
}
