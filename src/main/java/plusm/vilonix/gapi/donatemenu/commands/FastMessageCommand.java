package plusm.vilonix.gapi.donatemenu.commands;

import org.bukkit.entity.Player;
import plusm.vilonix.api.command.CommandInterface;
import plusm.vilonix.api.command.SpigotCommand;
import plusm.vilonix.api.mAPI;
import plusm.vilonix.api.player.BukkitGamer;
import plusm.vilonix.api.player.GamerEntity;
import plusm.vilonix.api.util.ConfigUtil;
import plusm.vilonix.gapi.donatemenu.DonateMenuListener;
import plusm.vilonix.gapi.donatemenu.FastMessage;
import plusm.vilonix.gapi.donatemenu.guis.FastMessageGui;

import java.util.Map;
import java.util.Objects;

public final class FastMessageCommand implements CommandInterface {

    private final DonateMenuListener donateMenuListener;

    public FastMessageCommand(DonateMenuListener donateMenuListener) {
        this.donateMenuListener = donateMenuListener;

        SpigotCommand command = COMMANDS_API.register("fastmessage", this, "fm");
        command.setOnlyPlayers(true);
    }

    @Override
    public void execute(GamerEntity gamerEntity, String command, String[] args) {
        BukkitGamer gamer = (BukkitGamer) gamerEntity;
        Player player = gamer.getPlayer();

        if (args.length == 0) {
            donateMenuListener.open(player, FastMessageGui.class);
            return;
        }

        if (!gamer.isPrimo()) {
            gamer.sendMessage(ConfigUtil.getMessage("NO_PERMS_GROUP", f -> f.replace("%minimal_donate%", Objects.requireNonNull(Objects.requireNonNull(mAPI.getLuckPerms().getGroupManager().getGroup("PRIMO")).getDisplayName()))));
            return;
        }

        FastMessage fastMessage = null;
        for (Map.Entry<String, FastMessage> entry : FastMessage.getMessages().entrySet()) {
            String name = entry.getKey().toLowerCase();
            if (name.startsWith(args[0].toLowerCase())) {
                fastMessage = entry.getValue();
                break;
            }
        }

        if (fastMessage == null) {
            gamerEntity.sendMessage("§6Vilonix §8>§c Сообщение " + args[0] + " не найдено!");
            return;
        }

        fastMessage.sendToAll(gamer);
    }
}
