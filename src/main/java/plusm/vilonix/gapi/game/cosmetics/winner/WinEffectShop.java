package plusm.vilonix.gapi.game.cosmetics.winner;

/*import org.bukkit.entity.Player;
import plusm.enetwork.api.inventory.DItem;
import plusm.enetwork.base.SoundType;
import plusm.enetwork.base.localizations.Language;
import plusm.enetwork.serverapi.guis.CustomItems;
import plusm.enetwork.serverapi.guis.shop.Shop;

import java.util.ArrayList;
import java.util.List;

public class WinEffectShop extends Shop {

    public WinEffectShop(Player player) {
        super(player, WinEffect.getWinEffects());
        for (int i = 0; i < pages.size(); ++i) {
            this.editPage(i, 4 * 9 + 4, new DItem(CustomItems.getBack(Language.RUSSIAN), (player1, clickType, slot) -> {
                SOUND_API.play(player, SoundType.PICKUP);
            }));
        }
    }

    @Override
    protected String getErrorMessage() {
        return "ты шо ебобо";
    }

    @Override
    protected String getInventoryName(int page) {
        String name = "Магазин эффектов ▸ Победа";
        if (page > 0) {
            name += " | " + (page + 1);
        }
        return name;
    }

    @Override
    protected int getRows(int page) {
        return 5;
    }

    @Override
    protected List<Integer> getSlots() {
        List<Integer> slots = new ArrayList<>();
        for (int i = 1; i <= 3; ++i) {
            for (int j = 1; j <= 7; ++j) {
                slots.add(i * 9 + j);
            }
        }
        return slots;
    }
}
*/