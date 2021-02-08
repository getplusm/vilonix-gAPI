package plusm.vilonix.gapi.commands;

import com.google.common.collect.ImmutableList;
import plusm.vilonix.api.VilonixNetwork;
import plusm.vilonix.api.command.CommandInterface;
import plusm.vilonix.api.command.CommandTabComplete;
import plusm.vilonix.api.command.SpigotCommand;
import plusm.vilonix.api.player.*;
import plusm.vilonix.api.player.constans.PurchaseType;
import plusm.vilonix.api.sql.GlobalLoader;
import plusm.vilonix.api.util.StringUtil;
import plusm.vilonix.gapi.utils.bukkit.BukkitUtil;

import java.util.List;
import java.util.Map;

public final class GiveMoneyCommand implements CommandInterface, CommandTabComplete {

    private final GamerManager gamerManager = VilonixNetwork.getGamerManager();
    private final ImmutableList<String> immutableList = ImmutableList.of("vilonix", "money");

    public GiveMoneyCommand() {
        SpigotCommand command = COMMANDS_API.register("addmoney", this);
        command.setCommandTabComplete(this);
        command.setGroup("ADMIN");
    }

    private void addMoney(GamerEntity gamerEntity, String name, PurchaseType purchaseType, int money) {
        BukkitUtil.runTaskAsync(() -> {
            IBaseGamer gamer = gamerManager.getOrCreate(name.toLowerCase());
            if (gamer == null || gamer.getPlayerID() == -1) {
                COMMANDS_API.playerNeverPlayed(gamerEntity, name);
                return;
            }

            if (!gamer.isOnline()) {
                Map<PurchaseType, Integer> playerMoney = GlobalLoader.getPlayerMoney(gamer.getPlayerID());
                GlobalLoader.changeMoney(gamer.getPlayerID(), purchaseType, money, !playerMoney.containsKey(purchaseType));
            } else if (gamer instanceof BukkitGamer) {
                BukkitGamer targetGamer = (BukkitGamer) gamer;
                targetGamer.changeMoney(purchaseType, money);
                targetGamer.sendMessage("§6Vilonix §8> §aВам выдали §с" + money + " " + (purchaseType.getId() == 0 ? "§dмонет" : "§6Вилониксов"));
            }

            gamerEntity.sendMessage("§6Vilonix §8> §fВы выдали игроку " + gamer.getDisplayName() + " §a"
                    + StringUtil.getNumberFormat(money) + " §" + purchaseType.getColor() + (purchaseType.getId() == 0 ? "§dМонет" : "§6Вилониксов"));
        });
    }

    @Override
    public void execute(GamerEntity gamerEntity, String command, String[] args) {
        if (gamerEntity.isHuman() && !((BukkitGamer) gamerEntity).isDeveloper())
            return;

        if (args.length < 3) {
            gamerEntity.sendMessage("§6Vilonix §8> §fОшибка, пишите /addmoney <ник игрока> <тип> <кол-во>");
            return;
        }

        int money;
        try {
            money = Integer.parseInt(args[2]);
        } catch (Exception e) {
            money = 0;
        }

        PurchaseType purchaseType;
        try {
            purchaseType = PurchaseType.valueOf(args[1].toUpperCase());
        } catch (Exception e) {
            purchaseType = PurchaseType.MONEY;
        }

        addMoney(gamerEntity, args[0], purchaseType, money);
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
