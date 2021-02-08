package plusm.vilonix.gapi.commands;

import org.bukkit.entity.Player;
import plusm.vilonix.api.command.CommandInterface;
import plusm.vilonix.api.command.SpigotCommand;
import plusm.vilonix.api.player.BukkitGamer;
import plusm.vilonix.api.player.GamerEntity;
import plusm.vilonix.gapi.guis.GuiDefaultContainer;
import plusm.vilonix.gapi.guis.basic.HelpGui;

public final class HelpCommand implements CommandInterface {

    private final GuiDefaultContainer container;

    public HelpCommand(GuiDefaultContainer guiDefaultContainer) {
        this.container = guiDefaultContainer;
        SpigotCommand command = COMMANDS_API.register("help", this, "ןמלמשü");
        command.setOnlyPlayers(true);
    }

    @Override
    public void execute(GamerEntity gamerEntity, String command, String[] args) {
        container.getGui(HelpGui.class).open();
    }
}
