package plusm.vilonix.gapi.donatemenu.commands;

import org.bukkit.entity.Player;
import plusm.vilonix.api.command.CommandInterface;
import plusm.vilonix.api.command.SpigotCommand;
import plusm.vilonix.api.player.BukkitGamer;
import plusm.vilonix.api.player.GamerEntity;
import plusm.vilonix.gapi.donatemenu.DonateMenuListener;
import plusm.vilonix.gapi.donatemenu.guis.MainDonateMenuGui;

public final class DonateMenuCommand implements CommandInterface {

    private final DonateMenuListener donateMenuListener;

    public DonateMenuCommand(DonateMenuListener donateMenuListener) {
        this.donateMenuListener = donateMenuListener;

        SpigotCommand command = COMMANDS_API.register("dm", this, "donatemenu");
        command.setOnlyPlayers(true);
    }

    @Override
    public void execute(GamerEntity gamerEntity, String command, String[] args) {
        BukkitGamer gamer = (BukkitGamer) gamerEntity;
        Player player = gamer.getPlayer();
        donateMenuListener.open(player, MainDonateMenuGui.class);
    }
}
