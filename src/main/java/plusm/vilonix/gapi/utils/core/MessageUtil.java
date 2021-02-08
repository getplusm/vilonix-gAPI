package plusm.vilonix.gapi.utils.core;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import plusm.vilonix.api.VilonixNetwork;
import plusm.vilonix.api.game.GameSettings;
import plusm.vilonix.api.player.BukkitGamer;
import plusm.vilonix.api.player.GamerManager;
import plusm.vilonix.api.player.Spigot;
import plusm.vilonix.api.util.ConfigUtil;
import plusm.vilonix.api.util.StringUtil;

@Getter
public class MessageUtil {
    private static final GamerManager GAMER_MANAGER = VilonixNetwork.getGamerManager();
    private static final Spigot SPIGOT = VilonixNetwork.getGamerManager().getSpigot();

    public static void alertMsg(boolean gamePrefix, String text) {
        for (BukkitGamer gamer : GAMER_MANAGER.getGamers().values()) {
            Player player = gamer.getPlayer();
            if (player == null || !player.isOnline())
                continue;
            player.sendMessage((gamePrefix ? GameSettings.prefix : "") + ConfigUtil.getMessage(text));
        }
        broadcastConsole(gamePrefix, ConfigUtil.getMessage(text));
    }

    public static void alertDeath(boolean gamePrefix, String text, String playerName, String killer) {
        for (BukkitGamer gamer : GAMER_MANAGER.getGamers().values()) {
            Player player = gamer.getPlayer();
            if (player == null || !player.isOnline())
                continue;
            player.sendMessage((gamePrefix ? GameSettings.prefix : "") + ConfigUtil.getMessage(text, f -> f.replace("%player%", playerName).replace("%target%", killer)));
        }
        broadcastConsole(gamePrefix, ConfigUtil.getMessage(text, f -> f.replace("%player%", playerName).replace("%target%", killer)));
    }

    public static void broadcast(String text) {
        Bukkit.broadcastMessage(GameSettings.prefix + text);
    }

    private static void broadcastConsole(boolean prefix, String text) {
        SPIGOT.sendMessage((prefix ? GameSettings.prefix : "") + text);
    }

    public static void sendConsole(String text) {
        SPIGOT.sendMessage("§6Vilonix §8> §f" + text);
    }

    public static void sendConsoleError(String text) {
        SPIGOT.sendMessage("§6Vilonix §8> §c" + text);
    }

    public static void sendListToPlayers(String message, boolean center) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            BukkitGamer gamer = GAMER_MANAGER.getGamer(player);
            if (gamer == null)
                continue;
            for (String string : ConfigUtil.getList(message)) {
                if (center) {
                    player.sendMessage(StringUtil.stringToCenter(string));
                } else {
                    player.sendMessage(string);
                }
            }
        }
        for (String string : ConfigUtil.getList(message)) {
            MessageUtil.broadcastConsole(false, string);
        }
    }

    public static void sendMessage(Player player, String text) {
        player.sendMessage(GameSettings.prefix + text);
    }
}
