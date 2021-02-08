package plusm.vilonix.gapi.commands;

import plusm.vilonix.api.command.CommandInterface;
import plusm.vilonix.api.command.SpigotCommand;
import plusm.vilonix.api.player.BukkitGamer;
import plusm.vilonix.api.player.GamerEntity;
import plusm.vilonix.api.player.constans.PurchaseType;
import plusm.vilonix.api.util.StringUtil;

public final class ConvertCommand implements CommandInterface {

    public ConvertCommand() {
        SpigotCommand command = COMMANDS_API.register("convert", this);
        command.setOnlyPlayers(true);
    }

    @Override
    public void execute(GamerEntity gamerEntity, String s, String[] args) {
        BukkitGamer gamer = (BukkitGamer) gamerEntity;

        if (args.length < 1) {
            gamer.sendMessage("§cОшибка, пишите §f/convert <рубли>");
            return;
        }

        int sum;
        try {
            sum = Integer.parseInt(args[0]);
        } catch (Exception e) {
            gamer.sendMessage("§cОшибка, нужно указывать только положительное челое число!");
            return;
        }

        if (sum < 1) {
            gamer.sendMessage("§cОшибка, нужно указывать только положительное челое число!");
            return;
        }

        if (!gamer.changeMoney(PurchaseType.VILONIX, -sum)) {
            return;
        }
        int total = sum * (gamer.isDonater() ? 1200 : 500);
        gamer.changeMoney(PurchaseType.MONEY, total);
        gamer.sendMessage("§6Vilonix §8> §fВы успешно обменяли §6" + StringUtil.getNumberFormat(sum) + " Рублей §fна §d" + StringUtil.getNumberFormat(total) + " Монет");
    }
}
