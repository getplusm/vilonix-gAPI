package plusm.vilonix.gapi.commands;

import plusm.vilonix.api.command.CommandInterface;
import plusm.vilonix.api.command.SpigotCommand;
import plusm.vilonix.api.player.GamerEntity;
import plusm.vilonix.gapi.guis.GuiDefaultContainer;
import plusm.vilonix.gapi.guis.basic.DonateGui;

public final class DonateCommand implements CommandInterface {

    private final GuiDefaultContainer container;

    public DonateCommand(GuiDefaultContainer container) {
        this.container = container;
        SpigotCommand command = COMMANDS_API.register("donate", this, "donat", "донат", "дон");
        command.setOnlyPlayers(true);
    }

    @Override
    public void execute(GamerEntity gamerEntity, String command, String[] args) {
        container.getGui(DonateGui.class).open();
    }
}
