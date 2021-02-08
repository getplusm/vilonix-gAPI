package plusm.vilonix.gapi.module.achievement.manager;

import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;
import plusm.vilonix.gapi.module.achievement.achievement.Achievement;
import plusm.vilonix.gapi.module.achievement.listeners.AchievementPlayerListener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Getter
public final class AchievementManager {

    private final AchievementPlayerManager playerManager = new AchievementPlayerManager();

    private final Map<Integer, Achievement> achievements = new ConcurrentHashMap<>();

    private final JavaPlugin javaPlugin;
    private final AchievementSql achievementSql;
    private final String texture = "textures/blocks/log_spruce.png"; //todo сделать сеттинг этой херни

    private boolean loadOnJoin;

    public AchievementManager(JavaPlugin javaPlugin, String dataBase) {
        this.javaPlugin = javaPlugin;
        achievementSql = new AchievementSql(this, dataBase);

        new AchievementPlayerListener(playerManager, this, javaPlugin);
    }

    public AchievementPlayerManager getPlayerManager() {
        return playerManager;
    }

    public String getTexture() {
        return texture;
    }

    public boolean isLoadOnJoin() {
        return loadOnJoin;
    }

    public AchievementSql getAchievementSql() {
        return achievementSql;
    }

    public void setLoadOnJoin(boolean loadOnJoin) {
        this.loadOnJoin = loadOnJoin;
    }

    public void addAchievements(List<Achievement> list) {
        list.forEach(achievement -> achievements.putIfAbsent(achievement.getId(), achievement));
    }

    public void addAchievements(Achievement achievement) {
        achievements.putIfAbsent(achievement.getId(), achievement);
    }

    public int getAllPoints() {
        return (int) achievements.values()
                .stream()
                .map(Achievement::getPoints)
                .count();
    }

    public Map<Integer, Achievement> getAchievements() {
        return new HashMap<>(achievements);
    }

    public  <T extends Achievement> T getAchievement(int id) {
        return (T) achievements.get(id);
    }

}
