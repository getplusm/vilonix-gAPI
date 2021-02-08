package plusm.vilonix.gapi.module.vk.cmd;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.objects.messages.Message;
import plusm.vilonix.gapi.event.ServerStartEvent;
import plusm.vilonix.gapi.loader.DartaAPI;
import plusm.vilonix.gapi.module.vk.SecondVK;

import java.util.concurrent.ThreadLocalRandom;

public class BukkitStartUp extends Command {
    public BukkitStartUp(final String name, final String payload) {
        super(name, payload);
    }

    @Override
    public void execute(final Message message) {
        if (!message.getPeerId().equals(529779924)) {
            try {
                SecondVK.getClient().messages().send(SecondVK.getActor()).randomId(ThreadLocalRandom.current().nextInt())
                        .peerId(message.getPeerId())
                        .message("��, � �� ���?\n\n���� �� �� �������� ����������, ��� ������").execute();
            } catch (ApiException | ClientException e) {
                e.printStackTrace();
            }
            return;
        }
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        switch (message.getText().toLowerCase()) {
            case "��������� ������":
                out.writeBoolean(true);
                out.writeUTF("�a������� ��� ����� �� ������");
            case "��������� ������":
                out.writeBoolean(false);
                out.writeUTF("�c������� ������� ������");
        }
        try {
            DartaAPI.getInstance().getLogger().info("�2�������� ��������� ������ ��..");
            SecondVK.getThread().interrupt();
            ServerStartEvent.bukkitStartUp.remove("startup");
        }catch (Exception ex){
            ex.printStackTrace();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            DartaAPI.getInstance().getServer().shutdown();
        }
    }
}
