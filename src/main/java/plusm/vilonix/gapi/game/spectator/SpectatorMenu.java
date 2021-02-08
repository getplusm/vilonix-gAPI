package plusm.vilonix.gapi.game.spectator;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import plusm.vilonix.api.ActionBarAPI;
import plusm.vilonix.api.VilonixNetwork;
import plusm.vilonix.api.event.game.PlayerKillEvent;
import plusm.vilonix.api.game.GameSettings;
import plusm.vilonix.api.game.GameState;
import plusm.vilonix.api.game.TeamManager;
import plusm.vilonix.api.inventory.DItem;
import plusm.vilonix.api.inventory.InventoryAPI;
import plusm.vilonix.api.inventory.type.DInventory;
import plusm.vilonix.api.player.BukkitGamer;
import plusm.vilonix.api.player.GamerManager;
import plusm.vilonix.api.util.InventoryUtil;
import plusm.vilonix.gapi.game.module.WaitModule;
import plusm.vilonix.gapi.game.team.SelectionTeam;
import plusm.vilonix.gapi.loader.DartaAPI;
import plusm.vilonix.gapi.utils.bukkit.BukkitUtil;
import plusm.vilonix.gapi.utils.core.PlayerUtil;
import plusm.vilonix.gapi.utils.inventory.ItemUtil;
import plusm.vilonix.libraries.LibAPI;
import plusm.vilonix.libraries.interfaces.packet.PacketContainer;
import plusm.vilonix.libraries.interfaces.packet.entityplayer.PacketCamera;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SpectatorMenu {
    static final Map<String, String> CAMERA = new ConcurrentHashMap<String, String>();
    private static final ActionBarAPI ACTION_BAR_API = VilonixNetwork.getActionBarAPI();
    private static final GamerManager GAMER_MANAGER = VilonixNetwork.getGamerManager();
    private static final InventoryAPI INVENTORY_API = VilonixNetwork.getInventoryAPI();
    private static final List<SpectatorMenu> MENUS = new ArrayList<SpectatorMenu>();

    private static final PacketContainer PACKET_CONTAINER = LibAPI.getManager().getPacketContainer();
    private List<DInventory> pages = new ArrayList<DInventory>();

    private SpectatorMenu() {
        int size = PlayerUtil.getAlivePlayers().size();
        int pagesCount = InventoryUtil.getPagesCount(size, 28);
        DInventory dInventory;
        for (int i = 0; i < pagesCount; ++i) {
            if (pagesCount > 1 && i != 0) {
                dInventory = INVENTORY_API.createInventory("Телепортер #" + (i + 1), 6);
            } else {
                dInventory = INVENTORY_API.createInventory("Телепортер", 6);
            }
            pages.add(dInventory);
        }
        MENUS.add(this);

        updateInventory();
    }

    public static void clearData() {
        MENUS.clear();
        CAMERA.clear();
    }

    public static void createMenu() {
        new SpectatorMenu();
        new Thread(() -> {
            while (GameState.getCurrent() == GameState.GAME) {
                try {
                    for (String spectatorName : CAMERA.values()) {
                        Player spectator = Bukkit.getPlayer(spectatorName);
                        if (spectator == null)
                            continue;
                        BukkitGamer gamer = GAMER_MANAGER.getGamer(spectator);
                        if (gamer == null)
                            return;
                        ACTION_BAR_API.sendBar(spectator, "§fНажмите §aSHIFT §fчтобы покинуть режим наблюдателя");
                    }
                    Thread.sleep(5 * 50);
                    update();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }).start();
    }

    private static String onPercent(double now, double max) {
        now = now * 100 / max;
        String color = "a";
        if (now <= 25)
            color = "c";
        if (25 < now && now < 85)
            color = "e";
        if (now >= 85)
            color = "a";
        return "§" + color + (int) now + "%";
    }

    public static void openInventory(Player player) {
        BukkitGamer gamer = GAMER_MANAGER.getGamer(player);
        if (gamer == null)
            return;

        for (SpectatorMenu spectatorMenu : MENUS) {
            DInventory dInventory = spectatorMenu.pages.get(0);
            if (dInventory != null) {
                dInventory.openInventory(player);
            }
        }
    }

    public static void update() {
        for (SpectatorMenu spectatorMenu : MENUS) {
            spectatorMenu.updateInventory();
        }
    }

    @SuppressWarnings("deprecation")
    private List<String> addLore(Player player) {
        List<String> lore = new ArrayList<>();
        TeamManager team = SelectionTeam.getSelectedTeams().get(player);

        lore.add(" ");
        if (PlayerKillEvent.getPlayerKiller(player) != null) {
            int kills = PlayerKillEvent.getPlayerKiller(player).getKills().size();
            lore.add("§7Убийств: §c" + String.valueOf(kills));
        } else {
            lore.add("§7Убийств: §c" + String.valueOf(0));
        }

        if (GameSettings.teamMode && team != null) {
            lore.add("§7Команда: " + team.getChatColor() + team.getName());
        }

        if (WaitModule.perkgui != null && WaitModule.perkgui.get(player) != null) {
            if (WaitModule.perkgui.get(player).getPerk() != null) {
                lore.add("§7Выбранный навык: §a" + WaitModule.perkgui.get(player).getPerk().getName());
            } else {
                lore.add("§7Выбранный навык: §cНе выбран");
            }
        }

        lore.addAll(Arrays.asList("§7Здоровье: §e" + onPercent(player.getHealth(), player.getMaxHealth()),
                "§7Голод: §e" + onPercent(player.getFoodLevel(), 20), "",
                "§e▸ ЛКМ, чтобы телепортироваться",
                "§e▸ ПКМ, чтобы наблюдать от 1-ого лица"));
        return lore;
    }

    public void updateInventory() {
        int pagesCount = InventoryUtil.getPagesCount(PlayerUtil.getAlivePlayers().size(), 28);

        int slot = 10;
        int pageNum = 0;
        for (DInventory inventory : pages)
            inventory.clearInventory();

        INVENTORY_API.pageButton(pagesCount, pages, 47, 51);

        for (Player alive : PlayerUtil.getAlivePlayers()) {
            BukkitGamer gamer = GAMER_MANAGER.getGamer(alive);
            if (gamer == null)
                continue;
            (pages.get(pageNum)).setItem(slot, new DItem(ItemUtil.setItemMeta(gamer.getHead(),
                    "§r" + gamer.getPrefix() + alive.getName(), addLore(alive), 1), (player, clickType, slot1) -> {
                player.teleport(gamer.getPlayer().getLocation());
                if (clickType.isRightClick()) {
                    BukkitUtil.runTaskLaterAsync(5L, () -> {
                        VilonixNetwork.getTitlesAPI().sendTitle(player, "§eРежим наблюдения",
                                "Наблюдаем за игроком:" + gamer.getChatName());
                        Player gamerPlayer = gamer.getPlayer();
                        if (gamerPlayer != null) {
                            PacketCamera packetCamera = PACKET_CONTAINER.getCameraPacket(gamerPlayer);
                            packetCamera.sendPacket(player);
                        }
                        // Object entityPlayer = ReflectionUtils.getNMSPlayer(gamer.getOwner());
                        // PacketPlayOutCamera packet = newlocale PacketPlayOutCamera(entityPlayer);
                        // packet.sendPacket(achievement);
                    });
                    if (CAMERA.containsValue(player.getName())) {
                        for (String aliveName : CAMERA.keySet()) {
                            if (!CAMERA.get(aliveName).equals(player.getName()))
                                continue;
                            CAMERA.remove(aliveName, player.getName());
                        }
                    }
                    player.getInventory().setHeldItemSlot(1);
                    PlayerUtil.getSpectators()
                            .forEach(spectator -> spectator.hidePlayer(DartaAPI.getInstance(), player));
                    CAMERA.put(gamer.getPlayer().getName(), player.getName());
                }
                player.closeInventory();
            }));

            if (slot == 16) {
                slot = 19;
            } else if (slot == 25) {
                slot = 28;
            } else if (slot == 34) {
                slot = 37;
            } else if (slot == 43) {
                slot = 10;
                ++pageNum;
            } else {
                ++slot;
            }
        }
    }
}
