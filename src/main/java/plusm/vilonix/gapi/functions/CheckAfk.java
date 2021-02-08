package plusm.vilonix.gapi.functions;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import plusm.vilonix.gapi.loader.DartaAPI;
import plusm.vilonix.gapi.utils.DListener;
import plusm.vilonix.gapi.utils.core.PlayerUtil;
import plusm.vilonix.api.TitleAPI;
import plusm.vilonix.api.VilonixNetwork;
import plusm.vilonix.api.game.GameSettings;
import plusm.vilonix.api.game.GameState;
import plusm.vilonix.api.game.MiniGameType;
import plusm.vilonix.api.player.BukkitGamer;
import plusm.vilonix.api.player.GamerManager;
import plusm.vilonix.api.sound.SoundAPI;
import plusm.vilonix.api.sound.SoundType;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CheckAfk extends DListener {

    private final Map<String, BukkitTask> afk = new ConcurrentHashMap<>();
    private final GamerManager gamerManager = VilonixNetwork.getGamerManager();
    private final SoundAPI soundAPI = VilonixNetwork.getSoundAPI();
    private final TitleAPI titleAPI = VilonixNetwork.getTitlesAPI();

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {
        if (GameSettings.minigame != MiniGameType.DEFAULT && GameSettings.minigame != MiniGameType.SURVIVAL) {
            Player player = e.getPlayer();
            String name = player.getName();
            BukkitGamer gamer = gamerManager.getGamer(player);
            if (gamer == null)
                return;
            if (e.getFrom().getBlockX() != e.getTo().getBlockX() || e.getFrom().getBlockY() != e.getTo().getBlockY() || e.getFrom().getBlockZ() != e.getTo().getBlockZ() || e.getFrom().getDirection() != e.getTo().getDirection()) {
                BukkitTask task = afk.remove(name);
                if (task != null)
                    task.cancel();
                return;
            }

            if (afk.containsKey(name))
                return;
            if (PlayerUtil.isSpectator(player))
                return;
            BukkitTask active = new BukkitRunnable() {
                int timeAFK = 0;
                int timeTitle = 0;

                @Override
                public void run() {
                    if (GameState.getCurrent() == GameState.GAME) {
                        if (!player.isOnline()) {
                            cancel();
                            afk.remove(name);
                        }
                        if (timeAFK >= 2700) {
                            soundAPI.play(player, SoundType.AFK_SOUND);
                            if (timeTitle >= 10) {
                                timeTitle = 0;
                                titleAPI.sendTitle(player, "§r", "§cÍו סעמי םא לוסעו!", 0, 3, 1);
                            }
                            timeTitle++;
                        }
                        timeAFK++;
                    }
                }
            }.runTaskTimer(DartaAPI.getInstance(), 1L, 1L);
            afk.put(name, active);
        }
    }
}