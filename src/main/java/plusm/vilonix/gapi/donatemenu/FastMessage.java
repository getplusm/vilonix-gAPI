package plusm.vilonix.gapi.donatemenu;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.luckperms.api.model.group.Group;
import plusm.vilonix.api.VilonixNetwork;
import plusm.vilonix.api.mAPI;
import plusm.vilonix.api.player.BukkitGamer;
import plusm.vilonix.api.player.GamerManager;
import plusm.vilonix.api.util.ConfigUtil;
import plusm.vilonix.gapi.donatemenu.event.AsyncGamerSendFastMessageEvent;
import plusm.vilonix.gapi.utils.bukkit.BukkitUtil;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

@AllArgsConstructor
@Getter
public enum FastMessage {
    ACCESSIBLY("Ãîó \u2510(\ufe36\u25bd\ufe36)\u250c"),
    CAPITULATE("Âææææóóõ(\u00d7_\u00d7)\u5c38"),
    DANCING("\u250c(^_^)\u2518"),
    DONT_STUPID("(; -_-)"),
    EASY("<(\uffe3\ufe36\uffe3)>"),
    EATING("(\u02d8\u25bd\u02d8)\u3063\u2668"),
    ENRAGES("\u2500=\u2261\u03a3(((\u3064\uff1e\uff1c)\u3064"),
    FUCK("\u2514(\uffe3-\uffe3\u2514)"),
    GIVE_RESOURCES("\u51f8\u25df(\u00ba o \u00ba )"),
    GO("\u250c(\u0ca0_\u0ca0)\u2518"),
    GO_AWAY("(\u256f\u00b0\u25a1\u00b0)\u256f \u253b\u2501\u2501\u253b"),
    GOODBYE("\u2570(\u256f\ufe35\u2570,)"),
    HELLO("(^\u03c9^)\u30ce"),
    HUGGING("(\u30ce^_^)\u30ce"),
    JEEZ("\u30fd(\uff9f\u3007\uff9f)\uff89"),
    KILLED("(\uff92\uffe3\u25bd\uffe3)\ufe3b\u2533\u2550\u4e00"),
    LAGS("(\u30ce\u00b0\u76ca\u00b0)\u30ce"),
    LOVE("(\u02d8\u2323\u02d8)\u2661"),
    MAJOR("$ (\u0ca0_\u0ca0) $"),
    MUSIC("(\uffe3\u25bd\uffe3)/\u266b\u00b8\u00b8\u266a"),
    NICELY("(\uff3e\u2022 \u03c9 \u2022\uff3e)"),
    OFFENSIVELY("(\u4e2a_\u4e2a)"),
    SAD("(\u256f\ufe35\u2570,)"),
    SLEEP("\uff0d\u03c9\uff0d) zzZ"),
    WHY("\uff3c(\u3007_\uff4f)\uff0f"),
    WOW("\u0669(\u25d5\u203f\u25d5)\u06f6"),
    WTF("(\u2299_\u2299)"),
    XZ("\u00af\uff3c_(\u30c4)_/\u00af"),

    DIE("(\u0ca0_\u0ca0)\u2013\u03a8"),
    HI("(\u0361\u00b0 \u035c\u0296 \u0361\u00b0)"),
    BEAUTIFUL("\u0ced\u0a67(\u275b\u25bf\u275b\u273f)\u0a6d\u0ce8"),
    CHEL("\u0ca0\ufe4f\u0ca0"),

    NE_JDALI("(\u30ce\uff40\u0414\u00b4)\u30ce\u5f61\u253b\u2501\u253b"),
    GDE_MONEY("[\u0332\u0305$\u0332\u0305(\u0332\u0305 \u0361\u0ca0_\u0ca0)\u0332\u0305$\u0332\u0305]"),
    FLEX("\u30d8(\uffe3\u03c9\uffe3\u30d8)"),
    WOW2("{\u00b4\u25d5 \u25e1 \u25d5\uff40}"),
    NU_TEBYA("( \ufe36\ufe3f\ufe36)_\u256d\u2229\u256e"),
    MINUS_LIFE("(\u0482\u2323\u0300_\u2323\u0301)"),
    INF_POWER("\u0f3c\u0e07=\u0ca0\u76ca\u0ca0=\u0f3d\u0e07"),
    ;

    private final GamerManager gamerManager = VilonixNetwork.getGamerManager();
    private final String smile;

    public static Map<String, FastMessage> getMessages() {
        Map<String, FastMessage> map = new HashMap<>();
        Arrays.stream(values()).forEach(fastMessage -> map.put(ConfigUtil.getMessage(fastMessage.getKey()), fastMessage));
        return map;
    }

    public Group getGroup() {
        return mAPI.getLuckPerms().getGroupManager().getGroup("PRIMO");
    }

    public String getKey() {
        return "FAST_MESSAGE_" + name();
    }

    public String getSmile() {
        return smile;
    }

    public void sendToAll(BukkitGamer gamer) {
        if (gamer == null || gamer.getPlayer() == null) {
            return;
        }

        AsyncGamerSendFastMessageEvent event = new AsyncGamerSendFastMessageEvent(gamer, this,
                new HashSet<>(gamerManager.getGamers().values()));
        BukkitUtil.runTaskAsync(() -> BukkitUtil.callEvent(event));
    }
}
