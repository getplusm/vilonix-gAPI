package plusm.vilonix.gapi.commands;

import plusm.vilonix.api.command.CommandInterface;
import plusm.vilonix.api.command.SpigotCommand;
import plusm.vilonix.api.player.BukkitGamer;
import plusm.vilonix.api.player.GamerEntity;
import plusm.vilonix.api.util.ConfigUtil;
import plusm.vilonix.api.util.StringUtil;

public final class LevelCommand implements CommandInterface {

    public LevelCommand() {
        SpigotCommand spigotCommand = COMMANDS_API.register("level", this, "lvl", "уровень", "лвл");
        spigotCommand.setOnlyPlayers(true);
    }

    @Override
    public void execute(GamerEntity gamerEntity, String command, String[] args) {
        BukkitGamer gamer = (BukkitGamer) gamerEntity;

        gamerEntity.sendMessage(" ");
        gamerEntity.sendMessage(ConfigUtil.getMessage("LEVEL_COMMAND_1", f -> f.replace("%level%", StringUtil.getNumberFormat(gamer.getLevelNetwork()))));
        gamerEntity.sendMessage(ConfigUtil.getMessage("LEVEL_COMMAND_2", f -> f.replace("%exp%", StringUtil.getNumberFormat(gamer.getExpNextLevel()))));
        gamerEntity.sendMessage(" ");
    }

}
