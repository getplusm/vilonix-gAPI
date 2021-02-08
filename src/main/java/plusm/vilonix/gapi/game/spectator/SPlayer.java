package plusm.vilonix.gapi.game.spectator;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import plusm.vilonix.api.VilonixNetwork;
import plusm.vilonix.api.game.GameModeType;
import plusm.vilonix.api.player.BukkitGamer;
import plusm.vilonix.api.player.GamerManager;
import plusm.vilonix.gapi.game.GameManager;
import plusm.vilonix.gapi.loader.DartaAPI;

import java.util.HashMap;

public class SPlayer {
    static final HashMap<String, SPlayer> sPlayers = new HashMap<>();
    private static final GamerManager GAMER_MANAGER = VilonixNetwork.getGamerManager();
    private int alwaysFly;
    private int hideSpectators;
    private Player nearPlayer;
    private Player player;
    private SpectatorSettings spectatorSettings;
    private int speedSpec;
    private int vision;

    private SPlayer(Player player) {
        this.player = player;
        BukkitGamer gamer = GAMER_MANAGER.getGamer(player);
        if (gamer == null)
            return;
        int PlayerID = gamer.getPlayerID();

        int[] data = GameManager.getInstance().getSpectatorLoaders().getStats(PlayerID);
        this.speedSpec = data[0];
        this.alwaysFly = data[1];
        this.hideSpectators = data[2];
        this.vision = data[3];

        spectatorSettings = new SpectatorSettings(this);

        sPlayers.put(player.getName(), this);
    }

    public static SPlayer getSPlayer(Player player) {
        if (sPlayers.get(player.getName()) == null) {
            return new SPlayer(player);
        }
        return sPlayers.get(player.getName());
    }

    public static void removeSPlayer(Player player) {
        sPlayers.remove(player.getName());
    }

    public int getAlwaysFly() {
        return this.alwaysFly;
    }

    public void setAlwaysFly(int fly) {
        if (fly == 1) {
            this.alwaysFly = 1;
        } else if (fly == 0) {
            this.alwaysFly = 0;
        }
    }

    public int getHideSpectators() {
        return this.hideSpectators;
    }

    public void setHideSpectators(int hide) {
        if (hide == 1) {
            this.hideSpectators = 1;
            for (Player p : Bukkit.getOnlinePlayers()) {
                if (p == getPlayer())
                    continue;
                if (GAMER_MANAGER.getGamer(p).getGameMode() == GameModeType.SPECTATOR)
                    getPlayer().hidePlayer(DartaAPI.getInstance(), p);
            }
        } else if (hide == 0) {
            this.hideSpectators = 0;
            for (Player p : Bukkit.getOnlinePlayers()) {
                if (p == getPlayer())
                    continue;
                if (GAMER_MANAGER.getGamer(p).getGameMode() == GameModeType.SPECTATOR)
                    getPlayer().showPlayer(DartaAPI.getInstance(), p);
            }
        }
    }

    public Player getNearPlayer() {
        return this.nearPlayer;
    }

    public void setNearPlayer(Player nearPlayer) {
        this.nearPlayer = nearPlayer;
    }

    public Player getPlayer() {
        return player;
    }

    public SpectatorSettings getSpectatorSettings() {
        return this.spectatorSettings;
    }

    public int getSpeedSpec() {
        return speedSpec;
    }

    public void setSpeedSpec(int speedSpec) {
        this.speedSpec = speedSpec;
    }

    public int getVision() {
        return vision;
    }

    public void setVision(int vision) {
        if (vision == 1) {
            this.vision = 1;
            player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, 0));
        } else if (vision == 0) {
            this.vision = 0;
            player.removePotionEffect(PotionEffectType.NIGHT_VISION);
        }
    }

    public void openInventory() {
        this.spectatorSettings.openInventory();
    }
}
