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
        SpigotCommand command = COMMANDS_API.register("referal", this, "ref", "реферал", "реф");
        command.setOnlyPlayers(true);
        command.setGroup("ADMIN");
    }

    @Override
    public void execute(final GamerEntity sender, String s, final String[] args) {
        BukkitGamer gamer = (BukkitGamer) sender;
        Player p = ((BukkitGamer) sender).getPlayer();
        if (args.length == 0) {
            if (!gamer.isAngel()) {
                gamer.sendMessage("&cРеферальная система доступна от &b&lANGEL&c и выше!");
                return;
            }

            if (GlobalLoader.hasReferral(gamer.getPlayerID())) {
                new Gui(p, 3, "Реферальная система Вилоникса", false) {
                    @Override
                    public void redraw() {
                        setItem(5, 2, ItemStackBuilder.of(Material.PAPER)
                                .name("§bСписок рефералов")
                                .lore("", "§7Список игроков, которые", "§7воспользовались Вашим кодом", "", "§eЩелкните, чтобы перейти")
                                .build(() -> {
                                    List<Item> items = new LinkedList<>();
                                    if (gamer.getSection(ReferralSection.class).getPlayers().isEmpty())
                                        items.add(ItemStackBuilder.of(Material.GLASS_BOTTLE).name("§cПусто :(").lore("", "§cНикто не использовал Ваш код").build());
                                    else
                                        for (IBaseGamer ref : gamer.getSection(ReferralSection.class).getPlayers())
                                            items.add(ItemStackBuilder.of(Head.getHeadByPlayerName(ref.getName())).name(ref.getDisplayName()).lore("", "§7Дата использования:", gamer.getSection(ReferralSection.class).getRefDate(ref.getPlayerID())).build());

                                    new PaginatedGui(f -> items, p, PaginatedGuiBuilder.create().itemSlots(DEFAULT_ITEM_SLOTS).title("Ваши рефералы"), false).redraw();
                                }));
                    }
                }.open();
                return;
            }
            new Gui(p, 3, "Реферальная система Вилоникса", false) {
                @Override
                public void redraw() {
                    setItem(5, 2, ItemStackBuilder.of(Material.EMERALD_BLOCK)
                            .name("§bСоздать реферальный код")
                            .lore("",
                                    "§fРеферальная система:",
                                    "&7Это отличный способ заработать",
                                    "&7игровую валюту &6Вилониксы&7!",
                                    "",
                                    "&cФункция доступна от &b&lANGEL",
                                    "",
                                    "&fПризы за рефералов:",
                                    "&b10 &8> &7100 &6Вилониксов",
                                    "&b25 &8> &7донат статус &7&lPRIMO &7либо 100 &6Вилониксов",
                                    "&b50 &8> &7донат статус &3&lLITE &7либо 300 &6Вилониксов",
                                    "&b70 &8> &7донат статус &e&lALEGRO &7либо 150 &6Вилониксов",
                                    "&b85 &8> &7200 &6Вилониксов",
                                    "&b90 &8> &7донат статус &b&lANGEL &7либо 200 &6Вилониксов",
                                    "&b100 &8> &7донат статус &c&lDEMON &7либо 400 &6Вилониксов",
                                    "",
                                    "§eЩелкните, чтобы создать код"
                            )
                            .build(() -> {
                                if (gamer.isAngel()) {
                                    gamer.playSound(SoundType.NO);
                                    return;
                                }
                                GlobalLoader.getMysqlDatabase().execute(MysqlQuery.insertTo("player_referrals")
                                        .set("playerId", gamer.getPlayerID()));
                                gamer.sendMessage("&2Реферальная система &8> &aВаша реферальная ссылка успешно создана");
                                gamer.playSound(SoundType.LEVEL_UP);
                                p.closeInventory();
                            })
                    );
                    setItem(7, 2, ItemStackBuilder.of(Material.BOOK).name("&bКак использовать")
                            .lore("",
                                    "&7Введите команду &c/ref [ник],",
                                    "&7чтобы использовать реферальный код",
                                    ""
                            ).build());
                }
            }.open();
            return;
        }
        if (args[0].equalsIgnoreCase(sender.getName())) {
            sender.sendMessage("&2Реферальная система &8> &cВы не можете стать свои рефералом :/");
            return;
        }
        IBaseGamer whoGamer = GamerAPI.get(args[0]);
        if (whoGamer == null || whoGamer.getPlayerID() == -1) {
            COMMANDS_API.playerNeverPlayed(sender, args[0]);
            return;
        }
        if (GlobalLoader.referralAlready(gamer.getPlayerID())) {
            sender.sendMessage("&2Реферальная система &8> &cВы уже являетесь рефералом!");
            return;
        }
        if (!GlobalLoader.hasReferral(whoGamer.getPlayerID())) {
            sender.sendMessage("&2Реферальная система &8> &cУ игрока нет реферальной ссылки!");
            return;
        }
        new Gui(p, 5, "Вы уверены?", false) {
            @Override
            public void redraw() {
                setItem(22, ItemStackBuilder.of(Material.BOOK)
                        .name("&c&lОбязательно прочитайте!")
                        .lore("",
                                "&fВнимание! Обдумайте это решение так-как",
                                "&fего нельзя отменить! Вы становитель рефералом",
                                "&fвсего &a&n&l1 &aраз &fдля &aодного игрока&f.",
                                "")
                        .glowing()
                        .build());
                setItem(32, ItemStackBuilder.of(Material.EMERALD_BLOCK)
                        .name("&aГотово")
                        .lore("", "&7Я знаю, что это действие нельзя отменить", "")
                        .build(() -> {
                            try {
                                if (gamer.getSection(MoneySection.class).changeMoney(PurchaseType.VILONIX, 5)) {
                                    if (whoGamer.getSection(ReferralSection.class).addReferral(gamer.getPlayerID(), 1)) {
                                        p.closeInventory();
                                        sender.sendMessage("&2Реферальная система &8> &fВы стали рефералом &a" + whoGamer.getDisplayName());
                                        VilonixNetwork.getParticleAPI().shootRandomFirework(p.getLocation());
                                    }
                                }
                            } catch (Exception ex) {
                                ex.printStackTrace();
                                sender.sendMessage("§cОшибка с обработкой DataGamerBalancer.class");
                                sender.sendMessage("§fПовторите попытку покупки заного.");
                            }
                        }));
            }
        }.open();
    }

}

