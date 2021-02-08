package plusm.vilonix.gapi.functions.compass;

import org.bukkit.entity.Player;
import plusm.vilonix.api.ActionBarAPI;
import plusm.vilonix.api.VilonixNetwork;
import plusm.vilonix.api.player.GamerManager;
import plusm.vilonix.api.util.LocationUtil;
import plusm.vilonix.gapi.game.ItemsListener;
import plusm.vilonix.gapi.utils.core.PlayerUtil;
import plusm.vilonix.gapi.utils.inventory.ItemUtil;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class CompassTask extends Thread {

    private static final ActionBarAPI ACTION_BAR_API = VilonixNetwork.getActionBarAPI();
    private static final GamerManager GAMER_MANAGER = VilonixNetwork.getGamerManager();

    CompassTask() {
        start();
    }

    public void run() {
        while (!Thread.interrupted()) {
            try {
                Thread.sleep(250);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            for (Compass compass : CompassManager.getCompasses()) {
                Player player = compass.getPlayer();
                if (PlayerUtil.isAlive(player)) {
                    Player currentPlayer = compass.getCurrentPlayer();
                    if (currentPlayer != null)
                        player.setCompassTarget(currentPlayer.getLocation());

                    if (ItemUtil.compareItems(player.getInventory().getItemInMainHand(), ItemsListener.getCompass())) {
                        if (currentPlayer != null) {
                            String message = "§fИгрок: " + currentPlayer.getDisplayName() + "      §fРасстояние: ";
                            double distance = LocationUtil.distance(player.getLocation(), currentPlayer.getLocation());
                            if (distance == -1) {
                                message += "§cДругой мир";
                            } else {
                                BigDecimal bd = new BigDecimal(distance);
                                bd = bd.setScale(1, RoundingMode.HALF_UP);
                                message += "§a" + bd.doubleValue();
                            }
                            ACTION_BAR_API.sendBar(player, message);
                        } else {
                            ACTION_BAR_API.sendBar(player, "§cНет живых игроков");
                        }
                    }
                } else {
                    CompassManager.removeCompass(player);
                }

            }
        }

    }

}
