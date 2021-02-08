package plusm.vilonix.gapi.listeners;

import com.google.common.collect.ImmutableSet;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import plusm.vilonix.api.Schedulers;
import plusm.vilonix.api.VilonixNetwork;
import plusm.vilonix.api.event.gamer.async.AsyncGamerJoinEvent;
import plusm.vilonix.api.event.gamer.async.AsyncGamerLoadSectionEvent;
import plusm.vilonix.api.event.gamer.async.AsyncGamerPreLoginEvent;
import plusm.vilonix.api.event.gamer.async.AsyncGamerQuitEvent;
import plusm.vilonix.api.mAPI;
import plusm.vilonix.api.player.BukkitGamer;
import plusm.vilonix.api.player.GamerAPI;
import plusm.vilonix.api.player.GamerManager;
import plusm.vilonix.api.player.sections.*;
import plusm.vilonix.api.scoreboard.ScoreBoardAPI;
import plusm.vilonix.api.sql.GlobalLoader;
import plusm.vilonix.gapi.loader.DartaAPI;
import plusm.vilonix.gapi.utils.bukkit.BukkitUtil;
import plusm.vilonix.libraries.entity.BukkitGamerImpl;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public final class GamerListener extends DListener<DartaAPI> {

    private final GamerManager gamerManager = VilonixNetwork.getGamerManager();
    private final ScoreBoardAPI scoreBoardAPI = VilonixNetwork.getScoreBoardAPI();
    private final Map<String, BukkitGamerImpl> gamers = new ConcurrentHashMap<>();
    private final ImmutableSet<Class<? extends Section>> loadedSections = ImmutableSet.of(
            MoneySection.class,
            NetworkingSection.class,
            JoinMessageSection.class,
            FriendsSection.class,
            ReferralSection.class
    );

    public GamerListener(DartaAPI dartaAPI) {
        super(dartaAPI);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void loadData(AsyncPlayerPreLoginEvent e) {
        String name = e.getName();

        GamerAPI.removeOfflinePlayer(name);

        BukkitGamer gamer = null;
        try {
            gamer = new BukkitGamerImpl(e); //ñîçäàåì ãåéìåðà
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        if (gamer == null) {
            e.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, "§c[êèêíóò]\n§ferror-#3APPLEgN\n\n§fÑèñòåìà íå íàøëà ñåðâåðà äëÿ Âàñ\n\n§cÏÅÐÅÇÀÉÄÈÒÅ");
            return;
        }

        BukkitUtil.callEvent(new AsyncGamerPreLoginEvent(gamer, e));
    }

    @EventHandler
    public void onLoadSection(AsyncGamerLoadSectionEvent e) {
        e.setSections(loadedSections); // èíèöèàëèçèðóåì äîïîëíèòåëüíûå ñåêöèè êîòîðûå äîëæíû áûòü çàãðóæåíû
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onGlobalJoin(PlayerJoinEvent e) {
        e.setJoinMessage(null);
        Player player = e.getPlayer();

        BukkitGamer gamer = gamerManager.getGamer(player);
        if (gamer == null) {
            player.kickPlayer("§c[êèêíóò]\n§ferror-#2GMNotLoad\n\n§fÑèñòåìà íå íàøëà ñåðâåðà äëÿ Âàñ\n\n§cÏÅÐÅÇÀÉÄÈÒÅ");
            return;
        }
        Schedulers.async().run(() -> {
            if (!player.isOnline()) {
                return;
            }
            BukkitUtil.callEvent(new AsyncGamerJoinEvent(gamer));
        });
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onGlobalLogin(PlayerLoginEvent e) {
        if (e.getResult() != PlayerLoginEvent.Result.ALLOWED) {
            return;
        }
        Player player = e.getPlayer();
        BukkitGamerImpl gamer = gamers.remove(player.getName().toLowerCase());

        if (gamer == null || gamer.getPlayerID() != GlobalLoader.getPlayerID(player.getName())) {
            e.disallow(PlayerLoginEvent.Result.KICK_OTHER, "§c[êèêíóò]\n§ferror-#1AGLS\n\n§fÑèñòåìà íå íàøëà ñåðâåðà äëÿ Âàñ\n\n§cÏÅÐÅÇÀÉÄÈÒÅ");
            return;
        }

        gamer.setPlayer(player);
        player.setDisplayName(gamer.getDisplayName());
        gamer.setGroup(mAPI.getLuckPerms().getGroupManager().getGroup(Objects.requireNonNull(mAPI.getLuckPerms().getUserManager().getUser(player.getName())).getPrimaryGroup()));
        GamerAPI.addGamer(gamer);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onLoadGamer(AsyncGamerPreLoginEvent e) {
        BukkitGamerImpl gamer = (BukkitGamerImpl) e.getGamer();
        if (gamer == null)
            return;
        gamers.put(gamer.getName().toLowerCase(), gamer);
        System.out.println("§7Äàííûå èãðîêà §d" + gamer.getName() + " §8(§cID:" + gamer.getPlayerID() + "§8)§7 çàãðóæåíû çà §e"
                + (System.currentTimeMillis() - gamer.getStart()) + "ms\n§7Äîáàâëåíî ñåêöèé â ãåéìåðà: §a" + gamer.getSections().size());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onQuit(PlayerQuitEvent e) {
        e.setQuitMessage(null);
        Player player = e.getPlayer();

        scoreBoardAPI.removeDefaultTag(player);

        BukkitGamer gamer = gamerManager.getGamer(player);
        if (gamer != null) {
            Schedulers.async().run(() -> BukkitUtil.callEvent(new AsyncGamerQuitEvent(gamer)));
        }

        gamerManager.removeGamer(player);
    }
}
