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
            gamer.sendMessage("�c������, ������ �f/convert <�����>");
            return;
        }

        int sum;
        try {
            sum = Integer.parseInt(args[0]);
        } catch (Exception e) {
            gamer.sendMessage("�c������, ����� ��������� ������ ������������� ����� �����!");
            return;
        }

        if (sum < 1) {
            gamer.sendMessage("�c������, ����� ��������� ������ ������������� ����� �����!");
            return;
        }

        if (!gamer.changeMoney(PurchaseType.VILONIX, -sum)) {
            return;
        }
        int total = sum * (gamer.isDonater() ? 1200 : 500);
        gamer.changeMoney(PurchaseType.MONEY, total);
        gamer.sendMessage("�6Vilonix �8> �f�� ������� �������� �6" + StringUtil.getNumberFormat(sum) + " ������ �f�� �d" + StringUtil.getNumberFormat(total) + " �����");
    }
}
