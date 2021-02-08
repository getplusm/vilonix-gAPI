package plusm.vilonix.gapi.game.team;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import plusm.vilonix.api.VilonixNetwork;
import plusm.vilonix.api.game.GameSettings;
import plusm.vilonix.api.game.TeamManager;
import plusm.vilonix.api.inventory.DItem;
import plusm.vilonix.api.inventory.InventoryAPI;
import plusm.vilonix.api.inventory.type.DInventory;
import plusm.vilonix.api.player.BukkitGamer;
import plusm.vilonix.api.player.GamerManager;
import plusm.vilonix.api.sound.SoundAPI;
import plusm.vilonix.api.sound.SoundType;
import plusm.vilonix.api.util.DIterator;
import plusm.vilonix.api.util.ItemUtil;
import plusm.vilonix.gapi.game.module.WaitModule;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class SelectionTeam {
    private static final GamerManager GAMER_MANAGER = VilonixNetwork.getGamerManager();
    private static final InventoryAPI INVENTORY_API = VilonixNetwork.getInventoryAPI();
    private static final SoundAPI SOUND_API = VilonixNetwork.getSoundAPI();
    private static final Map<Player, TeamManager> selectedTeams = new ConcurrentHashMap<>();
    private static final HashMap<Integer, TeamManager> slots = new HashMap<>();
    private DInventory dInventory;
    private Player player;

    public SelectionTeam(Player player) {
        BukkitGamer gamer = GAMER_MANAGER.getGamer(player);
        if (gamer == null)
            return;

        dInventory = INVENTORY_API.createInventory(player, "Выбор команды: " + player.getName(),
                TeamManager.getSlotsTeams()[0] / 9);

        this.player = player;
        createInventory();
    }

    public static List<Player> getPlayersByTeam(TeamManager team) {
        List<Player> list = new ArrayList<>();
        for (Map.Entry<Player, TeamManager> selectedTeam : selectedTeams.entrySet()) {
            if (selectedTeam.getValue() == team) {
                list.add(selectedTeam.getKey());
            }
        }
        return list;
    }

    public static int getPlayersInTeam(TeamManager team) {
        int count = 0;
        for (Map.Entry<Player, TeamManager> selectedTeam : selectedTeams.entrySet()) {
            if (selectedTeam.getValue() == team) {
                count++;
            }
        }
        return count;
    }

    public static Map<Player, TeamManager> getSelectedTeams() {
        return selectedTeams;
    }

    public static void open(Player player) {
        SelectionTeam selectionTeam = WaitModule.getSelectionTeam().get(player.getName());
        if (selectionTeam != null) {
            DInventory dInventory = selectionTeam.getdInventory();
            dInventory.openInventory(player);
        }
    }

    public static void resetSelectedTeams(Player player) {
        selectedTeams.remove(player);
        updateInventoryAll();
    }

    public static void updateInventoryAll() {
        for (SelectionTeam selectionTeam : WaitModule.getSelectionTeam().values())
            selectionTeam.updateInventory();

    }

    private void createInventory() {
        int[] slotsTeams = TeamManager.getSlotsTeams();
        DIterator<TeamManager> teams = new DIterator<>(TeamManager.getTeams().values());
        for (int i = 1; i < slotsTeams.length; i++) {
            TeamManager team = teams.getNext();
            dInventory.setItem(slotsTeams[i],
                    new DItem(
                            ItemUtil.getBuilder(Material.STAINED_GLASS).setDurability(team.getSubID())
                                    .setName(
                                            team.getChatColor() + team.getName()
                                                    + " [0/" + GameSettings.playersInTeam + "]")
                                    .setLore("§7Нет игроков...")
                                    .build(), (player, clickType, slot) -> {
                        if (getPlayersInTeam(team) < GameSettings.playersInTeam) {
                            if (selectedTeams.get(player) != team) {
                                selectedTeams.put(player, team);
                                SOUND_API.play(player, SoundType.POP);
                                updateInventoryAll();
                            } else {
                                SOUND_API.play(player, SoundType.NO);
                            }
                        } else {
                            SOUND_API.play(player, SoundType.NO);
                        }
                    }));
            slots.put(slotsTeams[i], team);
        }
        if (Bukkit.getOnlinePlayers().size() > 1) {
            updateInventory();
        }
    }

    public DInventory getdInventory() {
        return dInventory;
    }

    private void updateInventory() {
        for (Map.Entry<Integer, TeamManager> itemSlot : slots.entrySet()) {
            TeamManager team = itemSlot.getValue();
            if (!GAMER_MANAGER.containsGamer(player))
                return;
            int playersInTeam = getPlayersInTeam(team);
            if (selectedTeams.get(player) == team) {
                ItemStack itemStack = Objects.requireNonNull(GAMER_MANAGER.getGamer(player)).getHead();
                ItemMeta itemMeta = itemStack.getItemMeta();
                List<String> lore = new ArrayList<>();
                lore.add("§7Игроки:");
                lore.addAll(selectedTeams.entrySet().stream().filter(selectedTeam -> selectedTeam.getValue() == team)
                        .map(selectedTeam -> " §8▪ §r" + selectedTeam.getKey().getDisplayName())
                        .collect(Collectors.toList()));
                if (!selectedTeams.containsValue(team)) {
                    lore.clear();
                    lore.add("§7Нет игроков...");
                }
                if (playersInTeam == 0)
                    playersInTeam = 1;

                itemStack.setAmount(playersInTeam);
                itemMeta.setDisplayName(
                        team.getChatColor() + team.getName() + " ["
                                + getPlayersInTeam(team) + "/" + GameSettings.playersInTeam + "]");
                itemMeta.setLore(lore);

                itemStack.setItemMeta(itemMeta);

                int slot = itemSlot.getKey();
                DItem item = dInventory.getItems().get(slot);
                item.setItem(itemStack);
                dInventory.setItem(slot, item);
            } else {
                ItemStack itemStack = ItemUtil.getBuilder(Material.STAINED_GLASS).setDurability(team.getSubID())
                        .setName(team.getChatColor() + team.getName())
                        .setLore("§7Нет игроков...")
                        .build();
                ItemMeta itemMeta = itemStack.getItemMeta();
                List<String> lore = new ArrayList<>();
                lore.addAll(itemStack.getItemMeta().getLore());
                lore.addAll(selectedTeams.entrySet().stream().filter(selectedTeam -> selectedTeam.getValue() == team)
                        .map(selectedTeam -> " §8▪ §r" + selectedTeam.getKey().getDisplayName())
                        .collect(Collectors.toList()));
                if (!selectedTeams.containsValue(team)) {
                    lore.clear();
                    lore.add("§7Нет игроков...");
                }
                if (playersInTeam == 0) {
                    playersInTeam = 1;
                }
                itemStack.setAmount(playersInTeam);
                itemMeta.setDisplayName(itemMeta.getDisplayName() + " [" + getPlayersInTeam(team) + "/"
                        + GameSettings.playersInTeam + "]");
                itemMeta.setLore(lore);

                itemStack.setItemMeta(itemMeta);

                int slot = itemSlot.getKey();
                DItem item = dInventory.getItems().get(slot);
                item.setItem(itemStack);
                dInventory.setItem(slot, item);
            }
        }
    }
}
