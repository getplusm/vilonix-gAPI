package plusm.vilonix.gapi.guis;

import org.bukkit.entity.Player;
import plusm.vilonix.api.VilonixNetwork;
import plusm.vilonix.api.player.GamerManager;
import plusm.vilonix.gapi.commands.DonateCommand;
import plusm.vilonix.gapi.commands.HelpCommand;
import plusm.vilonix.gapi.donatemenu.DonateMenuData;
import plusm.vilonix.gapi.guis.basic.DonateGui;
import plusm.vilonix.gapi.guis.basic.Gui;
import plusm.vilonix.gapi.guis.basic.HelpGui;
import plusm.vilonix.gapi.guis.basic.RewardHelpGui;
import plusm.vilonix.gapi.guis.basic.donate.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class GuiDefaultContainer {

    private final GamerManager GAMER_MANAGER = VilonixNetwork.getGamerManager();
    private final Map<String, Gui> Guidata = new HashMap<>();
    private final Map<String, DonateMenuData> data = new ConcurrentHashMap<>();

    public GuiDefaultContainer(Player player) {
        createGui(HelpGui.class, player);
        createGui(DonateGui.class, player);
        createGui(RewardHelpGui.class, player);
        createGui(PrimoGUI.class, player);
        createGui(LiteGUI.class, player);
        createGui(AlegroGUI.class, player);
        createGui(AngelGUI.class, player);
        createGui(DemonGUI.class, player);
        createGui(FeliksGUI.class, player);

        new HelpCommand(this);
        new DonateCommand(this);
    }

    @SuppressWarnings("unchecked")
    public <T extends Gui> T getGui(Class<T> clazz) {
        return (T) Guidata.get(clazz.getSimpleName().toLowerCase());
    }

    private void createGui(Class<? extends Gui> clazz, Player player) {
        try {
            Gui gui = clazz.getConstructor(Player.class, GuiDefaultContainer.class)
                    .newInstance(player, this);
            this.Guidata.put(clazz.getSimpleName().toLowerCase(), gui);
        } catch (Exception ignored) {
        }
    }

    public void open(Player player, Class<? extends Gui> clazz) {
        String name = player.getName().toLowerCase();
        /*GuiDefaultContainer data = this.data.get(name);
        if (data == null) {
            data = new GuiDefaultContainer(player);
            this.data.put(name, data);
        }
*/
        Gui gui = getGui(clazz);
        if (gui == null) {
            return;
        }

        gui.open();
    }
}
