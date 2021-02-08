package plusm.vilonix.gapi.game.cosmetics.winner;

/*import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import plusm.enetwork.api.eNetwork;
import plusm.enetwork.api.game.cosmetics.CosmeticItem;
import plusm.enetwork.api.player.BukkitGamer;
import plusm.enetwork.api.player.GamerManager;
import plusm.enetwork.api.shop.BuyableCoins;
import plusm.enetwork.api.shop.Choosable;
import plusm.enetwork.api.shop.Shopable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class WinEffect implements BuyableCoins, CosmeticItem, Choosable, Shopable {

    private static final GamerManager GAMER_MANAGER = eNetwork.getGamerManager();

    private static List<WinEffect> winEffects = new ArrayList<>();
    public ItemStack icon;
    private int cost;
    private int id = 0;
    private Consumer<Player> winner;

    public WinEffect(int cost, ItemStack icon, Consumer<Player> winner) {
        id = winEffects.size();
        winEffects.add(this);
        this.cost = cost;
        this.icon = icon;
        this.winner = winner;
    }

    public static List<WinEffect> getWinEffects() {
        return winEffects;
    }

    @Override
    public void buy(String playerName) {
    }

    @Override
    public String canBuy(String playerName) {
        BukkitGamer gamer = GAMER_MANAGER.getGamer(playerName);
        if (gamer.getLevelNetwork() < 30 && !gamer.isNinja()) {
            return "§cДля покупки данного эффекта вам нужен §d30 уровень §cили выше!";
        }
        return null;
    }

    @Override
    public void choose(String playerName) {
    }

    @Override
    public int cost(String playerName) {
        BukkitGamer gamer = GAMER_MANAGER.getGamer(playerName);
        double multiplier = 1.0;
        if (gamer.isHero()) {
            multiplier = 0.8;
        }
        if (gamer.isSuperStar()) {
            multiplier = 0.65;
        }
        return (int) (cost * multiplier);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null)
            return false;
        if (o == this)
            return true;
        if (!(o instanceof CosmeticItem))
            return false;
        CosmeticItem item = (CosmeticItem) o;
        return item.getType() == this.getType() && item.getId() == this.getId();
    }

    @Override
    public ItemStack getIcon(String playerName) {
        return icon;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public int getType() {
        return 0;
    }

    @Override
    public int hashCode() {
        return getType() * 31 + this.getId();
    }

    @Override
    public boolean have(String playerName) {
        return false;
    }

    @Override
    public boolean isChoosed(String playerName) {
        return false;
    }

    public void use(Player player) {
        winner.accept(player);
    }
}
*/