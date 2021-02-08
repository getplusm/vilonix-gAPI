package plusm.vilonix.gapi.listeners;

import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.scheduler.BukkitRunnable;
import plusm.vilonix.api.VilonixNetwork;
import plusm.vilonix.api.effect.ParticleAPI;
import plusm.vilonix.api.event.gamer.GamerFriendEvent;
import plusm.vilonix.api.event.gamer.GamerLvlUpEvent;
import plusm.vilonix.api.player.BukkitGamer;
import plusm.vilonix.api.player.sections.FriendsSection;
import plusm.vilonix.api.util.StringUtil;
import plusm.vilonix.gapi.loader.DartaAPI;

import java.util.List;

public final class NetworkingListener extends DListener<DartaAPI> {

    private final ParticleAPI particleAPI = VilonixNetwork.getParticleAPI();

    public NetworkingListener(DartaAPI dartaAPI) {
        super(dartaAPI);
    }

    /*
     * @EventHandler public void onCoreSound(CoreInputPacketEvent event) {
     * if(event.getPacket() instanceof BukkitPlaySound) { BukkitPlaySound packet =
     * (BukkitPlaySound) event.getPacket(); SoundType sound =
     * SoundType.values()[packet.getSoundType()];
     *
     * if (!packet.isTargeted() && !LastCraft.isGame()) { for (Player player :
     * Bukkit.getOnlinePlayers()) { soundAPI.play(player, sound, packet.getVolume(),
     * packet.getPitch()); } return; }
     *
     * BukkitGamer gamer = GAMER_MANAGER.getGamer(packet.getPlayerId()); if (gamer
     * == null) { return; }
     *
     * soundAPI.play(gamer.getPlayer(), sound, packet.getVolume(),
     * packet.getPitch()); } }
     */

    @EventHandler(priority = EventPriority.LOWEST)
    public void onChangePlayerFriends(GamerFriendEvent e) { // добавить/удалить новых друзей в мапу
        BukkitGamer gamer = e.getGamer();

        FriendsSection section = gamer.getSection(FriendsSection.class);
        if (section != null) {
            section.changeFriend(e.getAction(), e.getFriend());
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onLvlUp(GamerLvlUpEvent e) {
        BukkitGamer gamer = e.getGamer();

        sendEffect(gamer);

        List<String> l = StringUtil.createWriter("УРОВЕНЬ ПОВЫШЕН", "_", 12, true);
        new BukkitRunnable() {
            int i = 0;

            @Override
            public void run() {
                VilonixNetwork.getTitlesAPI().sendTitle(gamer.getPlayer(), "§6§l" + l.get(i), "§8[§c" + StringUtil.getNumberFormat(e.getLevel() - 1) + "§8] §7\u21a0§8 [§a" + StringUtil.getNumberFormat(e.getLevel()) + "§8]", 0, 60, 0);
                this.i++;
                if (this.i == l.size()) {
                    this.cancel();
                    this.i = 0;
                }
            }
        }.runTaskTimer(javaPlugin, 1, 1);
        gamer.sendMessage("");
        gamer.sendMessage("§7\u250f\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501");
        gamer.sendMessage("§7\u2503" + StringUtil.stringToCenter("§a§l§kX§r §6§lУРОВЕНЬ ПОВЫШЕН §a§l§kXx§r")); //'§a§l§kX§r §6§lУРОВЕНЬ ПОВЫШЕН §a§l§kX§r'
        gamer.sendMessage("§7\u2503");
        gamer.sendMessage("§7\u2503" + StringUtil.stringToCenter("§fВы достигли §d" + StringUtil.getNumberFormat(e.getLevel()) + "§f уровня!")); //'§fВы достигли §d%s§f уровня!'
        gamer.sendMessage("§7\u2503" + StringUtil.stringToCenter("§fДо следующего уровня нужно §a" + StringUtil.getNumberFormat(e.getExpNextLevel()) + "§f опыта")); //'§fДо следующего уровня нужно §a%s§f опыта'
        gamer.sendMessage("§7\u2503");
        gamer.sendMessage("§7\u2503" + StringUtil.stringToCenter("§7Зайдите в профиль, чтобы получить свою награду")); //'§7Зайдите в профиль, чтобы получить свою награду'
        gamer.sendMessage("§7\u250f\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501");
        gamer.sendMessage("");
    }

    private void sendEffect(BukkitGamer gamer) {
        gamer.playSound(Sound.UI_TOAST_CHALLENGE_COMPLETE);
        Player player = gamer.getPlayer();
        if (player == null) {
            return;
        }
        particleAPI.launchInstantFirework(
                FireworkEffect.builder().with(FireworkEffect.Type.BALL).withColor(Color.WHITE, Color.PURPLE).build(),
                player.getLocation().clone().add(0.0, 0.75, 0.0));
    }
}
