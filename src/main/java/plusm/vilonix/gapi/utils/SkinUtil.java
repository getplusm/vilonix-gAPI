package plusm.vilonix.gapi.utils;

import org.bukkit.entity.Player;
import plusm.vilonix.api.VilonixNetwork;
import plusm.vilonix.api.event.gamer.GamerChangeSkinEvent;
import plusm.vilonix.api.event.gamer.async.AsyncGamerSkinApplyEvent;
import plusm.vilonix.api.player.BukkitGamer;
import plusm.vilonix.api.player.GamerManager;
import plusm.vilonix.api.skin.Skin;
import plusm.vilonix.api.skin.SkinType;
import plusm.vilonix.gapi.utils.bukkit.BukkitUtil;
import plusm.vilonix.libraries.LibAPI;
import plusm.vilonix.libraries.entity.BukkitGamerImpl;
import plusm.vilonix.libraries.interfaces.NmsManager;

public class SkinUtil {

    private final static GamerManager GAMER_MANAGER = VilonixNetwork.getGamerManager();
    private final static NmsManager NMS_MANAGER = LibAPI.getManager();

    public static void setSkin(Player player, String skinName, String value, String signature, SkinType skinType) {
        BukkitGamer gamer = GAMER_MANAGER.getGamer(player);
        if (gamer == null || skinName == null || value == null || signature == null) {
            return;
        }

        /*SkinSection section = gamer.getSection(SkinSection.class);
        if (section == null) {
            return;
        }

        Skin skin = new Skin(skinName, value, signature, skinType, System.currentTimeMillis());
        section.setSkin(skin);
        ((BukkitGamerImpl) gamer).setHead(value);

        GamerChangeSkinEvent event = new GamerChangeSkinEvent(gamer, skin);
        BukkitUtil.runTask(() -> BukkitUtil.callEvent(event));

        AsyncGamerSkinApplyEvent skinApplyEvent = new AsyncGamerSkinApplyEvent(gamer);
        BukkitUtil.callEvent(skinApplyEvent);

        if (skinApplyEvent.isCancelled()) {
            VilonixNetwork.getGamerManager().getSpigot().sendMessage("skinapply is cancelled");
            return;
        }

        NMS_MANAGER.setSkin(player, value, signature);*/
    }

}