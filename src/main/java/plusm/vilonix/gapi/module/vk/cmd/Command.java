package plusm.vilonix.gapi.module.vk.cmd;

import com.vk.api.sdk.objects.messages.Message;

public abstract class Command {
    private final String name;
    private final String payload;

    public abstract void execute(final Message p0);

    public String getName() {
        return this.name;
    }

    public String getPayload() {
        return this.payload;
    }

    public Command(final String name, final String payload) {
        this.name = name;
        this.payload = payload;
    }
}
