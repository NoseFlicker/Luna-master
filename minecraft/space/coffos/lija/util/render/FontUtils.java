package space.coffos.lija.util.render;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.StringUtils;

import static space.coffos.lija.api.element.Element.mc;

public class FontUtils {

    private static FontRenderer fontRenderer = mc.fontRendererObj;

    public static int getStringWidth(String text) {
        return fontRenderer.getStringWidth(StringUtils.stripControlCodes(text));
    }

    public static int getFontHeight() {
        return fontRenderer.FONT_HEIGHT;
    }

    public static void drawString(String text, double x, double y, int color) {
        fontRenderer.drawString(text, (int) x, (int) y, color);
    }

    public static void drawStringWithShadow(String text, double x, double y, int color) {
        fontRenderer.func_175063_a(text, (float) x, (float) y, color);
    }

    public static void drawCenteredString(String text, double x, double y, int color) {
        drawString(text, x - (fontRenderer.getStringWidth(text) >> 1), y, color);
    }
}