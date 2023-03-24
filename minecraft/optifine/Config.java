package optifine;

import net.minecraft.client.LoadingScreenRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.*;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.server.integrated.IntegratedServer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.WorldServer;
import org.apache.commons.io.IOUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.*;
import shadersmod.client.Shaders;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.*;
import java.util.concurrent.atomic.AtomicReferenceArray;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

public class Config {
    public static final String OF_NAME = "OptiFine";
    public static final String MC_VERSION = "1.8";
    public static final String OF_EDITION = "HD_U";
    public static final String OF_RELEASE = "H6";
    public static final String VERSION = "OptiFine_1.8_HD_U_H6";
    private static String newRelease = null;
    private static boolean notify64BitJava = false;
    static String openGlVersion = null;
    static String openGlRenderer = null;
    static String openGlVendor = null;
    private static boolean fancyFogAvailable = false;
    private static boolean occlusionAvailable = false;
    private static GameSettings gameSettings = null;
    private static Minecraft minecraft = null;
    private static boolean initialized = false;
    private static Thread minecraftThread = null;
    private static DisplayMode desktopDisplayMode = null;
    private static int antialiasingLevel = 0;
    private static int availableProcessors = 0;
    public static boolean zoomMode = false;
    private static int texturePackClouds = 0;
    public static boolean waterOpacityChanged = false;
    private static boolean fullscreenModeChecked = false;
    private static boolean desktopModeChecked = false;
    private static DefaultResourcePack defaultResourcePack = null;
    private static ModelManager modelManager = null;
    private static PrintStream systemOut = new PrintStream(new FileOutputStream(FileDescriptor.out));
    private static final Float DEF_ALPHA_FUNC_LEVEL = 0.1F;

    public static String getVersion() {
        return "OptiFine_1.8_HD_U_H6";
    }

    public static String getVersionDebug() {
        StringBuffer sb = new StringBuffer(32);

        if (isDynamicLights()) {
            sb.append("DL: ");
            sb.append(DynamicLights.getCount());
            sb.append(", ");
        }

        sb.append("OptiFine_1.8_HD_U_H6");
        String shaderPack = Shaders.getShaderPackName();

        if (shaderPack != null) {
            sb.append(", ");
            sb.append(shaderPack);
        }

        return sb.toString();
    }

    public static void initGameSettings(GameSettings settings) {
        if (gameSettings == null) {
            gameSettings = settings;
            minecraft = Minecraft.getMinecraft();
            desktopDisplayMode = Display.getDesktopDisplayMode();
            updateAvailableProcessors();
            ReflectorForge.putLaunchBlackboard("optifine.ForgeSplashCompatible", Boolean.TRUE);
        }
    }

    public static void initDisplay() {
        checkInitialized();
        antialiasingLevel = gameSettings.ofAaLevel;
        checkDisplaySettings();
        checkDisplayMode();
        minecraftThread = Thread.currentThread();
        updateThreadPriorities();
        Shaders.startup(Minecraft.getMinecraft());
    }

    private static void checkInitialized() {
        if (!initialized) {
            if (Display.isCreated()) {
                initialized = true;
                checkOpenGlCaps();
                startVersionCheckThread();
            }
        }
    }

    private static void checkOpenGlCaps() {
        log("");
        log(getVersion());
        log("Build: " + getBuild());
        log("OS: " + System.getProperty("os.name") + " (" + System.getProperty("os.arch") + ") version " + System.getProperty("os.version"));
        log("Java: " + System.getProperty("java.version") + ", " + System.getProperty("java.vendor"));
        log("VM: " + System.getProperty("java.vm.name") + " (" + System.getProperty("java.vm.info") + "), " + System.getProperty("java.vm.vendor"));
        log("LWJGL: " + Sys.getVersion());
        openGlVersion = GL11.glGetString(GL11.GL_VERSION);
        openGlRenderer = GL11.glGetString(GL11.GL_RENDERER);
        openGlVendor = GL11.glGetString(GL11.GL_VENDOR);
        log("OpenGL: " + openGlRenderer + ", version " + openGlVersion + ", " + openGlVendor);
        log("OpenGL Version: " + getOpenGlVersionString());

        if (!GLContext.getCapabilities().OpenGL12)
            log("OpenGL Mipmap levels: Not available (GL12.GL_TEXTURE_MAX_LEVEL)");

        fancyFogAvailable = GLContext.getCapabilities().GL_NV_fog_distance;

        if (!fancyFogAvailable) log("OpenGL Fancy fog: Not available (GL_NV_fog_distance)");

        occlusionAvailable = GLContext.getCapabilities().GL_ARB_occlusion_query;

        if (!occlusionAvailable) log("OpenGL Occlussion culling: Not available (GL_ARB_occlusion_query)");

        int maxTexSize = Minecraft.getGLMaximumTextureSize();
        dbg("Maximum texture size: " + maxTexSize + "x" + maxTexSize);
    }

    private static String getBuild() {
        try {
            InputStream e = Config.class.getResourceAsStream("/buildof.txt");

            if (e == null) return null;
            else {
                String build = readLines(e)[0];
                return build;
            }
        } catch (Exception var2) {
            warn("" + var2.getClass().getName() + ": " + var2.getMessage());
            return null;
        }
    }

    public static boolean isFancyFogAvailable() {
        return fancyFogAvailable;
    }

    private static String getOpenGlVersionString() {
        int ver = getOpenGlVersion();
        return "" + ver / 10 + "." + ver % 10;
    }

    private static int getOpenGlVersion() {
        return !GLContext.getCapabilities().OpenGL11 ? 10 : (!GLContext.getCapabilities().OpenGL12 ? 11 : (!GLContext.getCapabilities().OpenGL13 ? 12 : (!GLContext.getCapabilities().OpenGL14 ? 13 : (!GLContext.getCapabilities().OpenGL15 ? 14 : (!GLContext.getCapabilities().OpenGL20 ? 15 : (!GLContext.getCapabilities().OpenGL21 ? 20 : (!GLContext.getCapabilities().OpenGL30 ? 21 : (!GLContext.getCapabilities().OpenGL31 ? 30 : (!GLContext.getCapabilities().OpenGL32 ? 31 : (!GLContext.getCapabilities().OpenGL33 ? 32 : (!GLContext.getCapabilities().OpenGL40 ? 33 : 40)))))))))));
    }

    public static void updateThreadPriorities() {
        updateAvailableProcessors();

        if (isSingleProcessor()) {
            if (isSmoothWorld()) {
                minecraftThread.setPriority(10);
                setThreadPriority("Server thread", 1);
            } else {
                minecraftThread.setPriority(5);
                setThreadPriority("Server thread", 5);
            }
        } else {
            minecraftThread.setPriority(10);
            setThreadPriority("Server thread", 5);
        }
    }

    private static void setThreadPriority(String prefix, int priority) {
        try {
            ThreadGroup e = Thread.currentThread().getThreadGroup();

            if (e == null) return;

            int num = (e.activeCount() + 10) * 2;
            Thread[] ts = new Thread[num];
            e.enumerate(ts, false);

            Arrays.stream(ts).filter(t -> t != null && t.getName().startsWith(prefix)).forEachOrdered(t -> t.setPriority(priority));
        } catch (Throwable var7) {
            warn(var7.getClass().getName() + ": " + var7.getMessage());
        }
    }

    public static boolean isMinecraftThread() {
        return Thread.currentThread() == minecraftThread;
    }

    private static void startVersionCheckThread() {
        VersionCheckThread vct = new VersionCheckThread();
        vct.start();
    }

    public static boolean isMipmaps() {
        return gameSettings.mipmapLevels > 0;
    }

    public static int getMipmapLevels() {
        return gameSettings.mipmapLevels;
    }

    public static int getMipmapType() {
        switch (gameSettings.ofMipmapType) {
            case 0:
                return 9986;

            case 1:
                return 9986;

            case 2:
                if (isMultiTexture()) return 9985;

                return 9986;

            case 3:
                if (isMultiTexture()) return 9987;

                return 9986;

            default:
                return 9986;
        }
    }

    public static boolean isFogFancy() {
        return isFancyFogAvailable() && gameSettings.ofFogType == 2;
    }

    public static boolean isFogFast() {
        return gameSettings.ofFogType == 1;
    }

    public static boolean isFogOff() {
        return gameSettings.ofFogType == 3;
    }

    public static float getFogStart() {
        return gameSettings.ofFogStart;
    }

    public static void dbg(String s) {
        systemOut.print("[OptiFine] ");
        systemOut.println(s);
    }

    public static void warn(String s) {
        systemOut.print("[OptiFine] [WARN] ");
        systemOut.println(s);
    }

    public static void error(String s) {
        systemOut.print("[OptiFine] [ERROR] ");
        systemOut.println(s);
    }

    public static void log(String s) {
        dbg(s);
    }

    public static int getUpdatesPerFrame() {
        return gameSettings.ofChunkUpdates;
    }

    public static boolean isRainFancy() {
        return gameSettings.ofRain == 0 ? gameSettings.fancyGraphics : gameSettings.ofRain == 2;
    }

    public static boolean isRainOff() {
        return gameSettings.ofRain == 3;
    }

    public static boolean isCloudsFancy() {
        return gameSettings.ofClouds != 0 ? gameSettings.ofClouds == 2 : (isShaders() && !Shaders.shaderPackClouds.isDefault() ? Shaders.shaderPackClouds.isFancy() : (texturePackClouds != 0 ? texturePackClouds == 2 : gameSettings.fancyGraphics));
    }

    public static boolean isCloudsOff() {
        return gameSettings.ofClouds != 0 ? gameSettings.ofClouds == 3 : (isShaders() && !Shaders.shaderPackClouds.isDefault() ? Shaders.shaderPackClouds.isOff() : (texturePackClouds != 0 ? texturePackClouds == 3 : false));
    }

    static void updateTexturePackClouds() {
        texturePackClouds = 0;
        IResourceManager rm = getResourceManager();

        if (rm != null) {
            try {
                InputStream e = rm.getResource(new ResourceLocation("mcpatcher/color.properties")).getInputStream();

                if (e == null) return;

                Properties props = new Properties();
                props.load(e);
                e.close();
                String cloudStr = props.getProperty("clouds");

                if (cloudStr == null) return;

                dbg("Texture pack clouds: " + cloudStr);
                cloudStr = cloudStr.toLowerCase();

                if (cloudStr.equals("fast")) texturePackClouds = 1;

                if (cloudStr.equals("fancy")) texturePackClouds = 2;

                if (cloudStr.equals("off")) texturePackClouds = 3;
            } catch (Exception ignored) {
            }
        }
    }

    public static void setModelManager(ModelManager modelManager) {
        modelManager = modelManager;
    }

    static ModelManager getModelManager() {
        return modelManager;
    }

    public static boolean isTreesFancy() {
        return gameSettings.ofTrees == 0 ? gameSettings.fancyGraphics : gameSettings.ofTrees != 1;
    }

    public static boolean isTreesSmart() {
        return gameSettings.ofTrees == 4;
    }

    public static boolean isCullFacesLeaves() {
        return gameSettings.ofTrees == 0 ? !gameSettings.fancyGraphics : gameSettings.ofTrees == 4;
    }

    public static boolean isDroppedItemsFancy() {
        return gameSettings.ofDroppedItems == 0 ? gameSettings.fancyGraphics : gameSettings.ofDroppedItems == 2;
    }

    public static int limit(int val, int min, int max) {
        return val < min ? min : (val > max ? max : val);
    }

    public static float limit(float val, float min, float max) {
        return val < min ? min : (val > max ? max : val);
    }

    public static double limit(double val, double min, double max) {
        return val < min ? min : (val > max ? max : val);
    }

    static float limitTo1(float val) {
        return val < 0.0F ? 0.0F : (val > 1.0F ? 1.0F : val);
    }

    public static boolean isAnimatedWater() {
        return gameSettings.ofAnimatedWater != 2;
    }

    public static boolean isAnimatedPortal() {
        return gameSettings.ofAnimatedPortal;
    }

    public static boolean isAnimatedLava() {
        return gameSettings.ofAnimatedLava != 2;
    }

    public static boolean isAnimatedFire() {
        return gameSettings.ofAnimatedFire;
    }

    public static boolean isAnimatedRedstone() {
        return gameSettings.ofAnimatedRedstone;
    }

    public static boolean isAnimatedExplosion() {
        return gameSettings.ofAnimatedExplosion;
    }

    public static boolean isAnimatedFlame() {
        return gameSettings.ofAnimatedFlame;
    }

    public static boolean isAnimatedSmoke() {
        return gameSettings.ofAnimatedSmoke;
    }

    public static boolean isVoidParticles() {
        return gameSettings.ofVoidParticles;
    }

    public static boolean isWaterParticles() {
        return gameSettings.ofWaterParticles;
    }

    public static boolean isRainSplash() {
        return gameSettings.ofRainSplash;
    }

    public static boolean isPotionParticles() {
        return gameSettings.ofPotionParticles;
    }

    public static boolean isFireworkParticles() {
        return gameSettings.ofFireworkParticles;
    }

    public static float getAmbientOcclusionLevel() {
        return gameSettings.ofAoLevel;
    }

    public static String arrayToString(Object[] arr) {
        if (arr == null) return "";
        else {
            StringBuilder buf = new StringBuilder(arr.length * 5);

            IntStream.range(0, arr.length).forEachOrdered(i -> {
                Object obj = arr[i];
                if (i > 0) buf.append(", ");
                buf.append(obj);
            });

            return buf.toString();
        }
    }

    public static String arrayToString(int[] arr) {
        if (arr == null) return "";
        else {
            StringBuilder buf = new StringBuilder(arr.length * 5);

            IntStream.range(0, arr.length).forEachOrdered(i -> {
                int x = arr[i];
                if (i > 0) {
                    buf.append(", ");
                }
                buf.append(x);
            });

            return buf.toString();
        }
    }

    public static Minecraft getMinecraft() {
        return minecraft;
    }

    public static TextureManager getTextureManager() {
        return minecraft.getTextureManager();
    }

    public static IResourceManager getResourceManager() {
        return minecraft.getResourceManager();
    }

    public static InputStream getResourceStream(ResourceLocation location) throws IOException {
        return getResourceStream(minecraft.getResourceManager(), location);
    }

    public static InputStream getResourceStream(IResourceManager resourceManager, ResourceLocation location) throws IOException {
        IResource res = resourceManager.getResource(location);
        return res == null ? null : res.getInputStream();
    }

    public static IResource getResource(ResourceLocation location) throws IOException {
        return minecraft.getResourceManager().getResource(location);
    }

    public static boolean hasResource(ResourceLocation location) {
        try {
            IResource e = getResource(location);
            return e != null;
        } catch (IOException var2) {
            return false;
        }
    }

    public static boolean hasResource(IResourceManager resourceManager, ResourceLocation location) {
        try {
            IResource e = resourceManager.getResource(location);
            return e != null;
        } catch (IOException var3) {
            return false;
        }
    }

    static IResourcePack[] getResourcePacks() {
        ResourcePackRepository rep = minecraft.getResourcePackRepository();
        List entries = rep.getRepositoryEntries();
        ArrayList list = new ArrayList();

        for (Object entry1 : entries) {
            ResourcePackRepository.Entry entry = (ResourcePackRepository.Entry) entry1;
            list.add(entry.getResourcePack());
        }

        if (rep.getResourcePackInstance() != null) list.add(rep.getResourcePackInstance());

        return ((IResourcePack[]) list.toArray(new IResourcePack[0]));
    }

    public static String getResourcePackNames() {
        if (minecraft == null) return "";
        else if (minecraft.getResourcePackRepository() == null) return "";
        else {
            IResourcePack[] rps = getResourcePacks();

            if (rps.length <= 0) return getDefaultResourcePack().getPackName();
            else {
                String[] names = Arrays.stream(rps).map(IResourcePack::getPackName).toArray(String[]::new);

                return arrayToString(names);
            }
        }
    }

    static DefaultResourcePack getDefaultResourcePack() {
        if (defaultResourcePack == null) {
            Minecraft mc = Minecraft.getMinecraft();

            try {
                Field[] repository = mc.getClass().getDeclaredFields();

                for (Field field : repository) {
                    if (field.getType() == DefaultResourcePack.class) {
                        field.setAccessible(true);
                        defaultResourcePack = (DefaultResourcePack) field.get(mc);
                        break;
                    }
                }
            } catch (Exception var4) {
                warn("Error getting default resource pack: " + var4.getClass().getName() + ": " + var4.getMessage());
            }

            if (defaultResourcePack == null) {
                ResourcePackRepository var5 = mc.getResourcePackRepository();

                if (var5 != null) defaultResourcePack = (DefaultResourcePack) var5.rprDefaultResourcePack;
            }
        }

        return defaultResourcePack;
    }

    static boolean isFromDefaultResourcePack(ResourceLocation loc) {
        IResourcePack rp = getDefiningResourcePack(loc);
        return rp == getDefaultResourcePack();
    }

    static IResourcePack getDefiningResourcePack(ResourceLocation loc) {
        IResourcePack[] rps = getResourcePacks();

        for (int i = rps.length - 1; i >= 0; --i) {
            IResourcePack rp = rps[i];

            if (rp.resourceExists(loc)) return rp;
        }

        return getDefaultResourcePack().resourceExists(loc) ? getDefaultResourcePack() : null;
    }

    public static RenderGlobal getRenderGlobal() {
        return minecraft == null ? null : minecraft.renderGlobal;
    }

    public static boolean isBetterGrass() {
        return gameSettings.ofBetterGrass != 3;
    }

    static boolean isBetterGrassFancy() {
        return gameSettings.ofBetterGrass == 2;
    }

    static boolean isWeatherEnabled() {
        return gameSettings.ofWeather;
    }

    public static boolean isSkyEnabled() {
        return gameSettings.ofSky;
    }

    public static boolean isSunMoonEnabled() {
        return gameSettings.ofSunMoon;
    }

    public static boolean isVignetteEnabled() {
        return gameSettings.ofVignette == 0 ? gameSettings.fancyGraphics : gameSettings.ofVignette == 2;
    }

    public static boolean isStarsEnabled() {
        return gameSettings.ofStars;
    }

    public static void sleep(long ms) {
        try {
            Thread.currentThread();
            Thread.sleep(ms);
        } catch (InterruptedException var3) {
            var3.printStackTrace();
        }
    }

    static boolean isTimeDayOnly() {
        return gameSettings.ofTime == 1;
    }

    static boolean isTimeDefault() {
        return gameSettings.ofTime == 0;
    }

    static boolean isTimeNightOnly() {
        return gameSettings.ofTime == 2;
    }

    public static boolean isClearWater() {
        return gameSettings.ofClearWater;
    }

    public static int getAnisotropicFilterLevel() {
        return gameSettings.ofAfLevel;
    }

    public static boolean isAnisotropicFiltering() {
        return getAnisotropicFilterLevel() > 1;
    }

    public static int getAntialiasingLevel() {
        return antialiasingLevel;
    }

    public static boolean isAntialiasing() {
        return getAntialiasingLevel() > 0;
    }

    public static boolean isAntialiasingConfigured() {
        return getGameSettings().ofAaLevel > 0;
    }

    public static boolean isMultiTexture() {
        return getAnisotropicFilterLevel() > 1 || getAntialiasingLevel() > 0;
    }

    public static boolean between(int val, int min, int max) {
        return val >= min && val <= max;
    }

    public static boolean isDrippingWaterLava() {
        return gameSettings.ofDrippingWaterLava;
    }

    public static boolean isBetterSnow() {
        return gameSettings.ofBetterSnow;
    }

    private static Dimension getFullscreenDimension() {
        if (desktopDisplayMode == null) return null;
        else if (gameSettings == null)
            return new Dimension(desktopDisplayMode.getWidth(), desktopDisplayMode.getHeight());
        else {
            String dimStr = gameSettings.ofFullscreenMode;

            if (dimStr.equals("Default"))
                return new Dimension(desktopDisplayMode.getWidth(), desktopDisplayMode.getHeight());
            else {
                AtomicReferenceArray<String> dimStrs = new AtomicReferenceArray<>(tokenize(dimStr, " x"));
                return dimStrs.length() < 2 ? new Dimension(desktopDisplayMode.getWidth(), desktopDisplayMode.getHeight()) : new Dimension(parseInt(dimStrs.get(0), -1), parseInt(dimStrs.get(1), -1));
            }
        }
    }

    public static int parseInt(String str, int defVal) {
        try {
            if (str == null) return defVal;
            else {
                str = str.trim();
                return Integer.parseInt(str);
            }
        } catch (NumberFormatException var3) {
            return defVal;
        }
    }

    public static float parseFloat(String str, float defVal) {
        try {
            if (str == null) return defVal;
            else {
                str = str.trim();
                return Float.parseFloat(str);
            }
        } catch (NumberFormatException var3) {
            return defVal;
        }
    }

    public static boolean parseBoolean(String str, boolean defVal) {
        try {
            if (str == null) return defVal;
            else {
                str = str.trim();
                return Boolean.parseBoolean(str);
            }
        } catch (NumberFormatException var3) {
            return defVal;
        }
    }

    public static String[] tokenize(String str, String delim) {
        StringTokenizer tok = new StringTokenizer(str, delim);
        ArrayList list = new ArrayList();

        while (tok.hasMoreTokens()) {
            String strs = tok.nextToken();
            list.add(strs);
        }

        return ((String[]) list.toArray(new String[0]));
    }

    private static DisplayMode[] getFullscreenDisplayModes() {
        try {
            DisplayMode[] e = Display.getAvailableDisplayModes();

            DisplayMode[] var5 = Arrays.stream(e).filter(comp -> desktopDisplayMode == null || comp.getBitsPerPixel() == desktopDisplayMode.getBitsPerPixel() && comp.getFrequency() == desktopDisplayMode.getFrequency()).toArray(DisplayMode[]::new);
            Comparator var6 = (o1, o2) -> {
                DisplayMode dm1 = (DisplayMode) o1;
                DisplayMode dm2 = (DisplayMode) o2;
                return dm1.getWidth() != dm2.getWidth() ? dm2.getWidth() - dm1.getWidth() : (dm1.getHeight() != dm2.getHeight() ? dm2.getHeight() - dm1.getHeight() : 0);
            };
            Arrays.sort(var5, var6);
            return var5;
        } catch (Exception var4) {
            var4.printStackTrace();
            return new DisplayMode[]{desktopDisplayMode};
        }
    }

    public static String[] getFullscreenModes() {
        DisplayMode[] modes = getFullscreenDisplayModes();
        String[] names = new String[modes.length];

        IntStream.range(0, modes.length).forEachOrdered(i -> {
            DisplayMode mode = modes[i];
            String name = "" + mode.getWidth() + "x" + mode.getHeight();
            names[i] = name;
        });

        return names;
    }

    private static DisplayMode getDisplayMode(Dimension dim) throws LWJGLException {
        DisplayMode[] modes = Display.getAvailableDisplayModes();

        for (DisplayMode dm : modes)
            if (dm.getWidth() == dim.width && dm.getHeight() == dim.height && (desktopDisplayMode == null || dm.getBitsPerPixel() == desktopDisplayMode.getBitsPerPixel() && dm.getFrequency() == desktopDisplayMode.getFrequency()))
                return dm;

        return desktopDisplayMode;
    }

    public static boolean isAnimatedTerrain() {
        return gameSettings.ofAnimatedTerrain;
    }

    static boolean isAnimatedTextures() {
        return gameSettings.ofAnimatedTextures;
    }

    static boolean isSwampColors() {
        return gameSettings.ofSwampColors;
    }

    public static boolean isRandomMobs() {
        return gameSettings.ofRandomMobs;
    }

    static boolean isSmoothBiomes() {
        return gameSettings.ofSmoothBiomes;
    }

    public static boolean isCustomColors() {
        return gameSettings.ofCustomColors;
    }

    static boolean isCustomSky() {
        return gameSettings.ofCustomSky;
    }

    static boolean isCustomFonts() {
        return gameSettings.ofCustomFonts;
    }

    public static boolean isShowCapes() {
        return gameSettings.ofShowCapes;
    }

    public static boolean isConnectedTextures() {
        return gameSettings.ofConnectedTextures != 3;
    }

    public static boolean isNaturalTextures() {
        return gameSettings.ofNaturalTextures;
    }

    static boolean isConnectedTexturesFancy() {
        return gameSettings.ofConnectedTextures == 2;
    }

    public static boolean isFastRender() {
        return gameSettings.ofFastRender;
    }

    public static boolean isTranslucentBlocksFancy() {
        return gameSettings.ofTranslucentBlocks == 0 ? gameSettings.fancyGraphics : gameSettings.ofTranslucentBlocks == 2;
    }

    public static boolean isShaders() {
        return Shaders.shaderPackLoaded;
    }

    public static String[] readLines(InputStream is) throws IOException {
        ArrayList list = new ArrayList();
        InputStreamReader isr = new InputStreamReader(is, "ASCII");
        BufferedReader br = new BufferedReader(isr);

        while (true) {
            String lines = br.readLine();

            if (lines == null) return ((String[]) list.toArray(new String[0]));

            list.add(lines);
        }
    }

    static String readInputStream(InputStream in) throws IOException {
        return readInputStream(in, "ASCII");
    }

    private static String readInputStream(InputStream in, String encoding) throws IOException {
        InputStreamReader inr = new InputStreamReader(in, encoding);
        BufferedReader br = new BufferedReader(inr);
        StringBuffer sb = new StringBuffer();

        while (true) {
            String line = br.readLine();

            if (line == null) return sb.toString();

            sb.append(line);
            sb.append("\n");
        }
    }

    public static void readAll(InputStream is) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buf = new byte[1024];

        while (true) {
            int bytes = is.read(buf);

            if (bytes < 0) {
                is.close();
                byte[] bytes1 = baos.toByteArray();
                return;
            }

            baos.write(buf, 0, bytes);
        }
    }

    public static GameSettings getGameSettings() {
        return gameSettings;
    }

    public static String getNewRelease() {
        return newRelease;
    }

    public static void setNewRelease(String newRelease) {
        newRelease = newRelease;
    }

    static int compareRelease(String rel1, String rel2) {
        String[] rels1 = splitRelease(rel1);
        String[] rels2 = splitRelease(rel2);
        String branch1 = rels1[0];
        String branch2 = rels2[0];

        if (!branch1.equals(branch2)) {
            return branch1.compareTo(branch2);
        } else {
            int rev1 = parseInt(rels1[1], -1);
            int rev2 = parseInt(rels2[1], -1);

            if (rev1 != rev2) return rev1 - rev2;
            else {
                String suf1 = rels1[2];
                String suf2 = rels2[2];

                if (!suf1.equals(suf2)) {
                    if (suf1.isEmpty()) return 1;

                    if (suf2.isEmpty()) return -1;
                }

                return suf1.compareTo(suf2);
            }
        }
    }

    private static String[] splitRelease(String relStr) {
        if (relStr != null && relStr.length() > 0) {
            Pattern p = Pattern.compile("([A-Z])([0-9]+)(.*)");
            Matcher m = p.matcher(relStr);

            if (!m.matches()) return new String[]{"", "", ""};
            else {
                String branch = normalize(m.group(1));
                String revision = normalize(m.group(2));
                String suffix = normalize(m.group(3));
                return new String[]{branch, revision, suffix};
            }
        } else return new String[]{"", "", ""};
    }

    static int intHash(int x) {
        x = x ^ 61 ^ x >> 16;
        x += x << 3;
        x ^= x >> 4;
        x *= 668265261;
        x ^= x >> 15;
        return x;
    }

    static int getRandom(BlockPos blockPos, int face) {
        int rand = intHash(face + 37);
        rand = intHash(rand + blockPos.getX());
        rand = intHash(rand + blockPos.getZ());
        rand = intHash(rand + blockPos.getY());
        return rand;
    }

    static WorldServer getWorldServer() {
        if (minecraft == null) return null;
        else {
            WorldClient world = minecraft.theWorld;

            if (world == null) return null;
            else if (!minecraft.isIntegratedServerRunning()) return null;
            else {
                IntegratedServer is = minecraft.getIntegratedServer();

                if (is == null) return null;
                else {
                    WorldProvider wp = world.provider;

                    if (wp == null) return null;
                    else {
                        int wd = wp.getDimensionId();

                        try {
                            return is.worldServerForDimension(wd);
                        } catch (NullPointerException var5) {
                            return null;
                        }
                    }
                }
            }
        }
    }

    static int getAvailableProcessors() {
        return availableProcessors;
    }

    public static void updateAvailableProcessors() {
        availableProcessors = Runtime.getRuntime().availableProcessors();
    }

    public static boolean isSingleProcessor() {
        return getAvailableProcessors() <= 1;
    }

    public static boolean isSmoothWorld() {
        return gameSettings.ofSmoothWorld;
    }

    public static boolean isDynamicFov() {
        return gameSettings.ofDynamicFov;
    }

    static int getChunkViewDistance() {
        if (gameSettings == null) return 10;
        else {
            return gameSettings.renderDistanceChunks;
        }
    }

    public static boolean equals(Object o1, Object o2) {
        return o1 == o2 || (o1 == null ? false : o1.equals(o2));
    }

    public static String normalize(String s) {
        return s == null ? "" : s;
    }

    private static void checkDisplaySettings() {
        int samples = getAntialiasingLevel();

        if (samples > 0) {
            DisplayMode displayMode = Display.getDisplayMode();
            dbg("FSAA Samples: " + samples);

            try {
                Display.destroy();
                Display.setDisplayMode(displayMode);
                Display.create((new PixelFormat()).withDepthBits(24).withSamples(samples));
                Display.setResizable(false);
                Display.setResizable(true);
            } catch (LWJGLException var15) {
                warn("Error setting FSAA: " + samples + "x");
                var15.printStackTrace();

                try {
                    Display.setDisplayMode(displayMode);
                    Display.create((new PixelFormat()).withDepthBits(24));
                    Display.setResizable(false);
                    Display.setResizable(true);
                } catch (LWJGLException var14) {
                    var14.printStackTrace();

                    try {
                        Display.setDisplayMode(displayMode);
                        Display.create();
                        Display.setResizable(false);
                        Display.setResizable(true);
                    } catch (LWJGLException var13) {
                        var13.printStackTrace();
                    }
                }
            }

            if (!Minecraft.isRunningOnMac && getDefaultResourcePack() != null) {
                InputStream var2 = null;
                InputStream var3 = null;

                try {
                    var2 = getDefaultResourcePack().func_152780_c(new ResourceLocation("icons/icon_16x16.png"));
                    var3 = getDefaultResourcePack().func_152780_c(new ResourceLocation("icons/icon_32x32.png"));

                    if (var2 != null && var3 != null)
                        Display.setIcon(new ByteBuffer[]{readIconImage(var2), readIconImage(var3)});
                } catch (IOException var11) {
                    warn("Error setting window icon: " + var11.getClass().getName() + ": " + var11.getMessage());
                } finally {
                    IOUtils.closeQuietly(var2);
                    IOUtils.closeQuietly(var3);
                }
            }
        }
    }

    private static ByteBuffer readIconImage(InputStream is) throws IOException {
        BufferedImage var2 = ImageIO.read(is);
        int[] var3 = var2.getRGB(0, 0, var2.getWidth(), var2.getHeight(), null, 0, var2.getWidth());
        ByteBuffer var4 = ByteBuffer.allocate(4 * var3.length);

        Arrays.stream(var3).map(var8 -> var8 << 8 | var8 >> 24 & 255).forEachOrdered(var4::putInt);

        var4.flip();
        return var4;
    }

    public static void checkDisplayMode() {
        try {
            if (minecraft.isFullScreen()) {
                if (fullscreenModeChecked) return;

                fullscreenModeChecked = true;
                desktopModeChecked = false;
                DisplayMode e = Display.getDisplayMode();
                Dimension dim = getFullscreenDimension();

                if (dim == null || e.getWidth() == dim.width && e.getHeight() == dim.height) return;

                DisplayMode newMode = getDisplayMode(dim);

                if (newMode == null) return;

                Display.setDisplayMode(newMode);
                minecraft.displayWidth = Display.getDisplayMode().getWidth();
                minecraft.displayHeight = Display.getDisplayMode().getHeight();

                if (minecraft.displayWidth <= 0) minecraft.displayWidth = 1;

                if (minecraft.displayHeight <= 0) minecraft.displayHeight = 1;

                if (minecraft.currentScreen != null) {
                    ScaledResolution sr = new ScaledResolution(minecraft, minecraft.displayWidth, minecraft.displayHeight);
                    int sw = sr.getScaledWidth();
                    int sh = sr.getScaledHeight();
                    minecraft.currentScreen.setWorldAndResolution(minecraft, sw, sh);
                }

                minecraft.loadingScreen = new LoadingScreenRenderer(minecraft);
                updateFramebufferSize();
                Display.setFullscreen(true);
                minecraft.gameSettings.updateVSync();
                GlStateManager.func_179098_w();
            } else {
                if (desktopModeChecked) return;

                desktopModeChecked = true;
                fullscreenModeChecked = false;
                minecraft.gameSettings.updateVSync();
                Display.update();
                GlStateManager.func_179098_w();
                Display.setResizable(false);
                Display.setResizable(true);
            }
        } catch (Exception var6) {
            var6.printStackTrace();
        }
    }

    public static void updateFramebufferSize() {
        minecraft.getFramebuffer().createBindFramebuffer(minecraft.displayWidth, minecraft.displayHeight);

        if (minecraft.entityRenderer != null)
            minecraft.entityRenderer.updateShaderGroupSize(minecraft.displayWidth, minecraft.displayHeight);
    }

    public static Object[] addObjectToArray(Object[] arr, Object obj) {
        if (arr == null) throw new NullPointerException("The given array is NULL");
        else {
            int arrLen = arr.length;
            int newLen = arrLen + 1;
            Object[] newArr = ((Object[]) Array.newInstance(arr.getClass().getComponentType(), newLen));
            System.arraycopy(arr, 0, newArr, 0, arrLen);
            newArr[arrLen] = obj;
            return newArr;
        }
    }

    public static Object[] addObjectToArray(Object[] arr, Object obj, int index) {
        ArrayList list = new ArrayList(Arrays.asList(arr));
        list.add(index, obj);
        Object[] newArr = ((Object[]) Array.newInstance(arr.getClass().getComponentType(), list.size()));
        return list.toArray(newArr);
    }

    static Object[] addObjectsToArray(Object[] arr, Object[] objs) {
        if (arr == null) throw new NullPointerException("The given array is NULL");
        else if (objs.length == 0) return arr;
        else {
            int arrLen = arr.length;
            int newLen = arrLen + objs.length;
            Object[] newArr = ((Object[]) Array.newInstance(arr.getClass().getComponentType(), newLen));
            System.arraycopy(arr, 0, newArr, 0, arrLen);
            System.arraycopy(objs, 0, newArr, arrLen, objs.length);
            return newArr;
        }
    }

    public static boolean isCustomItems() {
        return gameSettings.ofCustomItems;
    }

    public static void drawFps() {
        int fps = Minecraft.func_175610_ah();
        String updates = getUpdates(minecraft.debug);
        int renderersActive = minecraft.renderGlobal.getCountActiveRenderers();
        int entities = minecraft.renderGlobal.getCountEntitiesRendered();
        int tileEntities = minecraft.renderGlobal.getCountTileEntitiesRendered();
        String fpsStr = "" + fps + " fps, C: " + renderersActive + ", E: " + entities + "+" + tileEntities + ", U: " + updates;
        minecraft.fontRendererObj.drawString(fpsStr, 2, 2, -2039584);
    }

    private static String getUpdates(String str) {
        int pos1 = str.indexOf(40);

        if (pos1 < 0) return "";
        else {
            int pos2 = str.indexOf(32, pos1);
            return pos2 < 0 ? "" : str.substring(pos1 + 1, pos2);
        }
    }

    public static int getBitsOs() {
        String progFiles86 = System.getenv("ProgramFiles(X86)");
        return progFiles86 != null ? 64 : 32;
    }

    public static int getBitsJre() {
        String[] propNames = new String[]{"sun.arch.data.model", "com.ibm.vm.bitmode", "os.arch"};

        for (String propName : propNames) {
            String propVal = System.getProperty(propName);

            if (propVal != null && propVal.contains("64")) return 64;
        }

        return 32;
    }

    public static boolean isNotify64BitJava() {
        return notify64BitJava;
    }

    public static void setNotify64BitJava(boolean flag) {
        notify64BitJava = flag;
    }

    public static void showGuiMessage(String line1, String line2) {
        GuiMessage gui = new GuiMessage(minecraft.currentScreen, line1, line2);
        minecraft.displayGuiScreen(gui);
    }

    static int[] addIntToArray(int[] intArray, int intValue) {
        return addIntsToArray(intArray, new int[]{intValue});
    }

    private static int[] addIntsToArray(int[] intArray, int[] copyFrom) {
        if (intArray != null && copyFrom != null) {
            int arrLen = intArray.length;
            int newLen = arrLen + copyFrom.length;
            int[] newArray = new int[newLen];
            System.arraycopy(intArray, 0, newArray, 0, arrLen);

            System.arraycopy(copyFrom, 0, newArray, arrLen, copyFrom.length);

            return newArray;
        } else throw new NullPointerException("The given array is NULL");
    }

    public static DynamicTexture getMojangLogoTexture(DynamicTexture texDefault) {
        try {
            ResourceLocation e = new ResourceLocation("textures/gui/title/mojang.png");
            InputStream in = getResourceStream(e);

            if (in == null) return texDefault;
            else {
                BufferedImage bi = ImageIO.read(in);

                if (bi == null) return texDefault;
                else {
                    return new DynamicTexture(bi);
                }
            }
        } catch (Exception var5) {
            warn(var5.getClass().getName() + ": " + var5.getMessage());
            return texDefault;
        }
    }

    public static void writeFile(File file, String str) throws IOException {
        FileOutputStream fos = new FileOutputStream(file);
        byte[] bytes = str.getBytes(StandardCharsets.US_ASCII);
        fos.write(bytes);
        fos.close();
    }

    public static boolean isDynamicLights() {
        return gameSettings.ofDynamicLights != 3;
    }

    static boolean isDynamicLightsFast() {
        return gameSettings.ofDynamicLights == 1;
    }

    static boolean isDynamicHandLight() {
        return isDynamicLights() && (!isShaders() || Shaders.isDynamicHandLight());
    }
}