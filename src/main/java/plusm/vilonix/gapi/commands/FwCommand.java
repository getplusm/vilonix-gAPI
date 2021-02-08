package plusm.vilonix.gapi.commands;

import plusm.vilonix.api.VilonixNetwork;
import plusm.vilonix.api.command.CommandInterface;
import plusm.vilonix.api.command.SpigotCommand;
import plusm.vilonix.api.effect.ParticleAPI;
import plusm.vilonix.api.mAPI;
import plusm.vilonix.api.player.BukkitGamer;
import plusm.vilonix.api.player.GamerEntity;
import plusm.vilonix.api.util.Cooldown;

public final class FwCommand implements CommandInterface {

    private final ParticleAPI particleAPI = VilonixNetwork.getParticleAPI();

    public FwCommand() {
        SpigotCommand spigotCommand = COMMANDS_API.register("fireworks", this, "fw");
        spigotCommand.setGroup("PRIMO");
        spigotCommand.setOnlyPlayers(true);
    }

    @Override
    public void execute(GamerEntity gamerEntity, String command, String[] args) {
        BukkitGamer gamer = (BukkitGamer) gamerEntity;
        if (Cooldown.hasOrAddCooldown(gamer, "fw", 120)){
            gamer.sendMessage("§cПерезагрузка пушки..");
            return;
        }
        particleAPI.shootRandomFirework(gamer.getPlayer());
    }
}