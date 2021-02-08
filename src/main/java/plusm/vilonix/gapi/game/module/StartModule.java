package plusm.vilonix.gapi.game.module;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import plusm.vilonix.api.VilonixNetwork;
import plusm.vilonix.api.event.game.ChangeGameStateEvent;
import plusm.vilonix.api.event.game.StartGameEvent;
import plusm.vilonix.api.game.GameSettings;
import plusm.vilonix.api.game.GameState;
import plusm.vilonix.api.game.TeamManager;
import plusm.vilonix.api.player.BukkitGamer;
import plusm.vilonix.api.player.GamerManager;
import plusm.vilonix.api.sound.SoundAPI;
import plusm.vilonix.api.sound.SoundType;
import plusm.vilonix.api.sql.GlobalLoader;
import plusm.vilonix.gapi.functions.CheckAfk;
import plusm.vilonix.gapi.functions.compass.CompassManager;
import plusm.vilonix.gapi.game.GameManager;
import plusm.vilonix.gapi.game.depend.DeathListener;
import plusm.vilonix.gapi.game.depend.GameListener;
import plusm.vilonix.gapi.game.perk.PerksGui;
import plusm.vilonix.gapi.game.perk.VanillaPerkListener;
import plusm.vilonix.gapi.game.spectator.SpectatorListener;
import plusm.vilonix.gapi.game.spectator.SpectatorMenu;
import plusm.vilonix.gapi.game.team.SelectionTeam;
import plusm.vilonix.gapi.guis.playerinventory.PlayerInventory;
import plusm.vilonix.gapi.guis.playerinventory.PlayerInventoryListener;
import plusm.vilonix.gapi.listeners.*;
import plusm.vilonix.gapi.module.stats.StatsPlayer;
import plusm.vilonix.gapi.utils.DListener;
import plusm.vilonix.gapi.utils.bukkit.BukkitUtil;
import plusm.vilonix.gapi.utils.core.PlayerUtil;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("deprecation")
public class StartModule {

    private static final GamerManager GAMER_MANAGER = VilonixNetwork.getGamerManager();
    private static final SoundAPI SOUND_API = VilonixNetwork.getSoundAPI();
    private static final List<DListener> gameListeners = new ArrayList<DListener>();
    private static PlayerInventory playerInventory;

    StartModule(WaitModule waitModule) {
        this.startGame();
        waitModule.listeners.forEach(DListener::unregisterListener);
        waitModule.listeners.clear();
    }

    public static List<DListener> getGameListeners() {
        return gameListeners;
    }

    static PlayerInventory getPlayerInventory() {
        return playerInventory;
    }

    private void setTeamsPlayers() {
        for (Player player : PlayerUtil.getAlivePlayers()) {
            TeamManager teamManager = SelectionTeam.getSelectedTeams().get(player);
            if (teamManager != null)
                continue;
            for (TeamManager team : TeamManager.getTeams().values()) {
                if (SelectionTeam.getPlayersInTeam(team) < GameSettings.playersInTeam) {
                    SelectionTeam.getSelectedTeams().put(player, team);
                    break;
                }
            }
        }
    }

    private void startGame() {
        GameState.setCurrent(GameState.GAME);
        ChangeGameStateEvent changeGameStateEvent = new ChangeGameStateEvent(GameState.GAME);
        BukkitUtil.callEvent(changeGameStateEvent);

        playerInventory = new PlayerInventory();
        playerInventory.startPI();

        gameListeners.add(new PlayerInventoryListener());
        gameListeners.add(new SpectatorListener());
        gameListeners.add(new CheckAfk());
        gameListeners.add(new GameListener());
        gameListeners.add(new VanillaPerkListener());
        SpectatorMenu.createMenu();

        if (!GameSettings.blockBreak) {
            gameListeners.add(new BlockBreakListener());
        }
        if (!GameSettings.blockPlace) {
            gameListeners.add(new BlockPlaceListener());
        }
        if (!GameSettings.blockPhysics) {
            gameListeners.add(new BlockPhysicsListener());
        }
        if (GameSettings.damage) {
            gameListeners.add(new DamageListener());
        }
        if (GameSettings.death) {
            gameListeners.add(new DeathListener());
        }
        if (!GameSettings.drop) {
            gameListeners.add(new DropListener());
        }
        if (!GameSettings.entitySpawn) {
            gameListeners.add(new EntitySpawnListener());
        }
        if (!GameSettings.fallDamage) {
            gameListeners.add(new FallListener());
        }
        if (!GameSettings.food) {
            gameListeners.add(new FoodListener());
        }
        if (!GameSettings.itemSpawn) {
            gameListeners.add(new ItemSpawnListener());
        }
        if (!GameSettings.physical) {
            gameListeners.add(new PhysicalListener());
        }
        if (!GameSettings.pickup) {
            gameListeners.add(new PickupListener());
        }
        if (!GameSettings.weather) {
            gameListeners.add(new WeatherListener());
        }
        if (GameSettings.TNTPrimed) {
            gameListeners.add(new TNTListener());
        }

        // Всех игроков распределить по тимам
        setTeamsPlayers();

        for (Player player : PlayerUtil.getAlivePlayers()) {
            GameManager.getInstance().getStats().createPlayerStats(player, false);
            StatsPlayer.getCachedPlayer(GlobalLoader.getPlayerID(player.getName())).addStats("Games", 1);
        }

        BukkitUtil.callEvent(new StartGameEvent(GameSettings.minigame));

        if (GameSettings.teamMode) {
            Bukkit.broadcastMessage("");
            WaitModule.alertMessageAll("§c§lВнимание! §cОбразование союзов из нескольких команд запрещено. Докладывайте о таких игроках на форум.");
            Bukkit.broadcastMessage("");
        }

        // активация перков
        for (PerksGui perksGui : WaitModule.perkgui.values()) {
            perksGui.apply();
            // PerkSaveEvent achievement = newlocale PerkSaveEvent(perksGui.getOwner(),
            // perksGui.getPerk());
            // BukkitUtil.callEvent(achievement);
        }

        for (Player player : PlayerUtil.getAlivePlayers()) {
            BukkitGamer gamer = GAMER_MANAGER.getGamer(player);
            if (gamer == null)
                continue;
            gamer.setGameMode(GameSettings.gamemode);
            SOUND_API.play(player, SoundType.LEVEL_UP);
            player.setAllowFlight(false);
            player.setFlying(false);
            CompassManager.createCompass(player);
        }
    }
}
