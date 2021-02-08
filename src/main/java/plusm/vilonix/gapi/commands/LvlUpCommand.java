package plusm.vilonix.gapi.commands;

import plusm.vilonix.api.command.CommandInterface;
import plusm.vilonix.api.command.SpigotCommand;
import plusm.vilonix.api.player.BukkitGamer;
import plusm.vilonix.api.player.GamerEntity;

public final class LvlUpCommand implements CommandInterface {

    public LvlUpCommand() {
        SpigotCommand spigotCommand = COMMANDS_API.register("levelup", this, "lup");
        spigotCommand.setGroup("ADMIN");
        spigotCommand.setOnlyPlayers(true);
    }

    @Override
    public void execute(GamerEntity gamerEntity, String command, String[] args) {
        if (!gamerEntity.getName().equalsIgnoreCase("plusm") || !gamerEntity.getName().equalsIgnoreCase("h0up") || !gamerEntity.getName().equalsIgnoreCase("Feliks_Soulja"))
            return;
        BukkitGamer gamer = (BukkitGamer) gamerEntity;
        int needExp = gamer.getExpNextLevel();
        gamer.addExp(needExp);
    }
}