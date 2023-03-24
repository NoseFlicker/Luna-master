package space.coffos.lija.impl.managers;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import space.coffos.lija.impl.gui.LiJAFontRenderer;

import java.awt.*;
import java.io.InputStream;

public class FontManager {

    public static LiJAFontRenderer standard = new LiJAFontRenderer(getFont(), true, 8);
    public static LiJAFontRenderer bebasMedium = new LiJAFontRenderer(getFontBebas(48), true, 8);
    public static LiJAFontRenderer bebasStandardGUI = new LiJAFontRenderer(getFontBebas((int) 43.5), true, 8);
    public static LiJAFontRenderer bebasMinGUI = new LiJAFontRenderer(getFontBebas((int) 45.5), true, 8);
    public static LiJAFontRenderer comfortaaStandard = new LiJAFontRenderer(getFont3(70), true, 8);
    public static LiJAFontRenderer main = new LiJAFontRenderer(getLiesFont(), true, 8);

    /* New fonts */
    public LiJAFontRenderer ico_regular = new LiJAFontRenderer(getFontFromLocation("luna/i_r.ttf", 85), true, 8);
    public LiJAFontRenderer ico_regularT2 = new LiJAFontRenderer(getFontFromLocation("luna/i_r.ttf", 48), true, 8);
    public LiJAFontRenderer ico_pic = new LiJAFontRenderer(getFontFromLocation("luna/i_ip.ttf", 85), true, 8);

    public static LiJAFontRenderer comfortaaSmall = new LiJAFontRenderer(getFont3(26), true, 8);
    public static LiJAFontRenderer comfortaaMedium = new LiJAFontRenderer(getFont3(36), true, 8);

    // Comfortaa for the main menu.
    public LiJAFontRenderer comfortaa_title = new LiJAFontRenderer(getFontFromLocation("C.ttf", 175), true, 8);
    public LiJAFontRenderer comfortaa_sub = new LiJAFontRenderer(getFontFromLocation("C.ttf", 155), true, 8);

    private static Font getFont() {
        Font font;
        try {
            InputStream is = Minecraft.getMinecraft().getResourceManager().getResource(new ResourceLocation("luna/Comfortaa_Regular.ttf")).getInputStream();
            font = Font.createFont(0, is);
            font = font.deriveFont(Font.PLAIN, 36);
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Error loading font");
            font = new Font("default", Font.PLAIN, 36);
        }
        return font;
    }

    private static Font getLiesFont() {
        Font font;
        try {
            InputStream is = Minecraft.getMinecraft().getResourceManager().getResource(new ResourceLocation("luna/Lies.ttf")).getInputStream();
            font = Font.createFont(0, is);
            font = font.deriveFont(Font.PLAIN, 85);
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Error loading font");
            font = new Font("default", Font.PLAIN, 85);
        }
        return font;
    }

    private Font getFontFromLocation(String location, int size) {
        Font font;
        try {
            InputStream is = Minecraft.getMinecraft().getResourceManager().getResource(new ResourceLocation(location)).getInputStream();
            font = Font.createFont(0, is);
            font = font.deriveFont(Font.PLAIN, size);
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Error loading font");
            font = new Font("default", Font.PLAIN, size);
        }
        return font;
    }

    public static Font getFont3(int size) {
        Font font;
        try {
            InputStream is = Minecraft.getMinecraft().getResourceManager().getResource(new ResourceLocation("C.ttf")).getInputStream();
            font = Font.createFont(0, is);
            font = font.deriveFont(Font.PLAIN, size);
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Error loading font");
            font = new Font("default", Font.PLAIN, size);
        }
        return font;
    }

    private static Font getFontBebas(int size) {
        Font font;
        try {
            InputStream is = Minecraft.getMinecraft().getResourceManager().getResource(new ResourceLocation("luna/bebas.ttf")).getInputStream();
            font = Font.createFont(0, is);
            font = font.deriveFont(Font.PLAIN, size);
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Error loading font");
            font = new Font("default", Font.PLAIN, size);
        }
        return font;
    }
}