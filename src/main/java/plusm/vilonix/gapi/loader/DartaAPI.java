package plusm.vilonix.gapi.loader;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.java.JavaPlugin;
import plusm.vilonix.api.VilonixNetwork;
import plusm.vilonix.api.player.Spigot;
import plusm.vilonix.api.sql.GlobalLoader;
import plusm.vilonix.api.sql.PlayerInfoLoader;
import plusm.vilonix.api.sql.api.AbstractDatabase;
import plusm.vilonix.api.types.GameType;
import plusm.vilonix.api.types.SubType;
import plusm.vilonix.gapi.commands.*;
import plusm.vilonix.gapi.donatemenu.DonateMenuListener;
import plusm.vilonix.gapi.exploit.imp.ExploitManager;
import plusm.vilonix.gapi.guis.GuiDefaultContainer;
import plusm.vilonix.gapi.listeners.GamerListener;
import plusm.vilonix.gapi.listeners.JoinListener;
import plusm.vilonix.gapi.listeners.NetworkingListener;
import plusm.vilonix.gapi.module.achievement.manager.AchievementSql;
import plusm.vilonix.gapi.module.stats.Stats;
import plusm.vilonix.gapi.utils.bukkit.EmptyWorldGenerator;
import plusm.vilonix.libraries.LibAPI;
import plusm.vilonix.libraries.packetreader.PacketReaderListener;

import java.util.Arrays;

@Getter
public final class DartaAPI extends JavaPlugin {

    private DonateMenuListener donateMenuListener;
    private EmptyWorldGenerator generator;
    private GuiDefaultContainer guiDefaultContainer;
    private static DartaAPI instance;
    private String username;

    public static DartaAPI getInstance() {
        return instance;
    }

    private void closeMysql() {
        AbstractDatabase globalBase = GlobalLoader.getMysqlDatabase();
        if (globalBase != null) {
            globalBase.close();
        }

        AbstractDatabase playerInfoBase = PlayerInfoLoader.getMysqlDatabase();
        if (playerInfoBase != null) {
            playerInfoBase.close();
        }

        AbstractDatabase statsBase = Stats.MYSQL_DATABASE;
        if (statsBase != null) {
            statsBase.close();
        }
        AbstractDatabase spec = SpectatorLoader.getMysqlDatabase();
        if (spec != null) {
            spec.close();
        }

        AbstractDatabase ach = AchievementSql.getMySqlDatabase();
        if (ach != null) {
            ach.close();
        }
    }

    @Override
    public final ChunkGenerator getDefaultWorldGenerator(String worldName, String id) {
        return generator;
    }

    public DonateMenuListener getDonateMenuListener() {
        return donateMenuListener;
    }

    public void setDonateMenuListener(DonateMenuListener donateMenuListener) {
        this.donateMenuListener = donateMenuListener;
    }

    public EmptyWorldGenerator getGenerator() {
        return this.generator;
    }

    public GuiDefaultContainer getGuiDefaultContainer() {
        return guiDefaultContainer;
    }

    public void setGuiDefaultContainer(GuiDefaultContainer guiDefaultContainer) {
        this.guiDefaultContainer = guiDefaultContainer;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public final void onDisable() {
        closeMysql();
    }

    @Override
    public final void onEnable() {
        try {
            checkIfBungee();
            instance = this;
            this.username = Bukkit.getServerName();
            getLogger().info("§fНазвание сервера определено как: " + username);

            generator = new EmptyWorldGenerator();

            LibAPI.init(this);
            PublicApiLoader.init(this);
            registerType();

            new GamerListener(this);
            new NetworkingListener(this);
//        if (SubType.current != SubType.MISC)
//            new ChatListener(this);
//        new BungeeMessageListener(this);
            new PacketReaderListener(this);
            new JoinListener(DartaAPI.getInstance());
            setDonateMenuListener(new DonateMenuListener(this));
            //setGuiDefaultContainer(new GuiDefaultContainer());

            registerCommands();
            //Bukkit.getScheduler().runTaskTimer(this, new WorldTime.TimeTask(), 20L, 30L);
            //WorldTime.freezeTime("lobby", 6000L, false);
            if (this.getServer().getPluginManager().getPlugin("PlaceholderAPI") != null) {
                new VilonixPAPI().register();
                this.getLogger().info("§bPAPI hooked");
            }
            if (this.getServer().getPluginManager().getPlugin("mFix") == null) {
                this.getPluginLoader().disablePlugin(this);
                getLogger().warning("§cОтключил плагин, ибо плагин mFix не найден!");
                getServer().spigot().restart();
            }
            new ExploitManager(this);


            /*getLogger().info("§2Запускаю временный сервер вк..");
            try {
                new SecondVK();
            }catch (Exception ex){
                ex.printStackTrace();
                Thread.sleep(1000);
                getServer().shutdown();
            }
            getLogger().info("§2Сервер запущен!");
            getLogger().info("§eОтправил запрос Плазмеру, ждем 2 минуты.");
            try {
                ServerStartEvent e = new ServerStartEvent(username, getServer().getIp(), getServer().getPort());
                BukkitUtil.callEvent(e);
                e.thread();
            }catch (Exception ex){
                ex.printStackTrace();
                Thread.sleep(1000);
                getServer().shutdown();
            }
             */
        } catch (Exception ex) {
            ex.printStackTrace();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            getServer().shutdown();
        }
    }

    private void registerCommands() {
        new FwCommand();
        new RulesCommand();
        new MemoryCommand();
        new CrashCommand();
        new LevelCommand();
        new ListCommand();
        new MoneyCommand();
        //new ConvertCommand();
        new PayCommand();
        new GiveMoneyCommand();
        new GiveKeyCommand();
        new ReferralCommand();
    }

    private void registerType() {
        Spigot spigot = VilonixNetwork.getGamerManager().getSpigot();
        String serverType = username.split("-")[0];

        try {
            SubType.current = SubType.valueOf(serverType.toUpperCase());
            GameType.current = SubType.current.getGameType();
        } catch (IllegalArgumentException exception) {
            SubType.current = SubType.getByName(System.getProperty("subType", "misc"));
            GameType.current = Arrays.stream(GameType.values())
                    .filter(gameType -> gameType.getLobbyChannel().equalsIgnoreCase(serverType))
                    .findFirst()
                    .orElse(SubType.current.getGameType());
        }
        spigot.sendMessage("§cТип сервера определен как §e" + GameType.current.name());
        spigot.sendMessage("§cПодтип сервера определен как §e" + SubType.current.name());
    }

    private void checkIfBungee() {
        if (!getServer().spigot().getConfig().getConfigurationSection("settings").getBoolean("bungeecord")) {
            getLogger().warning("§cСервер не подключен к BungeeCord. Возможно Вы просто забыли включить эту функцию §d`O.o/");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            getServer().shutdown();
        }
    }

}