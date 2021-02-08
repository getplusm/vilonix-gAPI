package plusm.vilonix.gapi.game;

import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import plusm.vilonix.api.game.GameSettings;
import plusm.vilonix.api.game.GameState;
import plusm.vilonix.api.game.MiniGameType;
import plusm.vilonix.api.player.BukkitGamer;
import plusm.vilonix.api.player.constans.SettingsType;
import plusm.vilonix.gapi.utils.DListener;
import plusm.vilonix.gapi.utils.bukkit.BukkitUtil;

import java.util.Random;

public class SettingsGameListener extends DListener {

    @EventHandler
    public void JoinPlayerEvent(PlayerJoinEvent e) {
        BukkitUtil.runTask(() -> {
            Player pl = e.getPlayer();
            BukkitGamer gamer = GAMER_MANAGER.getGamer(pl);

            assert gamer != null;
            if (gamer.isLite() && gamer.getSetting(SettingsType.FLY)) {
                pl.setAllowFlight(true);
                pl.setFlying(true);
            }

            if (GameSettings.minigame != MiniGameType.DEFAULT) {
                if (gamer.getSetting(SettingsType.MUSIC)) {
                    // TODO Сделать выбор музыки в донат меню
                    if (gamer.isAngel() && (GameState.getCurrent() == GameState.WAITING
                            || GameState.getCurrent() == GameState.STARTING)) {
                        Material sound = null;
                        int random = new Random().nextInt(8);
                        switch (random) {
                            case 0:
                                sound = Material.RECORD_3;
                                break;
                            case 1:
                                sound = Material.RECORD_4;
                                break;
                            case 2:
                                sound = Material.RECORD_5;
                                break;
                            case 3:
                                sound = Material.RECORD_6;
                                break;
                            case 4:
                                sound = Material.RECORD_7;
                                break;
                            case 5:
                                sound = Material.RECORD_8;
                                break;
                            case 6:
                                sound = Material.RECORD_9;
                                break;
                            case 7:
                                sound = Material.RECORD_10;
                                break;
                            case 8:
                                sound = Material.RECORD_12;
                                break;
                        }
                        pl.playEffect(pl.getLocation(), Effect.RECORD_PLAY, sound);
                    }
                }
            }
        });
    }
}
