package plusm.vilonix.gapi.commands;

import plusm.vilonix.api.VilonixNetwork;
import plusm.vilonix.api.command.CommandInterface;
import plusm.vilonix.api.command.SpigotCommand;
import plusm.vilonix.api.player.*;
import plusm.vilonix.api.player.constans.KeyType;
import plusm.vilonix.api.sql.PlayerInfoLoader;
import plusm.vilonix.api.util.Pair;
import plusm.vilonix.api.util.StringUtil;
import plusm.vilonix.gapi.utils.bukkit.BukkitUtil;

import java.util.Map;

public final class GiveKeyCommand implements CommandInterface {

    private final GamerManager gamerManager = VilonixNetwork.getGamerManager();

    public GiveKeyCommand() {
        SpigotCommand command = COMMANDS_API.register("addkeys", this);
        command.setGroup("ADMIN");
    }

    private void addKeys(GamerEntity gamerEntity, String name, KeyType keyType, int keys) {
        IBaseGamer gamer = GamerAPI.getOrCreate(name);
        if (gamer == null || gamer.getPlayerID() == -1) {
            COMMANDS_API.playerNeverPlayed(gamerEntity, name);
            return;
        }

        if (!gamer.isOnline()) {
            Map<KeyType, Pair<Integer, Integer>> keysData = PlayerInfoLoader.getData(gamer.getPlayerID()).getSecond();
            boolean insert = !keysData.containsKey(keyType);

            int random = 0;
            Pair<Integer, Integer> pair = keysData.get(keyType);
            if (pair != null) {
                random = pair.getSecond();
            }

            PlayerInfoLoader.updateData(gamer.getPlayerID(), keyType.getId(), keys, random, insert);

			/*BukkitBalancePacket packet = new BukkitBalancePacket(gamer.getPlayerID(), BukkitBalancePacket.Type.KEYS,
					keyType.getId(), keys);
			CoreConnector.getInstance().sendPacket(packet);*/
        } else if (gamer instanceof BukkitGamer) {
            BukkitGamer targetGamer = (BukkitGamer) gamer;
            targetGamer.changeKeys(keyType, keys);
            targetGamer.sendMessage("§6Vilonix §8> §aВам выдали " + keyType.getName() + " §ax" + keys);
        }

        gamerEntity.sendMessage("§6Vilonix §8> §fВы выдали игроку " + gamer.getDisplayName() + " §a"
                + StringUtil.getNumberFormat(keys) + " §fключей (" + keyType.getName() + ")");
    }

    @Override
    public void execute(GamerEntity gamerEntity, String command, String[] args) {
        if (gamerEntity.isHuman() && !((BukkitGamer) gamerEntity).isDeveloper()) {
            return;
        }

        if (args.length < 3) {
            gamerEntity.sendMessage("§6Vilonix §8> §fОшибка, пишите /addkeys <ник игрока> <type> <ключи>");
            return;
        }

        KeyType keyType;
        try {
            keyType = KeyType.getKey(Integer.parseInt(args[1]));
        } catch (Exception e) {
            keyType = null;
        }

        if (keyType == null) {
            gamerEntity.sendMessage("§6Vilonix §8> §fОшибка, неверно указан тип ключей!");
            return;
        }

        int keys;
        try {
            keys = Integer.parseInt(args[2]);
        } catch (Exception e) {
            keys = 0;
        }

        KeyType finalKeyType = keyType;
        int finalKeys = keys;
        BukkitUtil.runTaskAsync(() -> addKeys(gamerEntity, args[0], finalKeyType, finalKeys));
    }
}
