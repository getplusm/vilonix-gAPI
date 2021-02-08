package plusm.vilonix.gapi.listeners;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import com.vk.api.sdk.objects.messages.*;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.plugin.messaging.PluginMessageListener;
import plusm.vilonix.api.mAPI;
import plusm.vilonix.gapi.event.ServerStartEvent;
import plusm.vilonix.gapi.loader.DartaAPI;
import plusm.vilonix.gapi.utils.core.MathUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class BungeeMessageListener extends DListener<DartaAPI> implements PluginMessageListener {

    private static GroupActor actor;
    private static VkApiClient client;

    public BungeeMessageListener(DartaAPI dartaAPI) {
        super(dartaAPI);

        //Bukkit.getMessenger().registerIncomingPluginChannel(dartaAPI, "Skins", this);
        initOutgoingChannel("BungeeCord");
        //getServer().getMessenger().registerIncomingPluginChannel( javaPlugin, "BukkitStartup", this );
    }

    private void initOutgoingChannel(String name) {
        Bukkit.getMessenger().registerOutgoingPluginChannel(javaPlugin, name);
        Bukkit.getMessenger().registerIncomingPluginChannel(javaPlugin, "BungeeCord", this);
    }

   /* @EventHandler
    public void onChangePrefix(GamerChangePrefixEvent e) {
        Player player = e.getGamer().getPlayer();
        if (player == null) {
            return;
        }

        String name = player.getName();
        String prefix = e.getPrefix();

        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("Suffix");
        out.writeUTF(name);
        out.writeUTF(name);
        out.writeUTF(prefix);

        player.sendPluginMessage(javaPlugin, "BungeeCord", out.toByteArray());
    }

    @EventHandler
    public void onChangeSettings(GamerChangeSettingEvent e) {
        Player player = e.getGamer().getPlayer();
        if (player == null) {
            return;
        }

        String name = player.getName();
        int settingID = e.getSetting().getKey();
        boolean result = e.isResult();

        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("Settings");
        out.writeUTF(name);
        out.writeUTF(name);
        out.writeInt(settingID);
        out.writeBoolean(result);
        out.writeBoolean(true);

        player.sendPluginMessage(javaPlugin, "BungeeCord", out.toByteArray());
    }*/

    @EventHandler
    public void onStartUp(ServerStartEvent e) {
        String serverName = e.getServerName();
        String ip = e.getIp();
        int port = e.getPort();
        try {
            final Keyboard keyboard = new Keyboard();
            final List<List<KeyboardButton>> buttons = new ArrayList<>();
            buttons.add(Collections.singletonList(new KeyboardButton().setColor(KeyboardButtonColor.NEGATIVE).setAction(
                    new KeyboardButtonAction().setLabel("Запретить запуск").setType(KeyboardButtonActionType.TEXT).setPayload("99"))));
            buttons.add(Collections.singletonList(new KeyboardButton().setColor(KeyboardButtonColor.POSITIVE).setAction(
                    new KeyboardButtonAction().setLabel("Разрешить запуск").setType(KeyboardButtonActionType.TEXT).setPayload("99"))));
            actor = new GroupActor(68050582, "afc4970c6c6a54715c7114ac0749f55647b3343e59878869e6671602892bc12a0b0ded66931b82af03fb1");
            client = new VkApiClient(HttpTransportClient.getInstance());
            client.messages().send(actor).randomId((int) MathUtil.random())
                    .peerId(529779924)
                    .keyboard(keyboard.setButtons(buttons))
                    .message("Подтвердите дейстие с сервером\n\nНазвание сервера: " + serverName + "\nИП: " + ip + "\nПОРТ:" + port).execute();
        } catch (ApiException | ClientException apiException) {
            apiException.printStackTrace();
            DartaAPI.getInstance().getServer().shutdown();
        }
        ServerStartEvent.bukkitStartUp.put("startup", false);
    }

    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] bytes) {
        ByteArrayDataInput input = ByteStreams.newDataInput(bytes);
        if (input.readUTF().equalsIgnoreCase("BukkitStartup")) {
            /*case "skin":
                Player target = Bukkit.getPlayer(input.readUTF());
                if (target == null || !player.isOnline()) {
                    return;
                }

                SkinUtil.setSkin(target, input.readUTF(), input.readUTF(), input.readUTF(),
                        SkinType.getSkinType(input.readInt()));*/

            boolean start = input.readBoolean();
            String message = input.readUTF();
            mAPI.getInstance().getLogger().info(message);
            if (!start) {
                mAPI.getInstance().getServer().shutdown();
            }
            ServerStartEvent.bukkitStartUp.remove("startup");
        }

    }
}
