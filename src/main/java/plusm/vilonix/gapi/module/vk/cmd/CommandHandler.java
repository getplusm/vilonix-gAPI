package plusm.vilonix.gapi.module.vk.cmd;

import com.vk.api.sdk.objects.messages.Message;

public final class CommandHandler {
    public static void execute(final Message message) {
        final Command cmd = CommandManager.getCommand(message);
        if (cmd == null) {
            return;
        }
        cmd.execute(message);
    }
}
