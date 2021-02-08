package plusm.vilonix.gapi.loader;

import com.sun.istack.internal.NotNull;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import plusm.vilonix.api.VilonixNetwork;
import plusm.vilonix.api.player.BukkitGamer;
import plusm.vilonix.api.player.constans.PurchaseType;
import plusm.vilonix.api.util.StringUtil;

public class VilonixPAPI extends PlaceholderExpansion {

    @Override
    public String getIdentifier() {
        return "mapi";
    }

    @Override
    public String getAuthor() {
        return "plusm";
    }

    @Override
    public String getVersion() {
        return DartaAPI.getInstance().getDescription().getVersion();
    }

    @Override
    public String getName() {
        return "mAPI";
    }

    @Override
    public boolean register() {
        return super.register();
    }

    @Override
    public String onPlaceholderRequest(final Player p, final @NotNull String identifier) {
        if (p == null) {
            return "";
        }
        BukkitGamer gamer = VilonixNetwork.getGamerManager().getGamer(p);
        assert gamer != null;
        switch (identifier.toLowerCase()) {
            case "rubles":
                return StringUtil.getNumberFormat(gamer.getMoney(PurchaseType.VILONIX));
            case "money":
                return StringUtil.getNumberFormat(gamer.getMoney(PurchaseType.MONEY));
            case "level":
                return StringUtil.getNumberFormat(gamer.getLevelNetwork());
            case "exp_next":
                return StringUtil.getNumberFormat(gamer.getExpNextLevel());
            case "level_bar":
                return StringUtil.onPercentBar(gamer.getExp(), gamer.getExpNextLevel());
        }
        return null;
    }
}
