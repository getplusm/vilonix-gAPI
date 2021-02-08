package plusm.vilonix.gapi.commands;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;
import plusm.vilonix.api.VilonixNetwork;
import plusm.vilonix.api.command.CommandInterface;
import plusm.vilonix.api.command.SpigotCommand;
import plusm.vilonix.api.player.GamerEntity;
import plusm.vilonix.api.util.TimeUtil;

import java.lang.management.ManagementFactory;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;

public final class MemoryCommand implements CommandInterface {

    private final String spigotName = VilonixNetwork.getGamerManager().getSpigot().getName();

    public MemoryCommand() {
        SpigotCommand spigotCommand = COMMANDS_API.register("memory", this, "mem");
        spigotCommand.setGroup("ADMIN");
    }

    @Override
    public void execute(GamerEntity gamerEntity, String command, String[] args) {
        Runtime runtime = Runtime.getRuntime();
        gamerEntity.sendMessage("Ïàìÿòü ñåğâåğà §c§l" + spigotName + "§f:");
        gamerEntity.sendMessage(" §c\u2022 §fÀïòàéì: - §7" + TimeUtil.leftTime(System.currentTimeMillis() - ManagementFactory.getRuntimeMXBean().getStartTime()));
        gamerEntity.sendMessage(" §c\u2022 §fÌàêñèìàëüíî - §7" + runtime.maxMemory() / 1048576L + " ÌÁ");
        gamerEntity.sendMessage(" §c\u2022 §fÂûäåëåíî - §a" + runtime.totalMemory() / 1048576L + " ÌÁ");
        gamerEntity.sendMessage(" §c\u2022 §fÑâîáîäíî - §e" + runtime.freeMemory() / 1048576L + " ÌÁ");
        gamerEntity.sendMessage(
                " §c\u2022 §fÈñïîëüçóåòñÿ - §c" + (runtime.totalMemory() - runtime.freeMemory()) / 1048576L + " ÌÁ");
        gamerEntity.sendMessage(" ");

        for (World world : Bukkit.getWorlds()) {
            AtomicInteger tileEntities = new AtomicInteger();

            try {
                for (Chunk chunk : world.getLoadedChunks()) {
                    tileEntities.addAndGet(chunk.getTileEntities().length);
                }

            } catch (ClassCastException ex) {
                Bukkit.getLogger().log(Level.SEVERE, "Corrupted chunk achievement on world " + world, ex);
            }

            gamerEntity.sendMessage(" §c" + world.getName());
            gamerEntity.sendMessage("  §fİíòèòè: §a" + world.getEntities().size());
            gamerEntity.sendMessage("  §f×àíêîâ: §a" + world.getLoadedChunks().length);
            gamerEntity.sendMessage("  §fÒàéëîâ: §a" + tileEntities);
        }

    }
}
