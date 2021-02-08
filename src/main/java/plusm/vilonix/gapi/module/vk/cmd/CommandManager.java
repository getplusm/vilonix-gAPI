package plusm.vilonix.gapi.module.vk.cmd;

import com.google.common.collect.ImmutableSet;
import com.vk.api.sdk.objects.messages.Message;

import java.util.HashSet;
import java.util.Set;

public final class CommandManager {
    private static final Set<Command> COMMANDS = new HashSet<>();
    private static final ImmutableSet<String> ALIASES = ImmutableSet.of("аккаунт", "начать", "аккаунт", "/начать", "/аккаунт", "привязать", "/привязать");

    public static void addCommand(final Command command) {
        COMMANDS.add(command);
    }

    public static Command getCommand(final Message message) {
        final String[] msg = message.getText().split(" ");
        final String payload = message.getPayload();
        if (payload != null && !payload.isEmpty()) {
            return COMMANDS.stream().filter(command -> command.getPayload() != null && command.getPayload().equals(payload)).findFirst().orElse(null);
        }
        if (!ALIASES.contains(msg[0].toLowerCase())) {
            return null;
        }
        if (msg.length > 1) {
            return COMMANDS.stream().filter(command -> command.getName().equalsIgnoreCase(msg[1].toLowerCase())).findFirst().orElse(null);
        }
        return COMMANDS.stream().filter(command -> command.getName().equalsIgnoreCase("аккаунт")).findFirst().orElse(null);
    }

    static {
        addCommand(new BukkitStartUp("startup", "99"));
    }
}
