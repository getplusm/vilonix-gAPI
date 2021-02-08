package plusm.vilonix.gapi.game;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import plusm.vilonix.api.VilonixNetwork;
import plusm.vilonix.api.player.BukkitGamer;
import plusm.vilonix.api.player.GamerManager;
import plusm.vilonix.api.usableitem.ClickType;
import plusm.vilonix.api.usableitem.UsableAPI;
import plusm.vilonix.api.usableitem.UsableItem;
import plusm.vilonix.api.util.ItemUtil;
import plusm.vilonix.gapi.game.spectator.SPlayer;
import plusm.vilonix.gapi.game.spectator.SpectatorMenu;
import plusm.vilonix.gapi.game.team.SelectionTeam;
import plusm.vilonix.gapi.utils.core.PlayerUtil;

import java.util.Arrays;

public class ItemsListener {

    private static final GamerManager GAMER_MANAGER = VilonixNetwork.getGamerManager();

    private static UsableItem hub;
    private static UsableItem kits;
    private static UsableItem perks;
    private static UsableItem selectionTeam;
    private static UsableItem spectatorSettings;
    private static UsableItem startAgain;
    private static UsableItem teleporter;

    public ItemsListener() {
        UsableAPI usableAPI = VilonixNetwork.getUsableAPI();
        selectionTeam =
                usableAPI.createUsableItem(ItemUtil.getBuilder(Material.ANVIL)
                                .setName("§eВыбор команды")
                                .setLore("§7Используйте этот предмет,", "§7чтобы выбрать свою команду")
                                .build(),
                        (player, clickType, block) -> {
                            if (clickType != ClickType.RIGHT)
                                return;
                            SelectionTeam.open(player);
                        });
        kits =
                usableAPI.createUsableItem(ItemUtil.getBuilder(Material.IRON_SWORD)
                                .setName("§eНаборы")
                                .setLore("§7Используйте этот предмет,", "§7чтобы выбрать один из всевозможных", "§7наборов на эту игру")
                                .build(),
                        (player, clickType, block) -> {
                            // пусто
                        });
        perks =
                usableAPI.createUsableItem(ItemUtil.getBuilder(Material.CHEST)
                                .setName("§eУмения")
                                .setLore("§7Используйте этот предмет,", "§7чтобы выбрать один из всевозможных", "§7умений на эту игру")
                                .build(),
                        (player, clickType, block) -> {
                            // пусто
                        });
        teleporter =
                usableAPI.createUsableItem(ItemUtil.getBuilder(Material.COMPASS)
                                .setName("§eТелепортер")
                                .setLore("§7Используйте этот предмет,", "§7чтобы наблюдать за игроками")
                                .build(),
                        (player, clickType, block) -> {
                            if (clickType != ClickType.RIGHT)
                                return;
                            SpectatorMenu.openInventory(player);
                        });
        hub =
                usableAPI.createUsableItem(ItemUtil.getBuilder(Material.SLIME_BALL)
                                .setName("§eВыход")
                                .setLore("§7Используйте этот предмет,", "§7чтобы покинуть арену")
                                .build(),
                        (player, clickType, block) -> {
                            if (clickType != ClickType.RIGHT)
                                return;
                            PlayerUtil.redirectToHub(player);
                        });
        startAgain =
                usableAPI.createUsableItem(ItemUtil.getBuilder(Material.PAPER)
                                .setName("§eИграть заного")
                                .setLore("§7Используйте этот предмет,", "§7чтобы начать новую игру")
                                .build(),
                        (player, clickType, block) -> {
                            PlayerUtil.playerAgain(player);
                        });
        spectatorSettings =
                usableAPI.createUsableItem(ItemUtil.getBuilder(Material.REDSTONE_COMPARATOR)
                        .setName("§eНастройки наблюдателя")
                        .setLore("§7Используйте этот предмет,", "§7чтобы изменить настройки", "§7наблюдателя")
                        .build(), (player, clickType, block) -> {
                    if (clickType != ClickType.RIGHT)
                        return;
                    SPlayer.getSPlayer(player).openInventory();
                });

    }

    private static ItemStack get(UsableItem type) {
        UsableItem usableItem = type;
        if (usableItem == null) {
            usableItem = hub;
        }

        return usableItem.getItemStack();
    }

    public static ItemStack getCompass() {
        ItemStack itemStack = new ItemStack(Material.COMPASS);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName("§aЛокатор");
        itemMeta.setLore(Arrays.asList("§7Нажмите ПКМ, чтобы переключить", "§7указатель на другого игрока"));
        itemStack.setItemMeta(itemMeta);

        return itemStack;
    }

    public static ItemStack getHub(Player player) {
        if (!player.isOnline())
            return null;
        BukkitGamer gamer = GAMER_MANAGER.getGamer(player);
        if (gamer == null)
            return null;
        return get(hub);
    }

    public static ItemStack getKits(Player player) {
        if (!player.isOnline())
            return null;
        BukkitGamer gamer = GAMER_MANAGER.getGamer(player);
        if (gamer == null)
            return null;
        return get(kits);
    }

    public static ItemStack getPerk(Player player) {
        if (!player.isOnline())
            return null;
        BukkitGamer gamer = GAMER_MANAGER.getGamer(player);
        if (gamer == null)
            return null;
        return get(perks);
    }

    public static ItemStack getSelectionTeam(Player player) {
        if (!player.isOnline())
            return null;
        BukkitGamer gamer = GAMER_MANAGER.getGamer(player);
        if (gamer == null)
            return null;
        return get(selectionTeam);
    }

    public static ItemStack getSpectatorSettings(Player player) {
        if (!player.isOnline())
            return null;
        BukkitGamer gamer = GAMER_MANAGER.getGamer(player);
        if (gamer == null)
            return null;
        return get(spectatorSettings);
    }

    public static ItemStack getStartAgain(Player player) {
        if (!player.isOnline())
            return null;
        BukkitGamer gamer = GAMER_MANAGER.getGamer(player);
        if (gamer == null)
            return null;
        return get(startAgain);
    }

    public static ItemStack getTeleporter(Player player) {
        if (!player.isOnline())
            return null;
        BukkitGamer gamer = GAMER_MANAGER.getGamer(player);
        if (gamer == null)
            return null;
        return get(teleporter);
    }
}
