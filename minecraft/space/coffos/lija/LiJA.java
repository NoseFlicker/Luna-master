package space.coffos.lija;

import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.Display;
import space.coffos.lija.impl.gui.alt.AltManager;
import space.coffos.lija.impl.managers.*;
import space.coffos.lija.util.general.core.API;
import space.coffos.lija.util.web.WebHelper;

public enum LiJA {

    INSTANCE;

    private final Minecraft mc = Minecraft.getMinecraft();

    // Sub Client management
    public SubClients currentSubClient = SubClients.LiJA;
    /*
     * Build:
     * 0 => Temperance
     * 1 => LiJA
     * 2 => LiJA
     */
    public final String[] build = {"3", "2.0", "1.1"};

    public final String[] format = {"v", "b", ""}, classedObjects = {"space.lunaclient.luna.impl.orders."};
    public String currentFormat = format[2];

    // Background / icons to use
    public final String backgroundGray = "luna/gray.png";

    // Sponsors that's being displayed in AltManager.
    public String sponsorList;

    public WaypointManager waypointManager;
    public CustomFileManager fileManager;
    public SettingManager settingManager;
    public ElementManager elementManager;
    public FriendManager friendManager;
    public SubClientManager subManager;
    public ConfigManager configManager;
    public OrderManager orderManager;
    public EventManager eventManager;
    public FontManager fontManager;
    public AltManager altManager;
    public boolean isLoading;
    public API api;

    /**
     * Update Minecraft's title.
     */
    public void updateTitle() {
        Display.setTitle("| " + subManager.getClient() + " " + currentFormat + build[subManager.getClient().equalsIgnoreCase("Luna") ? 1 : subManager.getClient().equalsIgnoreCase("Temperance") ? 0 : 2]);
    }

    /**
     * Start LiJA.
     */
    public void onLaunch() {
        /*
        * FIXME: Proxies [Socks4]
        System.setProperty("socksProxyHost", "IP");
        System.setProperty("socksProxyPort", "PORT");
        */
        isLoading = true;
        WebHelper.trustCertificates();
        subManager = new SubClientManager();
        updateTitle();
        eventManager = new EventManager();
        elementManager = new ElementManager();
        fontManager = new FontManager();
        settingManager = new SettingManager();
        orderManager = new OrderManager();
        friendManager = new FriendManager();
        configManager = new ConfigManager();
        waypointManager = new WaypointManager();
        altManager = new AltManager();
        altManager.setupAlts();
        fileManager = new CustomFileManager();
        Runtime.getRuntime().addShutdownHook(new Thread(this::onClose));
        isLoading = false;
        api = new API();
    }


    /**
     * Close LiJA fully.
     */
    public void onClose() {
        ElementManager.elements.clear();
        orderManager.orders.clear();
        fileManager.saveFiles();
        System.out.println(
                "╔══════════════╗\n" +
                        "  Closing " + currentSubClient + "\n" +
                        "╚══════════════╝");
        System.gc();
        mc.shutdown();
    }
}