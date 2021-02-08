package plusm.vilonix.gapi.module.vk.cmd;

import com.vk.api.sdk.objects.messages.Message;

public class MessageHandler implements Runnable {
    private final Message message;

    @Override
    public void run() {
        CommandHandler.execute(this.message);
    }

    public MessageHandler(final Message message) {
        this.message = message;
    }
}
