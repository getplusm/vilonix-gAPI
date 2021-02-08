package plusm.vilonix.gapi.module.vk;

import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import com.vk.api.sdk.objects.messages.Message;
import com.vk.api.sdk.queries.messages.MessagesGetLongPollHistoryQuery;
import lombok.Getter;
import plusm.vilonix.gapi.module.vk.cmd.MessageHandler;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;

public class SecondVK {

    @Getter
    private static VkApiClient client;
    @Getter
    private static GroupActor actor;
    private int maxMsgId;
    private static int ts;
    @Getter
    private static Thread thread;

    public SecondVK() {
        AtomicLong longHolder = new AtomicLong();
        longHolder.set(System.currentTimeMillis());
        try {
            client = new VkApiClient(HttpTransportClient.getInstance());
            actor = new GroupActor(68050582, "afc4970c6c6a54715c7114ac0749f55647b3343e59878869e6671602892bc12a0b0ded66931b82af03fb1");
            ts = client.messages().getLongPollServer(actor).execute().getTs();
        } catch (ApiException | ClientException ignored) {
        }
        thread = new Thread(() -> {
            while (!Thread.interrupted()) {
                try {
                    Message message = this.getMessage();
                    if (message != null)
                        Executors.newCachedThreadPool().execute(new MessageHandler(message));
                    Thread.sleep(500L);
                } catch (ClientException | ApiException | InterruptedException e) {
                    thread.interrupt();
                }
            }
        }, "VkServer thread");
        thread.start();
    }

    private Message getMessage() throws ApiException, ClientException {
        final MessagesGetLongPollHistoryQuery eventsQuery = client.messages().getLongPollHistory(actor).ts(ts);
        if (this.maxMsgId > 0) {
            eventsQuery.maxMsgId(this.maxMsgId);
        }
        if (eventsQuery != null) {
            final List<Message> messages = eventsQuery.execute().getMessages().getItems();
            if (messages.isEmpty()) {
                return null;
            }
            ts = client.messages().getLongPollServer(actor).execute().getTs();
            if (!messages.get(0).isOut()) {
                final int messageId = messages.get(0).getId();
                if (messageId > this.maxMsgId) {
                    this.maxMsgId = messageId;
                }
                return messages.get(0);
            }
        }
        return null;
    }
}