package plusm.vilonix.gapi.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.plugin.java.JavaPlugin;
import plusm.vilonix.api.event.gamer.async.AsyncGamerJoinEvent;
import plusm.vilonix.api.player.BukkitGamer;
import plusm.vilonix.api.player.constans.JoinMessage;
import plusm.vilonix.api.player.constans.Version;
import plusm.vilonix.api.util.StringUtil;

public final class JoinListener extends DListener<JavaPlugin> {

    public JoinListener(JavaPlugin javaPlugin) {
        super(javaPlugin);
    }

    @EventHandler
    public void onJoin(AsyncGamerJoinEvent e) {
        BukkitGamer gamer = e.getGamer();
        /*if (gamer.getSetting(SettingsType.SNOW)) {
            Bukkit.getScheduler().scheduleSyncRepeatingTask(DartaAPI.getInstance(), () -> {
                VilonixNetwork.getParticleAPI().sendEffect(ParticleEffect.FIREWORKS_SPARK, gamer.getPlayer().getLocation().clone().add(15, 7.5, 15), 0.005f, 80);
            }, 0, 20L);
        }*/
        assert gamer != null;
        JoinMessage joinMessage = gamer.getJoinMessage();
        if (joinMessage == null || joinMessage.getId() == -1) {
            return;
        }
        if (gamer.getVersion().getVersion() < Version.MINECRAFT_1_12_2.getVersion()) {
            gamer.sendMessage("\n" + StringUtil.stringToCenter("§6Vilonix") + "\n" + StringUtil.stringToCenter("§cВаша версия Minecraft устарела.") + "\n" + StringUtil.stringToCenter("§aМы рекомендуем для игры версию §l1.12.2") + "\n§e\n");
        }

        if (joinMessage == JoinMessage.DEFAULT_MESSAGE) {
            if (gamer.isAngel()) {
                GAMER_MANAGER.getGamerEntities().values().forEach(
                        f -> f.sendMessage("§e>§a>§b> " + gamer.getDisplayName() + " §6зашел на сервер! §b<§a<§e<"));
                return;
            }
            GAMER_MANAGER.getGamerEntities().values()
                    .forEach(f -> f.sendMessage(gamer.getDisplayName() + " §6зашел на сервер!"));
            return;
        }
        GAMER_MANAGER.getGamerEntities().values().forEach(
                f -> f.sendMessage(String.format(joinMessage.getMessage(gamer), gamer.getDisplayName())));
    }
}
