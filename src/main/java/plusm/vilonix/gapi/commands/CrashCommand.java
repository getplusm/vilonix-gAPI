package plusm.vilonix.gapi.commands;

import com.google.common.collect.ImmutableList;
import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import plusm.vilonix.api.command.CommandInterface;
import plusm.vilonix.api.command.CommandTabComplete;
import plusm.vilonix.api.command.SpigotCommand;
import plusm.vilonix.api.mAPI;
import plusm.vilonix.api.player.BukkitGamer;
import plusm.vilonix.api.player.GamerEntity;
import plusm.vilonix.libraries.LibAPI;
import plusm.vilonix.libraries.interfaces.NmsManager;

import java.util.List;
import java.util.stream.Collectors;

public final class CrashCommand implements CommandInterface, CommandTabComplete {

    private final NmsManager nmsManager = LibAPI.getManager();

    public CrashCommand() {
        SpigotCommand spigotCommand = COMMANDS_API.register("crash", this);
        spigotCommand.setGroup("ADMIN");
        spigotCommand.setCommandTabComplete(this);
    }

    @Override
    public void execute(GamerEntity gamerEntity, String command, String[] args) {
        if (args.length != 1) {
            COMMANDS_API.notEnoughArguments(gamerEntity, "CRASH_FORMAT");
            return;
        }

        String name = args[0];
        Player target = Bukkit.getPlayer(name);
        if (target == null || !target.isOnline()) {
            COMMANDS_API.playerOffline(gamerEntity, name);
            return;
        }

        if (gamerEntity.getName().equals(name)) {
            gamerEntity.sendMessage("§cТы не можешь сам себя крашнуть");
            return;
        }

        gamerEntity.sendMessage("§fВы крашнули игрока " + target.getDisplayName());
        nmsManager.sendCrashClientPacket(target);

        // PacketPlayOutExplosion packet = new PacketPlayOutExplosion(Double.MAX_VALUE,
        // Double.MAX_VALUE,
        // Double.MAX_VALUE, Float.MAX_VALUE, Collections.EMPTY_LIST, newlocale
        // CraftVec3D(Double.MAX_VALUE,
        // Double.MAX_VALUE, Double.MAX_VALUE));
        // Packets.sendPacket(target, packet);
    }

    @Override
    public List<String> getComplete(GamerEntity gamerEntity, String alias, String[] args) {
        if (gamerEntity.isHuman() && ((BukkitGamer) gamerEntity).getGroup() != mAPI.getLuckPerms().getGroupManager().getGroup("ADMIN")) {
            return ImmutableList.of();
        }

        if (args.length == 1) {
            List<String> names = Bukkit.getOnlinePlayers().stream().map(HumanEntity::getName)
                    .collect(Collectors.toList());
            return COMMANDS_API.getCompleteString(names, args);
        }

        return ImmutableList.of();
    }
}
