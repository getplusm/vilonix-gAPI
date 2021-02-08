package plusm.vilonix.gapi.commands;

import plusm.vilonix.api.VilonixNetwork;
import plusm.vilonix.api.command.CommandInterface;
import plusm.vilonix.api.command.SpigotCommand;
import plusm.vilonix.api.player.BukkitGamer;
import plusm.vilonix.api.player.GamerEntity;
import plusm.vilonix.api.player.IBaseGamer;
import plusm.vilonix.api.player.constans.PurchaseType;
import plusm.vilonix.api.sql.GlobalLoader;
import plusm.vilonix.api.util.StringUtil;

import java.util.Map;

public final class MoneyCommand implements CommandInterface {

    public MoneyCommand() {
        SpigotCommand spigotCommand = COMMANDS_API.register("money", this, "balance", "bal", "������", "rubles", "�����", "�����", "������");
        spigotCommand.setOnlyPlayers(true);
    }

    @Override
    public void execute(GamerEntity gamerEntity, String command, String[] args) {
        BukkitGamer gamer = (BukkitGamer) gamerEntity;

        if (args.length > 0) {
            String name;
            try {
                name = args[0];
            } catch (Exception e) {
                name = gamer.getName();
            }
            IBaseGamer target = VilonixNetwork.getGamerManager().getOrCreate(name.toLowerCase());
            if (target == null || target.getPlayerID() == -1) {
                COMMANDS_API.playerNeverPlayed(gamerEntity, name);
                return;
            }
            if (!target.isOnline()) {
                Map<PurchaseType, Integer> playerMoney = GlobalLoader.getPlayerMoney(target.getPlayerID());
                String end = StringUtil.getCorrectWord((playerMoney.getOrDefault(PurchaseType.MONEY, 0)), "MONEY_1");
                gamerEntity.sendMessage("�6Vilonix �8>�f � ������� " + target.getDisplayName()
                        + " �d" + StringUtil.getNumberFormat((playerMoney.getOrDefault(PurchaseType.MONEY, 0))) + " " + end + "�f � �6" +
                        StringUtil.getNumberFormat((playerMoney.getOrDefault(PurchaseType.VILONIX, 0))) + " ������");
            } else if (target instanceof BukkitGamer) {
                BukkitGamer targetGamer = (BukkitGamer) target;
                String end = StringUtil.getCorrectWord(targetGamer.getMoney(PurchaseType.MONEY), "MONEY_1");
                gamerEntity.sendMessage("�6Vilonix �8>�f � ������� " + targetGamer.getDisplayName()
                        + " �d" + StringUtil.getNumberFormat(targetGamer.getMoney(PurchaseType.MONEY)) + " " + end + "�f � �6" +
                        StringUtil.getNumberFormat(targetGamer.getMoney(PurchaseType.VILONIX)) + " ������");
            }
        } else {
            String end = StringUtil.getCorrectWord(gamer.getMoney(PurchaseType.MONEY), "MONEY_1");
            gamerEntity.sendMessage("�6Vilonix �8>�f � ����� ������� �d" + StringUtil.getNumberFormat(gamer.getMoney(PurchaseType.MONEY)) + " " + end + "�f � �6" +
                    StringUtil.getNumberFormat(gamer.getMoney(PurchaseType.VILONIX)) + " ������");
        }
    }
}