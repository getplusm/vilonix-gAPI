package plusm.vilonix.gapi.listeners;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;
import plusm.vilonix.gapi.utils.bukkit.BukkitUtil;
import plusm.vilonix.gapi.utils.core.PlayerUtil;
import plusm.vilonix.api.VilonixNetwork;
import plusm.vilonix.api.event.gamer.async.AsyncGamerChatFormatEvent;
import plusm.vilonix.api.player.BukkitGamer;
import plusm.vilonix.api.player.Spigot;
import plusm.vilonix.api.player.constans.SettingsType;
import plusm.vilonix.api.sound.SoundType;
import plusm.vilonix.api.util.ChatUtil;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public final class ChatListener extends DListener<JavaPlugin> {

    private final Spigot spigot = VilonixNetwork.getGamerManager().getSpigot();

    public ChatListener(JavaPlugin dartaAPI) {
        super(dartaAPI);
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onAsyncPlayerChat(AsyncPlayerChatEvent e) {
        Player player = e.getPlayer();

        BukkitGamer gamer = GAMER_MANAGER.getGamer(player);
        if (gamer == null) {
            return;
        }

        //String suffix = gamer.getGroup().getSuffix();

        String prefix = gamer.getPrefix();
        e.setFormat(" §r" + prefix + player.getName() + " %2$s");

        if (PlayerUtil.isSpectator(player)) {
            e.getRecipients().clear();
            e.setFormat(" §8[§4\u2716§8]" + e.getFormat());
            e.getRecipients().addAll(PlayerUtil.getSpectators());
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onChat(AsyncPlayerChatEvent e) {
        e.setCancelled(true);

        BukkitGamer sender = GAMER_MANAGER.getGamer(e.getPlayer());
        if (sender == null) {
            return;
        }

        Set<BukkitGamer> recipients = new HashSet<>();
        for (Player player : e.getRecipients()) {
            BukkitGamer otherGamer = GAMER_MANAGER.getGamer(player);
            if (otherGamer == null) {
                continue;
            }

            recipients.add(otherGamer);
        }

        AsyncGamerChatFormatEvent event = new AsyncGamerChatFormatEvent(
                sender,
                recipients,
                e.getFormat(),
                e.getMessage());
        BukkitUtil.callEvent(event);

        String message = event.getMessage();
        spigot.sendMessage(event.getBaseFormat().replace("%2$s", "") + message);
        for (BukkitGamer gamer : event.getRecipients()) {
            String eventFormat = event.getFormat(gamer).replace("%2$s", "");

            gamer.sendMessage(eventFormat + message);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onChatToPlayer(AsyncGamerChatFormatEvent e) {
        BukkitGamer gamerSender = e.getGamer();

        for (BukkitGamer gamer : new HashSet<>(e.getRecipients())) {
            if (gamer == gamerSender || !gamer.getSetting(SettingsType.CHAT)) {
                continue;
            }

            boolean nicknameFound = false;

            TextComponent finalComponent = new TextComponent(e.getFormat(gamer).replace("%2$s", ""));

            for (String word : e.getMessage().split(" ")) {
                if (!nicknameFound && word.equalsIgnoreCase(gamer.getName())) {
                    nicknameFound = true;

                    TextComponent component = new TextComponent(gamer.getName());
                    component.setColor(ChatColor.LIGHT_PURPLE);
                    component.setUnderlined(true);

                    component.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new BaseComponent[]{new TextComponent(ChatUtil.getComponentFromList(
                            Arrays.asList("§fНажмите сюда, чтобы написать", "§fигроку "+gamerSender.getDisplayName()+" §fв лс")))
                    }));

                    component.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/msg " + gamerSender.getName() + " "));

                    finalComponent.addExtra(component);

                } else {
                    TextComponent textComponent = new TextComponent(word);
                    finalComponent.addExtra(textComponent);
                }

                finalComponent.addExtra(" ");
            }

            if (nicknameFound) {
                e.removeRecipient(gamer);

                gamer.playSound(SoundType.LEVEL_UP, 0.6f, 0.2f);

                gamer.sendMessage(finalComponent);
            }
        }
    }
}