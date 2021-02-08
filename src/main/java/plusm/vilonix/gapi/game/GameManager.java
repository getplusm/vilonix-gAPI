package plusm.vilonix.gapi.game;

import plusm.vilonix.api.types.GameType;
import plusm.vilonix.gapi.game.module.StartModule;
import plusm.vilonix.gapi.loader.SpectatorLoader;
import plusm.vilonix.gapi.module.stats.Stats;
import plusm.vilonix.gapi.utils.DListener;

import java.util.ArrayList;
import java.util.List;

public abstract class GameManager {

    private static GameManager instance;
    private final SpectatorLoader spectatorLoader;
    private final Stats stats;

    protected GameManager() {
        instance = this;
        this.loadConfig();
        List<String> columns = new ArrayList<>();
        if (GameType.current != GameType.UNKNOWN)
            columns.add("Games");
        columns.addAll(this.getColumns());
        this.stats = new Stats(this.getTable(), columns);
        this.spectatorLoader = new SpectatorLoader();
    }

    public static GameManager getInstance() {
        return instance;
    }

    public void addGameListener(DListener listener) {
        StartModule.getGameListeners().add(listener);
    }

    protected abstract List<String> getColumns();

    public SpectatorLoader getSpectatorLoaders() {
        return spectatorLoader;
    }

    public Stats getStats() {
        return stats;
    }

    protected abstract String getTable();

    protected abstract void loadConfig();
}
