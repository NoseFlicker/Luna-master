package space.coffos.lija.impl.elements.luna.render.hud;

import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;
import space.coffos.lija.LiJA;
import space.coffos.lija.api.element.Category;
import space.coffos.lija.api.element.Element;
import space.coffos.lija.api.element.ElementStructure;
import space.coffos.lija.api.event.EventRegister;
import space.coffos.lija.api.setting.BooleanHandler;
import space.coffos.lija.api.setting.DoubleHandler;
import space.coffos.lija.api.setting.ModeHandler;
import space.coffos.lija.api.setting.Setting;
import space.coffos.lija.impl.elements.luna.render.NameProtect;
import space.coffos.lija.impl.elements.luna.render.hud.structure.Armor;
import space.coffos.lija.impl.elements.luna.render.hud.structure.TabGui;
import space.coffos.lija.impl.elements.luna.render.hud.structure.ToggledMods;
import space.coffos.lija.impl.events.EventRender2D;
import space.coffos.lija.impl.events.EventUpdate;
import space.coffos.lija.impl.managers.FontManager;
import space.coffos.lija.util.entity.PlayerUtils;
import space.coffos.lija.util.render.ColorUtils;
import space.coffos.lija.util.render.FontUtils;
import space.coffos.lija.util.render.RenderUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@ElementStructure(name = "HUD", category = Category.RENDER)
public class HUD extends Element {

    @ModeHandler(name = "Theme", currentOption = "Luna", options = {"Luna", "Modern"}, locked = false)
    public static Setting theme;

    @DoubleHandler(name = "Red", currentValue = 255, minValue = 0, maxValue = 255, onlyInt = true, locked = false)
    public static Setting red;

    @DoubleHandler(name = "Green", currentValue = 0, minValue = 0, maxValue = 255, onlyInt = true, locked = false)
    public static Setting green;

    @DoubleHandler(name = "Blue", currentValue = 93, minValue = 0, maxValue = 255, onlyInt = true, locked = false)
    public static Setting blue;

    @BooleanHandler(name = "ArrayList", booleanValue = true)
    public static Setting arrayList;
    private ToggledMods toggledMods = new ToggledMods();

    @BooleanHandler(name = "TabGui", booleanValue = false)
    public static Setting tabGui;
    private TabGui tabGui1 = new TabGui();

    @BooleanHandler(name = "Armor", booleanValue = true)
    public static Setting armor;
    private Armor armor1 = new Armor();

    @BooleanHandler(name = "Rainbow", booleanValue = true)
    public static Setting rainbowArrayList;

    @BooleanHandler(name = "Notifications", booleanValue = false)
    public static Setting notifications;

    @BooleanHandler(name = "FPS-Helper", booleanValue = false)
    public static Setting fps;

    @ModeHandler(name = "Rectangle", currentOption = "Slim", options = {"Slim", "None"}, locked = false)
    public static Setting rectangle;

    @BooleanHandler(name = "Hotbar", booleanValue = true)
    public static Setting hotBar;

    @EventRegister
    public void onRender2D(EventRender2D e) {
        final String clientName = "Luna";
        if (!tabGui.getValBoolean() & !theme.getValString().equalsIgnoreCase("Modern"))
            FontManager.comfortaaStandard.drawStringWithShadow(ChatFormatting.WHITE + clientName + ChatFormatting.RESET + " " + LiJA.INSTANCE.currentFormat + LiJA.INSTANCE.build[1], 2, 1, ColorUtils.getColor(ColorUtils.ColorChoices.MAIN));
        if (!(mc.currentScreen instanceof GuiChat)) {
            if (!theme.getValString().equalsIgnoreCase("Modern")) {
                GlStateManager.pushMatrix();
                GlStateManager.scale(0.55, 0.55, 0.55);
                FontUtils.drawStringWithShadow(String.format("LUNA%s", LiJA.INSTANCE.build[1].replace(" ", "")), 2, 40, -1);
                FontUtils.drawStringWithShadow(String.format("TIMER SPEED: %s", mc.timer.timerSpeed), 2, 55, -1);
                GlStateManager.popMatrix();
            }
            /*
             * New method for item rectangles:
             */
            if (hotBar.getValBoolean()) {
                RenderUtils.R2DUtils.drawBorderedRect((float) (GuiIngame.animatedWidth - 11), PlayerUtils.getScaledRes().getScaledHeight() - 22, (float) (GuiIngame.animatedWidth + 11), PlayerUtils.getScaledRes().getScaledHeight(), 1, 0x25000000, theme.getValString().equalsIgnoreCase("Modern") ? -1 : ColorUtils.getColor(ColorUtils.ColorChoices.MAIN));
                RenderUtils.R2DUtils.drawBorderedRect((float) (GuiIngame.animatedWidth - 11), PlayerUtils.getScaledRes().getScaledHeight() - 22, (float) (GuiIngame.animatedWidth + 11), PlayerUtils.getScaledRes().getScaledHeight(), 0, 0x25000000, theme.getValString().equalsIgnoreCase("Modern") ? -1 : ColorUtils.getColor(ColorUtils.ColorChoices.MAIN));

                FontManager.main.drawStringWithShadow(clientName, 5.0F, GuiMainMenu.height - 23.3f, theme.getValString().equalsIgnoreCase("Modern") ? -1 : ColorUtils.getColor(ColorUtils.ColorChoices.MAIN));


                /* Draws the players name onto the HotBar */
                FontManager.bebasMedium.drawStringWithShadow(LiJA.INSTANCE.elementManager.getElement(NameProtect.class).isToggled() ? "§7Name: §8" + NameProtect.name : "§7Name: §8" + mc.thePlayer.getName(), 55.0F, GuiMainMenu.height - 22f, theme.getValString().equalsIgnoreCase("Modern") ? -1 : ColorUtils.getColor(ColorUtils.ColorChoices.MAIN));

                FontManager.bebasMedium.drawStringWithShadow("§7FPS: §8" + mc.debugFPS, 55.0F, GuiMainMenu.height - 12f, theme.getValString().equalsIgnoreCase("Modern") ? -1 : ColorUtils.getColor(ColorUtils.ColorChoices.MAIN));
                GL11.glDisable(GL11.GL_LINE_SMOOTH);
            }
        }

        if (theme.getValString().equalsIgnoreCase("Modern")) {
            GL11.glPushMatrix();
            GlStateManager.scale(1.55, 1.55, 1.55);
            LiJA.INSTANCE.fontManager.ico_regular.drawStringWithShadow("Luna", 2, 1, -1);
            LiJA.INSTANCE.fontManager.ico_pic.drawStringWithShadow("L", 2, 20, -1);
            GL11.glPopMatrix();
        }
        if (hotBar.getValBoolean()) {
            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy"); /* Year, Month, Day */
            Date today = Calendar.getInstance().getTime();
            String renderDate = dateFormat.format(today);
            DateFormat dff = new SimpleDateFormat("HH:mm:ss"); /* Hour, Minute, Second */
            Date today2 = Calendar.getInstance().getTime();
            String renderTime = dff.format(today2);

            /*
             * Draws the date
             */

            FontManager.bebasMedium.drawStringWithShadow("§7" + renderDate, (float) (PlayerUtils.getScaledRes().getScaledWidth() - FontManager.bebasMedium.getStringWidth("§7" + renderDate) - 4.5), PlayerUtils.getScaledRes().getScaledHeight() - 22, ColorUtils.getColor(ColorUtils.ColorChoices.MAIN));
            FontManager.bebasMedium.drawStringWithShadow("§7" + renderTime, (float) (PlayerUtils.getScaledRes().getScaledWidth() - FontManager.bebasMedium.getStringWidth("§7" + renderTime) - 4.5), PlayerUtils.getScaledRes().getScaledHeight() - 12, ColorUtils.getColor(ColorUtils.ColorChoices.MAIN));
        }
    }

    @EventRegister
    public void onLateUpdate(EventUpdate e) {
        if (isToggled()) {
            if (mc.thePlayer.isUsingItem() | mc.thePlayer.isEating())
                if (mc.gameSettings.keyBindAttack.pressed) mc.thePlayer.swingItem();
            if (fps.getValBoolean()) {
                mc.gameSettings.renderDistanceChunks = 7;
                mc.gameSettings.enableVsync = true;
                mc.gameSettings.fancyGraphics = false;
                mc.gameSettings.ofFogStart = 0.8f;
                mc.gameSettings.ofAnimatedLava = 1;
                mc.gameSettings.ofSmoothFps = false;
                mc.gameSettings.particleSetting = 2;
                mc.gameSettings.ofFastMath = false;
                mc.gameSettings.touchscreen = false;
                mc.gameSettings.ofRain = 1;
                mc.gameSettings.ofClouds = 1;
                mc.gameSettings.ofCloudsHeight = 256;
                mc.gameSettings.clouds = false;
                if (!mc.isSingleplayer())
                    mc.loadingScreen = null;
            }
        }
    }

    @Override
    public void onEnable() {
        super.onEnable();
        LiJA.INSTANCE.eventManager.register(toggledMods);
        LiJA.INSTANCE.eventManager.register(armor1);
        LiJA.INSTANCE.eventManager.register(tabGui1);
    }

    @Override
    public void onDisable() {
        super.onDisable();
        LiJA.INSTANCE.eventManager.unregister(toggledMods);
        LiJA.INSTANCE.eventManager.unregister(armor1);
        LiJA.INSTANCE.eventManager.unregister(tabGui1);
    }
}