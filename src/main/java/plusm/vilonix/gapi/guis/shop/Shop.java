package plusm.vilonix.gapi.guis.shop;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import plusm.vilonix.api.VilonixNetwork;
import plusm.vilonix.api.inventory.DItem;
import plusm.vilonix.api.inventory.InventoryAPI;
import plusm.vilonix.api.inventory.type.DInventory;
import plusm.vilonix.api.player.GamerManager;
import plusm.vilonix.api.player.constans.PurchaseType;
import plusm.vilonix.api.shop.Boxed;
import plusm.vilonix.api.shop.BuyableCoins;
import plusm.vilonix.api.shop.Choosable;
import plusm.vilonix.api.shop.Shopable;
import plusm.vilonix.api.sound.SoundAPI;
import plusm.vilonix.api.sound.SoundType;
import plusm.vilonix.api.util.Rarity;
import plusm.vilonix.api.util.StringUtil;
import plusm.vilonix.gapi.utils.inventory.ItemUtil;

import java.util.*;

public abstract class Shop {

    protected static final GamerManager GAMER_MANAGER = VilonixNetwork.getGamerManager();
    protected static final InventoryAPI INVENTORY_API = VilonixNetwork.getInventoryAPI();
    protected static final SoundAPI SOUND_API = VilonixNetwork.getSoundAPI();

    protected Shopable choosedItem = null;
    protected List<DInventory> pages = new ArrayList<>();
    protected Player player;
    protected int selectedPage = -1, selectedSlot = -1;

    protected Shop(Player player, Collection<? extends Shopable> items) {
        this.player = player;
        List<Integer> slots = getSlots();
        Iterator<? extends Shopable> iterator = items.iterator();
        for (int i = 0; i < items.size(); ++i) {
            int page = i / slots.size();
            int slot = slots.get(i % slots.size());
            Shopable item = iterator.next();
            editPage(page, slot, getDItem(item, slot, page));
        }
    }

    protected void editPage(int page, int slot, DItem item) {
        if (page >= pages.size())
            pages.add(INVENTORY_API.createInventory(player, getInventoryName(page), getRows(page), null));

        pages.get(page).setItem(slot, item);
    }

    protected DItem getDItem(Shopable item, int slot, int page) {
        String playerName = player.getName();
        ItemStack toSet = item.getIcon(playerName).clone();
        List<String> lore = toSet.getItemMeta().getLore();

        if (lore == null) {
            lore = new ArrayList<>();
        }
        String prefixCode = "§c";

        if (!item.have(playerName) && item instanceof BuyableCoins) {
            BuyableCoins buyable = (BuyableCoins) item;
            boolean canBuy = true;
            if (buyable.canBuy(playerName) != null) {
                canBuy = false;
                lore.add("");
                lore.add(buyable.canBuy(playerName));
            } else if (buyable.cost(playerName) != 0) {
                String correct = StringUtil.getCorrectWord(buyable.cost(playerName), "MONEY_1");
                lore.add("");
                lore.add("§7Цена: §6" + buyable.cost(playerName) + " §7" + correct);
                if (Objects.requireNonNull(GAMER_MANAGER.getGamer(playerName)).getMoney(PurchaseType.MONEY) < buyable.cost(playerName))
                    canBuy = false;
            }
            if (canBuy) {
                prefixCode = "§e";
            } else {
                lore.add("");
                lore.add("§cДанный предмет вам недоступен");
                toSet.setType(Material.STAINED_GLASS_PANE);
                toSet.setDurability((byte) 14);
                prefixCode = "§c";
            }
        }

        if (!item.have(playerName) && item instanceof Boxed) {
            Boxed boxed = (Boxed) item;
            if (boxed.getRarity() != Rarity.NONE) {
                lore.add("");
                lore.add("§сНапишите про это на форум и получите 10 тысяч опыта!");
            }
        }

        if (item.have(playerName)) {
            if (item instanceof Choosable) {
                Choosable choosable = (Choosable) item;
                if (choosable.isChoosed(playerName)) {
                    toSet = ItemUtil.addEnchantment(toSet);
                    choosedItem = item;
                    selectedSlot = slot;
                    selectedPage = page;
                    lore.add("");
                    lore.add("§a\u25b8 Активировано");
                    prefixCode = "§b";
                } else {
                    lore.add("");
                    lore.add("§e\u25b8 Нажмите, чтобы активировать");
                    prefixCode = "§a";
                }
            } else {
                lore.add("");
                lore.add("§aИмеется");
                prefixCode = "§a";
            }
        }

        toSet = ItemUtil.setItemMeta(toSet, prefixCode + removePrefixCode(toSet.getItemMeta().getDisplayName()), lore);
        return new DItem(toSet, (player1, clickType, slot1) -> {
            if (item.have(playerName) && item instanceof Choosable) { // Если предмет есть и он выбираемый
                ((Choosable) item).choose(playerName); // Выбираем текущий
                if (choosedItem != null && choosedItem != item) { // Если выбранный есть и он не текущий
                    editPage(selectedPage, selectedSlot, getDItem(choosedItem, selectedSlot, selectedPage)); // РЕКУРСИЯ,
                    // УБЬЕТ
                    // НАХРЕН
                    // НЕ
                    // ЛЕЗЬ
                    selectedPage = page;
                    selectedSlot = slot1; // Обновляем положение выбранного предмета
                    choosedItem = item;
                }
                editPage(page, slot1, getDItem(item, slot1, page)); // Обновляем текущий предмет
                SOUND_API.play(player, SoundType.CLICK);
                return;
            }
            if (item.have(playerName) && !(item instanceof Choosable)) {
                player1.closeInventory();
                player1.sendMessage(getErrorMessage());
                SOUND_API.play(player, SoundType.NO);
                return;
            }
            if (item instanceof BuyableCoins) {
                BuyableCoins buyable = (BuyableCoins) item;
                if (buyable.canBuy(playerName) == null) {
                    if (buyable.cost(playerName) > Objects.requireNonNull(GAMER_MANAGER.getGamer(player1)).getMoney(PurchaseType.MONEY)) {
                        player1.closeInventory();
                        SOUND_API.play(player, SoundType.NO);
                        player1.sendMessage("§cУ вас недостаточно денег, чтобы купить данный предмет!");
                        return;
                    }
                    buyable.buy(playerName);
                    SOUND_API.play(player, SoundType.SELECTED);
                    editPage(page, slot1, getDItem(item, slot1, page));
                } else {
                    player1.closeInventory();
                    SOUND_API.play(player, SoundType.NO);
                    player1.sendMessage(buyable.canBuy(playerName));
                }
            }
        });
    }

    protected abstract String getErrorMessage();

    protected abstract String getInventoryName(int page);

    protected abstract int getRows(int page);

    protected abstract List<Integer> getSlots();

    public void openShop(Player player) {
        pages.get(0).openInventory(player);
    }

    private String removePrefixCode(String s) {
        if (s.charAt(0) == '§') {
            return s.substring(2);
        } else {
            return s;
        }
    }
}
