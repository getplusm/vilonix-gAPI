package plusm.vilonix.gapi.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import plusm.vilonix.api.event.DEvent;
import plusm.vilonix.api.mAPI;
import plusm.vilonix.gapi.utils.bukkit.BukkitUtil;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Getter
@AllArgsConstructor
public class ServerStartEvent extends DEvent {

    public static Map<String, Boolean> bukkitStartUp = new ConcurrentHashMap<>();
    String serverName;
    String ip;
    int port;

    public void thread() {
        BukkitUtil.runTaskLater(20 * 120, () -> {
            if (!bukkitStartUp.isEmpty()) {
                mAPI.getInstance().getLogger().warning("§cПлазмер не ответил, вырубаю нахуй");
                mAPI.getInstance().getServer().shutdown();
            }
        });
    }

}
