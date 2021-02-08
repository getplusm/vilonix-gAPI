package plusm.vilonix.gapi.game.depend;

import io.netty.util.internal.ConcurrentSet;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import plusm.vilonix.api.game.GameSettings;
import plusm.vilonix.api.game.GameState;
import plusm.vilonix.api.player.BukkitGamer;
import plusm.vilonix.api.player.GamerAPI;
import plusm.vilonix.gapi.game.module.WaitModule;
import plusm.vilonix.gapi.utils.DListener;
import plusm.vilonix.gapi.utils.bukkit.BukkitUtil;
import plusm.vilonix.gapi.utils.core.PlayerUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@Getter
public class LoginListener extends DListener {

    private static final Set<Player> players = new ConcurrentSet<>();

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        players.add(e.getPlayer());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerLogin(PlayerLoginEvent e) {
        if (e.getResult() != PlayerLoginEvent.Result.ALLOWED)
            return;

        Player player = e.getPlayer();
        BukkitGamer gamer = GAMER_MANAGER.getGamer(player);
        if (gamer == null)
            return;

        if (GameState.WAITING == GameState.getCurrent() || GameState.STARTING == GameState.getCurrent()) {
            if (WaitModule.getTime() >= 4 || !WaitModule.isStarting()) {
                List<Player> players = new ArrayList<>(LoginListener.players);
                if (players.size() >= GameSettings.slots) {
                    if (gamer.isPrimo()) {
                        Collections.shuffle(players);
                        for (Player all : players) {
                            if (all == player)
                                continue;
                            if (!GAMER_MANAGER.getGamer(all).isDonater()) {
                                LoginListener.players.remove(all);
                                all.sendMessage("§cПростите, но вы были перемещены в лобби, ваше место занял §r" + gamer.getDisplayName());
                                PlayerUtil.redirectToHub(all);
                                BukkitUtil.runTaskLater(20, () -> all.kickPlayer("§cОшибка телепортации в лобби"));
                                return;
                            }
                        }
                        e.disallow(PlayerLoginEvent.Result.KICK_FULL, "§cОшибка, на арене только донатеры");
                        GamerAPI.removeGamer(gamer);
                    } else {
                        e.disallow(PlayerLoginEvent.Result.KICK_FULL, "§cАрена заполнена, не хватает слотов? Купите §7§lPRIMO§c или выше");
                        GamerAPI.removeGamer(gamer);
                    }
                }
            } else {
                e.disallow(PlayerLoginEvent.Result.KICK_OTHER, "§cИгра начинается...");
            }
        } else if (GameState.GAME == GameState.getCurrent()
                && System.currentTimeMillis() - GameState.getGameTime() <= 5000) {
            e.disallow(PlayerLoginEvent.Result.KICK_OTHER, "§cИгра начинается...");
            GamerAPI.removeGamer(gamer);
        } else if (GameState.END == GameState.getCurrent()) {
            e.disallow(PlayerLoginEvent.Result.KICK_OTHER, "§cСервер перезагружается...");
            GamerAPI.removeGamer(gamer);
        }

    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        players.remove(e.getPlayer());
    }
}