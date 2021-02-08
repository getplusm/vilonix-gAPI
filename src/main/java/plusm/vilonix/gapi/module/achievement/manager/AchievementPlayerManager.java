package plusm.vilonix.gapi.module.achievement.manager;

import org.bukkit.entity.Player;
import plusm.vilonix.api.VilonixNetwork;
import plusm.vilonix.api.player.BukkitGamer;
import plusm.vilonix.api.player.GamerManager;
import plusm.vilonix.gapi.module.achievement.achievement.AchievementPlayer;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class AchievementPlayerManager {

    private final Map<String, AchievementPlayer> achievementPlayers = new ConcurrentHashMap<>();
    private final GamerManager gamerManager = VilonixNetwork.getGamerManager();

    public AchievementPlayer getAchievementPlayer(int playerID) {
        BukkitGamer gamer = gamerManager.getGamer(playerID);
        if (gamer != null)
            return getAchievementPlayer(gamer.getName());

        return null;
    }

    public AchievementPlayer getAchievementPlayer(String name) {
        return achievementPlayers.get(name.toLowerCase());
    }

    public AchievementPlayer getAchievementPlayer(Player player) {
        return getAchievementPlayer(player.getName());
    }

    public AchievementPlayer getOrCreateAchievementPlayer(BukkitGamer gamer, AchievementManager achievementManager) {
        AchievementPlayer achievementPlayer = getAchievementPlayer(gamer.getName());
        if (achievementPlayer != null) {
            return achievementPlayer;
        }

        achievementPlayer = new AchievementPlayer(gamer, achievementManager);
        addAchievementPlayer(achievementPlayer);
        return achievementPlayer;
    }

    public boolean contains(String name) {
        return achievementPlayers.containsKey(name.toLowerCase());
    }

    public void addAchievementPlayer(AchievementPlayer achievementPlayer) {
        if (achievementPlayer == null)
            return;

        String name = achievementPlayer.getGamer().getName().toLowerCase();
        if (achievementPlayers.containsKey(name))
            return;

        achievementPlayers.put(name, achievementPlayer);
    }

    public Map<String, AchievementPlayer> getAchievementPlayers() {
        return new HashMap<>(achievementPlayers);
    }

    public void removeAchievementPlayer(String name) {
        achievementPlayers.remove(name.toLowerCase());
    }
}
