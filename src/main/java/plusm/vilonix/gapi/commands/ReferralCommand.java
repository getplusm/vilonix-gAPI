package plusm.vilonix.gapi.commands;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import plusm.vilonix.api.VilonixNetwork;
import plusm.vilonix.api.command.CommandInterface;
import plusm.vilonix.api.command.SpigotCommand;
import plusm.vilonix.api.inventory.gui.Gui;
import plusm.vilonix.api.inventory.gui.Item;
import plusm.vilonix.api.inventory.gui.paginated.PaginatedGui;
import plusm.vilonix.api.inventory.gui.paginated.PaginatedGuiBuilder;
import plusm.vilonix.api.inventory.gui.scheme.MenuScheme;
import plusm.vilonix.api.player.BukkitGamer;
import plusm.vilonix.api.player.GamerAPI;
import plusm.vilonix.api.player.GamerEntity;
import plusm.vilonix.api.player.IBaseGamer;
import plusm.vilonix.api.player.constans.PurchaseType;
import plusm.vilonix.api.player.sections.MoneySection;
import plusm.vilonix.api.player.sections.ReferralSection;
import plusm.vilonix.api.sound.SoundType;
import plusm.vilonix.api.sql.GlobalLoader;
import plusm.vilonix.api.sql.api.query.MysqlQuery;
import plusm.vilonix.api.util.Head;
import plusm.vilonix.api.util.item.ItemStackBuilder;

import java.util.LinkedList;
import java.util.List;

public class ReferralCommand implements CommandInterface {

    public static final List<Integer> DEFAULT_ITEM_SLOTS = new MenuScheme()
            .mask("000000000")
            .mask("011111110")
            .mask("011111110")
            .mask("011111110")
            .mask("011111110")
            .mask("000111000")
            .getMaskedIndexesImmutable();

    public ReferralCommand() {
        SpigotCommand command = COMMANDS_API.register("referal", this, "ref", "�������", "���");
        command.setOnlyPlayers(true);
        command.setGroup("ADMIN");
    }

    @Override
    public void execute(final GamerEntity sender, String s, final String[] args) {
        BukkitGamer gamer = (BukkitGamer) sender;
        Player p = ((BukkitGamer) sender).getPlayer();
        if (args.length == 0) {
            if (!gamer.isAngel()) {
                gamer.sendMessage("&c����������� ������� �������� �� &b&lANGEL&c � ����!");
                return;
            }

            if (GlobalLoader.hasReferral(gamer.getPlayerID())) {
                new Gui(p, 3, "����������� ������� ���������", false) {
                    @Override
                    public void redraw() {
                        setItem(5, 2, ItemStackBuilder.of(Material.PAPER)
                                .name("�b������ ���������")
                                .lore("", "�7������ �������, �������", "�7��������������� ����� �����", "", "�e��������, ����� �������")
                                .build(() -> {
                                    List<Item> items = new LinkedList<>();
                                    if (gamer.getSection(ReferralSection.class).getPlayers().isEmpty())
                                        items.add(ItemStackBuilder.of(Material.GLASS_BOTTLE).name("�c����� :(").lore("", "�c����� �� ����������� ��� ���").build());
                                    else
                                        for (IBaseGamer ref : gamer.getSection(ReferralSection.class).getPlayers())
                                            items.add(ItemStackBuilder.of(Head.getHeadByPlayerName(ref.getName())).name(ref.getDisplayName()).lore("", "�7���� �������������:", gamer.getSection(ReferralSection.class).getRefDate(ref.getPlayerID())).build());

                                    new PaginatedGui(f -> items, p, PaginatedGuiBuilder.create().itemSlots(DEFAULT_ITEM_SLOTS).title("���� ��������"), false).redraw();
                                }));
                    }
                }.open();
                return;
            }
            new Gui(p, 3, "����������� ������� ���������", false) {
                @Override
                public void redraw() {
                    setItem(5, 2, ItemStackBuilder.of(Material.EMERALD_BLOCK)
                            .name("�b������� ����������� ���")
                            .lore("",
                                    "�f����������� �������:",
                                    "&7��� �������� ������ ����������",
                                    "&7������� ������ &6���������&7!",
                                    "",
                                    "&c������� �������� �� &b&lANGEL",
                                    "",
                                    "&f����� �� ���������:",
                                    "&b10 &8> &7100 &6����������",
                                    "&b25 &8> &7����� ������ &7&lPRIMO &7���� 100 &6����������",
                                    "&b50 &8> &7����� ������ &3&lLITE &7���� 300 &6����������",
                                    "&b70 &8> &7����� ������ &e&lALEGRO &7���� 150 &6����������",
                                    "&b85 &8> &7200 &6����������",
                                    "&b90 &8> &7����� ������ &b&lANGEL &7���� 200 &6����������",
                                    "&b100 &8> &7����� ������ &c&lDEMON &7���� 400 &6����������",
                                    "",
                                    "�e��������, ����� ������� ���"
                            )
                            .build(() -> {
                                if (gamer.isAngel()) {
                                    gamer.playSound(SoundType.NO);
                                    return;
                                }
                                GlobalLoader.getMysqlDatabase().execute(MysqlQuery.insertTo("player_referrals")
                                        .set("playerId", gamer.getPlayerID()));
                                gamer.sendMessage("&2����������� ������� &8> &a���� ����������� ������ ������� �������");
                                gamer.playSound(SoundType.LEVEL_UP);
                                p.closeInventory();
                            })
                    );
                    setItem(7, 2, ItemStackBuilder.of(Material.BOOK).name("&b��� ������������")
                            .lore("",
                                    "&7������� ������� &c/ref [���],",
                                    "&7����� ������������ ����������� ���",
                                    ""
                            ).build());
                }
            }.open();
            return;
        }
        if (args[0].equalsIgnoreCase(sender.getName())) {
            sender.sendMessage("&2����������� ������� &8> &c�� �� ������ ����� ���� ��������� :/");
            return;
        }
        IBaseGamer whoGamer = GamerAPI.get(args[0]);
        if (whoGamer == null || whoGamer.getPlayerID() == -1) {
            COMMANDS_API.playerNeverPlayed(sender, args[0]);
            return;
        }
        if (GlobalLoader.referralAlready(gamer.getPlayerID())) {
            sender.sendMessage("&2����������� ������� &8> &c�� ��� ��������� ���������!");
            return;
        }
        if (!GlobalLoader.hasReferral(whoGamer.getPlayerID())) {
            sender.sendMessage("&2����������� ������� &8> &c� ������ ��� ����������� ������!");
            return;
        }
        new Gui(p, 5, "�� �������?", false) {
            @Override
            public void redraw() {
                setItem(22, ItemStackBuilder.of(Material.BOOK)
                        .name("&c&l����������� ����������!")
                        .lore("",
                                "&f��������! ��������� ��� ������� ���-���",
                                "&f��� ������ ��������! �� ����������� ���������",
                                "&f����� &a&n&l1 &a��� &f��� &a������ ������&f.",
                                "")
                        .glowing()
                        .build());
                setItem(32, ItemStackBuilder.of(Material.EMERALD_BLOCK)
                        .name("&a������")
                        .lore("", "&7� ����, ��� ��� �������� ������ ��������", "")
                        .build(() -> {
                            try {
                                if (gamer.getSection(MoneySection.class).changeMoney(PurchaseType.VILONIX, 5)) {
                                    if (whoGamer.getSection(ReferralSection.class).addReferral(gamer.getPlayerID(), 1)) {
                                        p.closeInventory();
                                        sender.sendMessage("&2����������� ������� &8> &f�� ����� ��������� &a" + whoGamer.getDisplayName());
                                        VilonixNetwork.getParticleAPI().shootRandomFirework(p.getLocation());
                                    }
                                }
                            } catch (Exception ex) {
                                ex.printStackTrace();
                                sender.sendMessage("�c������ � ���������� DataGamerBalancer.class");
                                sender.sendMessage("�f��������� ������� ������� ������.");
                            }
                        }));
            }
        }.open();
    }

}

