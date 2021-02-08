package plusm.vilonix.gapi.game.perk;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;
import plusm.vilonix.api.VilonixNetwork;
import plusm.vilonix.api.inventory.DItem;
import plusm.vilonix.api.inventory.action.InventoryAction;
import plusm.vilonix.api.inventory.type.DInventory;
import plusm.vilonix.api.sound.SoundType;
import plusm.vilonix.gapi.game.ItemsListener;
import plusm.vilonix.gapi.guis.shop.ShopInventory;
import plusm.vilonix.gapi.utils.bukkit.BukkitUtil;
import plusm.vilonix.gapi.utils.inventory.ItemUtil;

import java.util.Arrays;
import java.util.List;

public class PerksGui extends ShopInventory {

    private Perk select = null;

    public PerksGui(Player player) {
        super(player);
        DInventory dInventory = VilonixNetwork.getInventoryAPI().createInventory(player, "Усиления", 5);
        dInventory.createInventoryAction(new InventoryAction() {
            @Override
            public void onClose(Player player) {
                if (select == null)
                    return;
                PerkSaveEvent event = new PerkSaveEvent(player, select);
                BukkitUtil.callEvent(event);
            }

            @Override
            public void onOpen(Player player) {
                fillShopPages(0);
            }
        });
        shopPages.add(dInventory);
    }

    private void addPerk(int page, int position, Perk perk) {
        DInventory dInventory = shopPages.get(page);
        if (perk.has(player) || GAMER_MANAGER.getGamer(player).isLite()) { //отключить предметы, если нет перка
            dInventory.setItem(position, new DItem(
                    (select == null || select.getId() != perk.getId()) ? perk.getItem(player) : enablePerk(perk, false),
                    (player, clickType, slot) -> {
                        DItem dItem = dInventory.getItems().get(slot);
                        if (select != null && select.getName().equals(perk.getName())) {
                            VilonixNetwork.getSoundAPI().play(player, SoundType.DESTROY);
                            dItem.setItem(disablePerk(perk));
                        } else {
                            Perk selectedPrev = select;
                            dItem.setItem(enablePerk(perk, true));
                            if (selectedPrev != null) {
                                addPerk(page, 10 + selectedPrev.getId() / 7 * 9 + selectedPrev.getId() % 7,
                                        selectedPrev);
                            }
                            VilonixNetwork.getSoundAPI().play(player, SoundType.SELECTED);
                        }
                        dInventory.setItem(slot, dItem);
                    }));
        } else {
            dInventory.setItem(position, new DItem(perk.getWrongItem(player), (player, clickType, slot) -> {
                VilonixNetwork.getSoundAPI().play(player, SoundType.NO);
                player.sendMessage(perk.getErrorMessage());
                player.closeInventory();
            }));
        }
    }

    public void apply() {
        HandlerList.unregisterAll(this);
        if (select != null)
            select.onUse(this.player);
    }

    private ItemStack disablePerk(Perk perkSelect) {
        select = null;
        player.sendMessage(Perk.getPrefix() + "§fВы отключили умение §c" + perkSelect.getName());
        return perkSelect.getItem(player);
    }

    private ItemStack enablePerk(Perk perkSelect, boolean msg) {
        select = perkSelect;
        if (msg)
            player.sendMessage(Perk.getPrefix() + "§fВы выбрали умение §e" + perkSelect.getName());

        ItemStack newItem = perkSelect.getItem(player);
        List<String> lore = newItem.getItemMeta().getLore();

        lore.addAll(Arrays.asList("", "§aУмение выбрано"));

        ItemUtil.setItemMeta(newItem, "§a" + newItem.getItemMeta().getDisplayName().substring(2), lore);
        ItemUtil.addEnchantment(newItem);

        return newItem;
    }

    @Override
    public void fillShopPages(int page) {
        for (int i = 0; i < Perk.perks.size(); ++i) {
            addPerk(0, 10 + (i / 7) * 9 + i % 7, Perk.perks.get(i));
        }
    }

    public Perk getPerk() {
        return select;
    }

    public void setPerk(Perk perk) {
        if (perk != null) {
            select = perk;
        }
    }

    @Override
    public Material getTrigger() {
        return ItemsListener.getPerk(player).getType();
    }

}