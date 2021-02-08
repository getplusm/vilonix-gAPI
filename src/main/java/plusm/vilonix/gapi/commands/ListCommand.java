package plusm.vilonix.gapi.commands;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import plusm.vilonix.api.VilonixNetwork;
import plusm.vilonix.api.command.CommandInterface;
import plusm.vilonix.api.player.BukkitGamer;
import plusm.vilonix.api.player.GamerEntity;
import plusm.vilonix.api.player.GamerManager;
import plusm.vilonix.api.types.GameType;
import plusm.vilonix.api.util.StringUtil;

import java.util.Arrays;

public final class ListCommand implements CommandInterface {

    private final GamerManager GAMER_MANAGER = VilonixNetwork.getGamerManager();

    public ListCommand() {
        COMMANDS_API.register("list", this, "список", "игроки", "players");
    }

    @Override
    public void execute(GamerEntity gamerEntity, String command, String[] strings) {
        StringBuilder best = new StringBuilder();
        StringBuilder players = new StringBuilder();
        StringBuilder donate = new StringBuilder();
        StringBuilder staff = new StringBuilder();

        int online = Bukkit.getOnlinePlayers().size();
        for (Player player : Bukkit.getOnlinePlayers()) {
            BukkitGamer gamer = GAMER_MANAGER.getGamer(player);
            if (gamer == null)
                continue;
            String displayName = player.getDisplayName();
            if (gamer.isStaff()) {
                staff.append(displayName).append("§f, ");
                continue;
            }
            if (!gamer.isDonater()) {
                players.append(displayName).append("§f, ");
                continue;
            }
            if (gamer.isDemon() || gamer.isYouTube()) {
                best.append(displayName).append("§f, ");
                continue;
            }
            donate.append(displayName).append("§f, ");
        }
        gamerEntity.sendMessages(Arrays.asList("",
                "§9Cервер §8> §fНа режиме §a" + online + " §f" + StringUtil.getCorrectWord(online, "PLAYERS_1"),
                " §a\u2022 §fКрутые ребята: " + correct(best),
                " §a\u2022 §fИгроки: " + correct(players),
                " §a\u2022 §fДонатеры: " + correct(donate),
                " §a\u2022 §fАдминистрация: " + correct(staff),
                ""));

    }

    private String correct(StringBuilder stringBuilder) {
        String list = String.valueOf(stringBuilder);
        if (list.length() == 0)
            return "§cНикого нет";

        return list.substring(0, list.length() - 4);
    }
}
