package space.coffos.lija.impl.gui.notifications;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import space.coffos.lija.api.element.Element;
import space.coffos.lija.impl.managers.FontManager;

import java.awt.*;
import java.util.ArrayList;

public enum NotificationUtil {

    /* @author TheSlowly, FlatIcons from https://www.flaticon.com/packs/alerts */
    INSTANCE;

    public static ArrayList<ClientNotification> notifications = new ArrayList<>();

    public int reAlpha(int color, float alpha) {
        Color c = new Color(color);
        float r = ((float) 1 / 255) * c.getRed();
        float g = ((float) 1 / 255) * c.getGreen();
        float b = ((float) 1 / 255) * c.getBlue();
        return new Color(r, g, b, alpha).getRGB();
    }

    public void drawNotifications() {
        ScaledResolution res = new ScaledResolution(Minecraft.getMinecraft(), Minecraft.getMinecraft().displayWidth, Minecraft.getMinecraft().displayHeight);
        double startY = res.getScaledHeight() - 45;
        final double lastY = startY;
        for (int i = 0; i < notifications.size(); i++) {
            ClientNotification not = notifications.get(i);
            if (not.shouldDelete())
                notifications.remove(i);
            not.draw(startY, lastY);
            startY -= not.getHeight() + 1;
        }
    }

    public static void sendClientMessage(String message, int time, ClientNotification.Type type) {
        notifications.add(new ClientNotification(message, time, type));
    }

    public static double getAnimationState(double animation, double finalState, double speed) {
        float add = (float) (0.01 * speed);
        if (animation < finalState) if (animation + add < finalState)
            animation += add;
        else
            animation = finalState;
        else if (animation - add > finalState)
            animation -= add;
        else
            animation = finalState;
        return animation;
    }

    public void drawImage(ResourceLocation image, int x, int y, int width, int height) {
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDepthMask(false);
        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        Minecraft.getMinecraft().getTextureManager().bindTexture(image);
        Gui.drawModalRectWithCustomSizedTexture(x, y, 0, 0, width, height, width, height);
        GL11.glDepthMask(true);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
    }

    public void drawCenteredString(String text, float x, float y, int color) {
        drawString(text, x - (Element.mc.fontRendererObj.getStringWidth(text) >> 1), y, color);
    }

    public int drawString(String text, float x, float y, int color) {
        return FontManager.bebasMinGUI.drawStringWithShadow(text, x, y - 1.5f, color);
    }
}