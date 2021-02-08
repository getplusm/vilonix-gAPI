package plusm.vilonix.gapi.utils.games;

import lombok.experimental.UtilityClass;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import plusm.vilonix.api.TitleAPI;
import plusm.vilonix.api.VilonixNetwork;
import plusm.vilonix.api.game.GameSettings;
import plusm.vilonix.api.player.GamerManager;
import plusm.vilonix.api.util.StringUtil;
import plusm.vilonix.gapi.loader.DartaAPI;
import plusm.vilonix.gapi.utils.bukkit.BukkitUtil;

import java.util.List;

@UtilityClass
public class TitleUtil {

    private static final GamerManager GAMER_MANAGER = VilonixNetwork.getGamerManager();
    private static final TitleAPI TITLE_API = VilonixNetwork.getTitlesAPI();

    public static String formatString(String str, int position) {
        final StringBuffer buffer = new StringBuffer(position);
        buffer.append(str);
        for (int i = 0; i < position; i++) {
            buffer.append(" ");
        }
        return buffer.toString();
    }

    private static void SendAnimationTitle(Player player, String subtitle) {
        if (!player.isOnline())
            return;
        List<String> list = StringUtil.getAnimationTitle("�����", "�c", "�e", 3);

        new BukkitRunnable() {
            int l = 0;

            @Override
            public void run() {
                TITLE_API.sendTitle(player, "�c�l" + list.get(l), subtitle, 0, 4 * 20, 20);
                this.l++;
                if (this.l == list.size()) {

                    this.cancel();
                }
            }
        }.runTaskTimer(DartaAPI.getInstance(), 2L, 2L);
    }

    public static void StartGameTitle(Player player, String subtitle) {
        String game = GameSettings.minigame.toString();
        String title = "�6�l" + game;
        String subTitle = (GameSettings.teamMode ? "�7��������� �����" : "�7��������� �����");

        TITLE_API.sendTitle(player, title, subTitle, 0, 10 * 20, 0);

        BukkitUtil.runTaskLaterAsync(60L, () -> SendAnimationTitle(player, subtitle));
    }

    public static void StartGameTitle(Player player, String subtitle, int time) {
        String title;
        String subTitle;

        if (time == 10) {
            title = "�6�l" + GameSettings.minigame.toString();
            subTitle = "�f����� ����������!";
            TITLE_API.sendTitle(player, title, subTitle, 0, 10 * 20, 0);
            return;
        }
        if (time == 0) {
            List<String> list = StringUtil.getAnimationTitle("�����", "�c", "�e", 6);
            BukkitUtil.runTaskAsync(() -> SendAnimationTitle(player, subtitle));
            list.clear();
            return;
        }

        title = "�6�l" + GameSettings.minigame.toString();
        subTitle = "�f�� ������ " + (time >= 3 ? "�a" : "") + (time == 2 ? "�e" : "")
                + (time == 1 ? "�c" : "") + StringUtil.getUTFNumber(time);
        TITLE_API.sendTitle(player, title, subTitle, 0, 22 * 20, 22 * 20);

    }

    public static void StartGameTitle(Player player, String subtitle, String type) {
        String game = GameSettings.minigame.toString();
        TITLE_API.sendTitle(player, "�6�l" + game, type, 0, 10 * 20, 0);

        BukkitUtil.runTaskLaterAsync(60L, () -> SendAnimationTitle(player, subtitle));
    }
}
