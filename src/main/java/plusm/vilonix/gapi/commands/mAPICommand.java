package plusm.vilonix.gapi.commands;

import com.google.common.collect.ImmutableList;
import plusm.vilonix.api.VilonixNetwork;
import plusm.vilonix.api.command.CommandInterface;
import plusm.vilonix.api.command.CommandTabComplete;
import plusm.vilonix.api.command.SpigotCommand;
import plusm.vilonix.api.player.BukkitGamer;
import plusm.vilonix.api.player.GamerEntity;
import plusm.vilonix.api.player.IBaseGamer;
import plusm.vilonix.gapi.donatemenu.DonateMenuData;

import java.util.List;
import java.util.stream.Collectors;

public class mAPICommand implements CommandInterface, CommandTabComplete {

    private final ImmutableList<String> immutableList = ImmutableList.of("fastmessages", "joinmessages");

    public mAPICommand() {
        SpigotCommand command = COMMANDS_API.register("mAPI", this);
        command.setCommandTabComplete(this);
        command.setOnlyPlayers(true);
        command.setGroup("ADMIN");
    }

    @Override
    public void execute(GamerEntity gamerEntity, String s, String[] strings) {
        BukkitGamer gamer = (BukkitGamer) gamerEntity;
        if (strings.length < 2) {
            gamer.sendMessage("§cНа данный моменты доступно:\n/mapi reload <секция(нажми ТАБ)>");
            return;
        }
        switch (strings[0].toUpperCase()) {
            case "reload":
                String type = strings[1];
                if (type.equalsIgnoreCase("fw") || type.equalsIgnoreCase("fastmessages")) {
                    for (BukkitGamer g : VilonixNetwork.getGamerManager().getGamers().values().stream().filter(IBaseGamer::isOnline).collect(Collectors.toList())) {

                    }
                }
                break;
        }
    }

    @Override
    public List<String> getComplete(GamerEntity gamerEntity, String alias, String[] args) {
        if (gamerEntity.isHuman() && !((BukkitGamer) gamerEntity).isDeveloper()) {
            return ImmutableList.of();
        }
        if (args.length == 2) {
            return COMMANDS_API.getCompleteString(immutableList, args);
        }

        return ImmutableList.of();
    }
}
