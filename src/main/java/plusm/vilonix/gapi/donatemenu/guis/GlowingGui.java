package plusm.vilonix.gapi.donatemenu.guis;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import plusm.vilonix.api.VilonixNetwork;
import plusm.vilonix.api.inventory.DItem;
import plusm.vilonix.api.mAPI;
import plusm.vilonix.api.player.BukkitGamer;
import plusm.vilonix.api.scoreboard.ScoreBoardAPI;
import plusm.vilonix.api.sound.SoundType;
import plusm.vilonix.api.sql.GlobalLoader;
import plusm.vilonix.api.util.ConfigUtil;
import plusm.vilonix.api.util.ItemUtil;
import plusm.vilonix.gapi.donatemenu.DonateMenuData;
import plusm.vilonix.gapi.utils.bukkit.BukkitUtil;

import java.util.ArrayList;

public final class GlowingGui extends DonateMenuGui {

    private static final ScoreBoardAPI SCORE_BOARD_API = VilonixNetwork.getScoreBoardAPI();

    public GlowingGui(Player player, DonateMenuData donateMenuData) {
        super(player, donateMenuData,
                 "Донатерские обн. > Подсветка персонажа");
    }

    private static void setPrefix(BukkitGamer gamer, ChatColor chatColor) {
        if (VilonixNetwork.isGame()) {
            gamer.sendMessage("§6Vilonix §8> §cВы не можете сменить префикс во время игры");
            SOUND_API.play(gamer.getPlayer(), SoundType.NO);
            return;
        }

        SOUND_API.play(gamer.getPlayer(), SoundType.SELECTED);
        gamer.sendMessage("§6Vilonix §8> §fВаш префикс был изменен");

        String newPrefix = gamer.getPrefix().replaceAll("§[0-9a-e]", "§" + chatColor.getChar());
        if (newPrefix.equalsIgnoreCase(gamer.getPrefix())) {
            return;
        }

        //gamer.setPrefix(newPrefix);
        Player p = gamer.getPlayer();
        VilonixNetwork.getParticleAPI().resetGlowing(p);
        VilonixNetwork.getParticleAPI().getOrCreateGlowing(p, new ArrayList<>(Bukkit.getOnlinePlayers()));
        //BukkitUtil.runTaskAsync(() -> GlobalLoader.setPrefix(gamer.getPlayerID(), "§" + chatColor.getChar()));
        SCORE_BOARD_API.setPrefix(gamer.getPlayer(), newPrefix);
    }

    private void createItem(BukkitGamer owner, int slot, ChatColor color, int blockColorId) {
        boolean available = owner.getPrefixColor() == color;
        String nameColor = "§" + color.getChar() + ConfigUtil.getMessage(color.name().toUpperCase());
        inventory.setItem(slot,
                new DItem(ItemUtil.getBuilder(Material.STAINED_GLASS).setDurability((short) blockColorId)
                        .setName(nameColor)
                        .setLore(available ? "§eЩелкните, чтобы отключить"
                                : "§eЩелкните, чтобы включить " + nameColor + "§e цвет")
                        .removeFlags().glowing(available).build(), (clicker, clickType, slot1) -> {
                    BukkitGamer gamer = GAMER_MANAGER.getGamer(clicker);
                    if (gamer == null || (gamer.getGroup() != mAPI.getLuckPerms().getGroupManager().getGroup("FELIKS") && gamer.getGroup() != mAPI.getLuckPerms().getGroupManager().getGroup("ADMIN"))
                            || gamer.isTester()) {
                        player.sendMessage("§cУ вас нет прав, данная функция только для §a§lFELIKS");
                        SOUND_API.play(player, SoundType.NO);
                        return;
                    }

                    if (available) {
                        SOUND_API.play(player, SoundType.NO);
                        return;
                    }

                    setPrefix(gamer, color);
                    player.closeInventory();
                }));
    }

    @Override
    protected void setItems(BukkitGamer gamer) {
        createItem(gamer, 11, ChatColor.GRAY, 8);
        createItem(gamer, 12, ChatColor.YELLOW, 4);
        createItem(gamer, 13, ChatColor.AQUA, 3);
        createItem(gamer, 14, ChatColor.GREEN, 5);
        createItem(gamer, 15, ChatColor.RED, 14);
        createItem(gamer, 20, ChatColor.DARK_GREEN, 13);
        createItem(gamer, 21, ChatColor.GOLD, 1);
        createItem(gamer, 22, ChatColor.DARK_AQUA, 9);
        createItem(gamer, 23, ChatColor.LIGHT_PURPLE, 6);
        createItem(gamer, 24, ChatColor.BLACK, 15);

        setBack(40, donateMenuData.get(MainDonateMenuGui.class));
    }
}
