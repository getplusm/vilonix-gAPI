package plusm.vilonix.gapi.utils.core;

import lombok.experimental.UtilityClass;
import plusm.vilonix.api.VilonixNetwork;
import plusm.vilonix.api.player.Spigot;
import plusm.vilonix.api.util.StringUtil;

import java.io.*;
import java.util.List;

@UtilityClass
public class LogUtil {

    private final Spigot SPIGOT = VilonixNetwork.getGamerManager().getSpigot();

    public void log(List<String> log) {
        try {
            File path = new File("/root/logs/" + SPIGOT.getName() + "/");
            if (!path.exists()) {
                path.mkdirs();
            }
            File file = new File("/root/logs/" + SPIGOT.getName() + "/log-"
                    + StringUtil.getDate().replace('/', '-') + ".txt");
            if (!file.isFile()) {
                file.createNewFile();
            }
            try (FileWriter fw = new FileWriter("/root/logs/" + SPIGOT.getName() + "/log-"
                    + StringUtil.getDate().replace('/', '-') + ".txt", true);
                 BufferedWriter bw = new BufferedWriter(fw);
                 PrintWriter pw = new PrintWriter(bw)) {
                log.forEach(pw::println);
            } catch (IOException ignored) {
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
