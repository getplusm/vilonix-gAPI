package plusm.vilonix.gapi.guis.shop;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import plusm.vilonix.api.VilonixNetwork;
import plusm.vilonix.api.inventory.InventoryAPI;
import plusm.vilonix.api.inventory.type.DInventory;
import plusm.vilonix.api.player.GamerManager;
import plusm.vilonix.api.sound.SoundAPI;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class ShopInventory implements Listener {

    protected static final GamerManager GAMER_MANAGER = VilonixNetwork.getGamerManager();
    public static Map<String, ShopInventory> shopPlayers = new ConcurrentHashMap<>();
    protected static InventoryAPI inventoryAPI = VilonixNetwork.getInventoryAPI();
    protected static SoundAPI soundAPI = VilonixNetwork.getSoundAPI();

    static {
        new ShopListener();
    }

    protected Player player;

    /*
     * Страницы ивентаря. Начальная страница - 0. Поддерживает передвижение по
     * страницам.
     */
    protected List<DInventory> shopPages = new ArrayList<>();

    public ShopInventory(Player player) {
        this.player = player;
        if (getTrigger() != null) {
            shopPlayers.put(player.getName(), this);
        }
    }

    protected abstract void fillShopPages(int page); // начальное заполение страниц

    public Player getPlayer() {
        return player;
    }

    public List<DInventory> getShopPages() {
        return shopPages;
    }

    protected abstract Material getTrigger(); // Какой предмет вызывает открытие инвенторя

}
