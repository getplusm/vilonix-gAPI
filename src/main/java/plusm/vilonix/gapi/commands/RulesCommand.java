package plusm.vilonix.gapi.commands;

import org.bukkit.entity.Player;
import plusm.vilonix.api.JSONMessageAPI;
import plusm.vilonix.api.VilonixNetwork;
import plusm.vilonix.api.command.CommandInterface;
import plusm.vilonix.api.player.BukkitGamer;
import plusm.vilonix.api.player.GamerEntity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class RulesCommand implements CommandInterface {

    private final JSONMessageAPI jsonMessageAPI = VilonixNetwork.getJsonMessageAPI();

    public RulesCommand() {
        COMMANDS_API.register("rules", this);
    }

    @Override
    public void execute(GamerEntity gamerEntity, String command, String[] args) {
        Player player = ((BukkitGamer) gamerEntity).getPlayer();
        String rules = "";

        try {

            URLConnection ver = new URL("https://forum.vilonix.ru/index.php?pages/rules/").openConnection();
            ver.setConnectTimeout(1200);
            try (BufferedReader in = new BufferedReader(new InputStreamReader(ver.getInputStream()))) {
                rules = in.readLine();
            }
        } catch (IOException ex) {
            gamerEntity.sendMessage("�c������ � ��������� �������� � ���������.\n������ �� ����� �� �������, �� �� ������ ��������� �� ���� - https://vk.cc/bVwvT8");
        }

        List<String> list = new ArrayList<>(Arrays.asList(rules.split("\n")));
        int page = 1;
        if (args.length != 0) {
            try {
                page = Math.abs(Integer.parseInt(args[0]));
            } catch (Exception e) {
                gamerEntity.sendMessage("�c������� ���������� �����!");
                return;
            }
            if (page == 0) {
                page = 1;
            }
        }
        final int pages = (int) Math.ceil(list.size() / 5);
        if (page > pages) {
            gamerEntity.sendMessage("�c�������� �� �������");
            return;
        }
        page = (page - 1) * 5;
        final StringBuilder sb = new StringBuilder();
        for (int p = page, i = 0; p < list.size() && i != 5; ++p, ++i) {
            sb.append("�f�����: &6").append(list.get(p).replace("-", ".")).append(" �8|�f ��������:�9 ").append(list.get(p).replace(".", "-")).append("\n");
        }
        gamerEntity.sendMessage("�6Vilonix �8> �f������ ������ ������ �������\n\n�������� " + (Math.ceil(page / 5) + 1) + " �� " + pages + "\n\n" + sb.toString());
    }
}
