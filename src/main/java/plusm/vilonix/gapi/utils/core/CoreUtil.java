package plusm.vilonix.gapi.utils.core;

import lombok.Getter;
import lombok.experimental.UtilityClass;
import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import plusm.vilonix.gapi.commands.MoneyCommand;
import plusm.vilonix.gapi.gamemodes.GameModeScoreBoardTeam;
import plusm.vilonix.gapi.loader.DartaAPI;
import plusm.vilonix.gapi.utils.bukkit.BukkitUtil;
import plusm.vilonix.api.VilonixNetwork;
import plusm.vilonix.api.event.game.*;
import plusm.vilonix.api.game.GameState;
import plusm.vilonix.api.player.BukkitGamer;
import plusm.vilonix.api.player.GamerAPI;
import plusm.vilonix.api.player.GamerManager;
import plusm.vilonix.api.scoreboard.ScoreBoardAPI;
import plusm.vilonix.gapi.listeners.*;
import plusm.vilonix.gapi.utils.bukkit.CheckMemory;
import plusm.vilonix.gapi.utils.bukkit.WorldTime;

import java.io.File;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

@UtilityClass
@Getter
public class CoreUtil {

    private final static GamerManager GAMER_MANAGER = VilonixNetwork.getGamerManager();
    private final static ScoreBoardAPI SCORE_BOARD_API = VilonixNetwork.getScoreBoardAPI();
    public static boolean restart = false;

    public static String getConfigDirectory() {
        return "/home/config/" + GAMER_MANAGER.getSpigot().getName().split("-")[0];
    }

    public static String getGameWorld() {
        String world = null;
        File folder = new File("/home/vilonix/" + GAMER_MANAGER.getSpigot().getName());
        File[] listOfFiles = folder.listFiles();
        assert listOfFiles != null;
        for (File file : listOfFiles) {
            if (file.isDirectory()) {
                if (!file.getName().equals("logs")
                        && !file.getName().equals("lobby")
                        && !file.getName().equals("plugins")
                        && !file.getName().equals("timings")
                        && !file.getName().equals("endlobby")) {
                    world = file.getName();
                }
            }
        }
        return world;
    }

    public static String getRandom(Collection<String> e) {
        return e.stream().skip((int) (e.size() * Math.random())).findFirst().orElse("limbo-1");
    }

    public static String getServerDirectory() {
        return "/home/vilonix/" +
                GAMER_MANAGER.getSpigot().getName().split("-")[0]
                + "/" +
                GAMER_MANAGER.getSpigot().getName();
    }

    public static void registerGame(String mapName) {
        registerGame(mapName, true);
    }

    public static void registerGame(String mapName, boolean alwaysDay) {
        new MoneyCommand();
        //new ItemsListener();
        //new WaitModule();
        //new LoginListener();
        new ChunkListener();
        new InventoryListener();

        new WorldListener();

        WorldTime.addWorld("lobby", 6000);
        if (alwaysDay)
            WorldTime.addWorld(getGameWorld(), 6000);
        new CheckMemory();
    }

    public static void registerLobby(int time) {
        new MoneyCommand();
        new JoinListener(DartaAPI.getInstance());
        new LobbyGuardListener();
        new BlockPhysicsListener();
        new PlayerInteractListener();
        new DamageListener();
        new EntitySpawnListener();
        new ItemSpawnListener();
        new PhysicalListener();
        new WeatherListener();
        new ChunkListener();

        new WorldListener();

        WorldTime.addWorld("lobby", time);
    }

    public static void restart() {
        BukkitUtil.runTask(() -> {
            Bukkit.getOnlinePlayers().forEach(player -> {
                BukkitGamer gamer = GAMER_MANAGER.getGamer(player);
                if (gamer != null)
                    player.kickPlayer("§6Vilonix §8> §cОшибка при телепортации в лобби");
            });

            try {
                String world = getGameWorld();
                if (!Bukkit.unloadWorld(world, false) || restart) {
                    Bukkit.shutdown();
                    return;
                }
                FileUtils.cleanDirectory(new File("lobby", "playerdata"));
                FileUtils.cleanDirectory(new File(getGameWorld(), "playerdata"));
                GameModeScoreBoardTeam.resetBoard();

                SCORE_BOARD_API.removeDefaultTags();

                GamerAPI.clearGamers();

                //GameManager.getInstance().getClass().newInstance();
                //new WaitModule();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        GameState.setCurrent(GameState.WAITING);
        ChangeGameStateEvent changeGameStateEvent = new ChangeGameStateEvent(GameState.WAITING);
        BukkitUtil.callEvent(changeGameStateEvent);
    }

    public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map) {
        return map.entrySet().stream().sorted(Map.Entry.comparingByValue(Collections.reverseOrder()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
    }
}
