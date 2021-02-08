package plusm.vilonix.gapi.game.module;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import plusm.vilonix.api.VilonixNetwork;
import plusm.vilonix.api.event.game.ChangeGameStateEvent;
import plusm.vilonix.api.event.game.EndGameEvent;
import plusm.vilonix.api.event.game.PlayerKillEvent;
import plusm.vilonix.api.event.game.RestartGameEvent;
import plusm.vilonix.api.game.GameModeType;
import plusm.vilonix.api.game.GameState;
import plusm.vilonix.api.game.TeamManager;
import plusm.vilonix.api.player.BukkitGamer;
import plusm.vilonix.api.player.GamerManager;
import plusm.vilonix.gapi.game.GameManager;
import plusm.vilonix.gapi.game.ItemsListener;
import plusm.vilonix.gapi.game.depend.EndLobby;
import plusm.vilonix.gapi.game.spectator.SpectatorMenu;
import plusm.vilonix.gapi.game.team.SelectionTeam;
import plusm.vilonix.gapi.listeners.*;
import plusm.vilonix.gapi.utils.AntiCheatUtils;
import plusm.vilonix.gapi.utils.DListener;
import plusm.vilonix.gapi.utils.bukkit.BukkitUtil;
import plusm.vilonix.gapi.utils.core.CoreUtil;
import plusm.vilonix.gapi.utils.core.PlayerUtil;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("deprecation")
public class EndModule {

    private static final GamerManager GAMER_MANAGER = VilonixNetwork.getGamerManager();

    private final List<DListener> endListeners = new ArrayList<>();

    public EndModule() {
        this.endGame();
    }

    private void endGame() {
        VilonixNetwork.getScoreBoardAPI().unregisterObjectives();
        GameState.setCurrent(GameState.END);

        ChangeGameStateEvent changeGameStateEvent = new ChangeGameStateEvent(GameState.END);
        BukkitUtil.callEvent(changeGameStateEvent);

        endListeners.add(new BlockBreakListener());
        endListeners.add(new BlockPlaceListener());
        endListeners.add(new BlockPhysicsListener());
        endListeners.add(new DamageListener());
        endListeners.add(new DropListener());
        endListeners.add(new EntitySpawnListener());
        endListeners.add(new FoodListener());
        endListeners.add(new ItemSpawnListener());
        endListeners.add(new PhysicalListener());
        endListeners.add(new PickupListener());
        endListeners.add(new WeatherListener());
        StartModule.getGameListeners().forEach(DListener::unregisterListener);
        StartModule.getGameListeners().clear();
        StartModule.getPlayerInventory().stopPI();

        EndGameEvent endGameEvent = new EndGameEvent();
        BukkitUtil.callEvent(endGameEvent);

        WaitModule.getSelectionTeam().clear();
        SelectionTeam.getSelectedTeams().clear();

        BukkitUtil.runTask(() -> { //ставим гм и выдаем предметы
            for (Player player : Bukkit.getOnlinePlayers()) {
                BukkitGamer gamer = GAMER_MANAGER.getGamer(player);
                if (gamer == null)
                    continue;
                gamer.setGameMode(GameModeType.ADVENTURE);
                player.getInventory().setItem(8, ItemsListener.getHub(player));
                player.getInventory().setItem(4, ItemsListener.getStartAgain(player));
                player.closeInventory();
            }
        });

        endListeners.add(new EndLobby(endGameEvent));

        new Thread(() -> {
            try {
                Thread.sleep(10000);
                WaitModule.alertMessageAll("§cРестарт арены, через 5 секунд вы будете перемещены в лобби");
                Thread.sleep(5000);
                Bukkit.getOnlinePlayers().forEach(PlayerUtil::redirectToHub);
                Thread.sleep(5000);
                this.endListeners.forEach(DListener::unregisterListener);
                this.endListeners.clear();

                Bukkit.unloadWorld("endlobby", false);

                WaitModule.perkgui.clear();
                PlayerKillEvent.players.clear();
                SpectatorMenu.clearData();

                RestartGameEvent restartGameEvent = new RestartGameEvent();
                BukkitUtil.callEvent(restartGameEvent);

                TeamManager.getTeams().clear();

                BukkitUtil.runTask(AntiCheatUtils::unloadAndLoad);

                CoreUtil.restart();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }).start();
    }
}