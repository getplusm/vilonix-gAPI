package plusm.vilonix.gapi.commands;

import com.google.common.collect.ImmutableList;
import plusm.vilonix.api.VilonixNetwork;
import plusm.vilonix.api.command.CommandInterface;
import plusm.vilonix.api.command.CommandTabComplete;
import plusm.vilonix.api.command.SpigotCommand;
import plusm.vilonix.api.player.*;
import plusm.vilonix.api.player.constans.PurchaseType;
import plusm.vilonix.api.sound.SoundAPI;
import plusm.vilonix.api.sound.SoundType;
import plusm.vilonix.api.sql.GlobalLoader;
import plusm.vilonix.api.util.StringUtil;
import plusm.vilonix.gapi.utils.bukkit.BukkitUtil;

import java.util.List;
import java.util.Map;

public final class PayCommand implements CommandInterface, CommandTabComplete {

    private final GamerManager gamerManager = VilonixNetwork.getGamerManager();
    private final SoundAPI SOUND_API = VilonixNetwork.getSoundAPI();
    private final ImmutableList<String> immutableList = ImmutableList.of("vilonix", "money");

    public PayCommand() {
        SpigotCommand command = COMMANDS_API.register("pay", this, "перевод");
        command.setCommandTabComplete(this);
    }

    private void pay(GamerEntity gamerEntity, String name, PurchaseType purchaseType, int money) {
        BukkitUtil.runTaskAsync(() -> {
            if (gamerEntity.getName().equals(name)) {
                gamerEntity.sendMessage("§cпереводы на себя запрещены в этой вселенной §aO_o");
                return;
            }
            IBaseGamer gamer = gamerManager.getOrCreate(name.toLowerCase());
            if (gamer == null || gamer.getPlayerID() == -1) {
                COMMANDS_API.playerNeverPlayed(gamerEntity, name);
                return;
            }
            try {
                if (!gamer.isOnline()) {
                    Map<PurchaseType, Integer> playerMoney = GlobalLoader.getPlayerMoney(gamer.getPlayerID());
                    GlobalLoader.changeMoney(gamer.getPlayerID(), purchaseType, money, !playerMoney.containsKey(purchaseType));
                } else if (gamer instanceof BukkitGamer) {
                    BukkitGamer targetGamer = (BukkitGamer) gamer;
                    targetGamer.changeMoney(purchaseType, money);
                    targetGamer.sendMessage("§6Vilonix §8> " + gamerEntity.getChatName() + " перевел вам " + money + (purchaseType.getId() == 0 ? " §dмонет" : " §6Вилониксов"));
                    SOUND_API.play(targetGamer.getPlayer(), SoundType.LEVEL_UP);
                }
                if (((BukkitGamer) gamerEntity).changeMoney(purchaseType, -money)) {
                    gamerEntity.sendMessage("§6Vilonix §8> §fВы передали игроку " + gamer.getDisplayName() + " §a"
                            + StringUtil.getNumberFormat(money) + " §" + purchaseType.getColor() + (purchaseType.getId() == 0 ? "§dМонет" : "§6Вилониксов"));
                    SOUND_API.play(((BukkitGamer) gamerEntity).getPlayer(), SoundType.SELECTED);
                } else {
                    gamerEntity.sendMessage("§cОшибка с обработкой команды. §lGGPAY#1");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                gamerEntity.sendMessage("§cОшибка с обработкой DataGamerBalancer.class");
                gamerEntity.sendMessage("§fПовторите попытку покупки заного.");
            }
        });
    }

    @Override
    public void execute(GamerEntity gamerEntity, String command, String[] args) {
        if (!gamerEntity.isHuman())
            return;
        BukkitGamer gamer = (BukkitGamer) gamerEntity;
        if (args.length < 3) {
            gamerEntity.sendMessage("§6Vilonix §8> §fОшибка, пишите §c/pay <ник игрока> <тип(нажмите TAB)> <кол-во>");
            return;
        }

        int money;
        try {
            money = Integer.parseInt(args[2]);
        } catch (NumberFormatException e) {
            gamerEntity.sendMessage("§cВведите положительное число!");
            return;
        }
        if (String.valueOf(money).contains("-") || String.valueOf(money).contains("/") || String.valueOf(money).contains("%") || String.valueOf(money).contains("=") || String.valueOf(money).contains(">") || String.valueOf(money).contains("<")) {
            gamer.sendMessage("§cВведенное число имеет чужие аргументы");
            return;
        }
        if (money <= 0){
            gamer.sendMessage("§cНельзя переводить суммы = 0");
            return;
        }
        PurchaseType purchaseType;
        try {
            purchaseType = PurchaseType.valueOf(args[1].toUpperCase());
        } catch (Exception e) {
            purchaseType = PurchaseType.MONEY;
        }
        if (gamer.getMoney(purchaseType) < money) {
            gamer.sendMessage("§6Vilonix §8> §cВаш баланс " + (purchaseType.getId() == 0 ? "§dмонет" : "§6вилониксов") + " §cслишком мал для переводов.");
            return;
        }
        pay(gamerEntity, args[0], purchaseType, money);
    }

    @Override
    public List<String> getComplete(GamerEntity gamerEntity, String alias, String[] args) {
        if (gamerEntity.isHuman() && !((BukkitGamer) gamerEntity).isDeveloper()) {
            return ImmutableList.of();
        }

        if (args.length == 1) {
            return COMMANDS_API.getCompleteString(GamerAPI.getGamers().keySet(), args);
        }

        if (args.length == 2) {
            return COMMANDS_API.getCompleteString(immutableList, args);
        }

        return ImmutableList.of();
    }
}
