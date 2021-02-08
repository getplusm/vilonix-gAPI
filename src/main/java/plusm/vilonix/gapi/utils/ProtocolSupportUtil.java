package plusm.vilonix.gapi.utils;

import lombok.experimental.UtilityClass;
import org.bukkit.entity.Player;
import plusm.vilonix.api.player.constans.Version;
import protocolsupport.api.ProtocolSupportAPI;

@UtilityClass
public class ProtocolSupportUtil {

    public static Version getVersion(Player player) {
        return Version.getVersion(ProtocolSupportAPI.getProtocolVersion(player).getId());
    }
}
