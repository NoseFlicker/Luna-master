package space.coffos.lija.impl.gui.notifications;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ResourceLocation;
import space.coffos.lija.util.math.Timer;
import space.coffos.lija.util.render.ColorUtils;

import java.awt.*;

import static space.coffos.lija.api.element.Element.mc;

/*
 * @author TheSlowly, modified by Zhn17.
 */

public class ClientNotification {

    private String message;
    private Timer timer;
    private double lastY, posY, width, height, animationX;
    private int color = new Color(29, 29, 29).getRGB(), imageWidth;
    private ResourceLocation image;
    private long stayTime;

    ClientNotification(String message, int time, Type type) {
        this.message = message;
        timer = new Timer();
        timer.reset();
        width = mc.fontRendererObj.getStringWidth(message) + 35;
        height = 19;
        animationX = width;
        stayTime = time;
        imageWidth = 13;
        posY = -1;
        image = new ResourceLocation("luna/icon/" + type.name().toLowerCase() + ".png");
    }

    public void draw(double getY, double lastY) {
        this.lastY = lastY;
        animationX = NotificationUtil.getAnimationState(animationX, isFinished() ? width : 0, Math.max(isFinished() ? 20 * 50 : 30, Math.abs(animationX - (isFinished() ? 50 : 0)) * 10));
        posY = posY == -1 ? getY : NotificationUtil.getAnimationState(posY, getY, 250);
        ScaledResolution res = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
        int x1 = (int) (res.getScaledWidth() - width + animationX), x2 = (int) (res.getScaledWidth() + animationX), y1 = (int) posY, y2 = (int) (y1 + height);
        Gui.drawRect(x1, y1, x2, y2, color);
        Gui.drawRect(x1, y2, x2, y2 + 0.5F, color);
        Gui.drawRect(x1, y2, x2, y2 + 0.5F, NotificationUtil.INSTANCE.reAlpha(1, 0.5F));

        Gui.drawRect(x1, y1, (int) (x1 + height / 2 - 7), y2 + 0.5f, ColorUtils.getColor(ColorUtils.ColorChoices.MAIN));
        NotificationUtil.INSTANCE.drawImage(image, (int) (x1 + (height - imageWidth) / 2F), y1 + (int) ((height - imageWidth) / 2F), imageWidth, imageWidth);

        NotificationUtil.INSTANCE.drawCenteredString(message, (float) (x1 + width / 2F) + 10, (float) (y1 + height / 3.5F), -1);
    }

    boolean shouldDelete() {
        return isFinished() && animationX >= width;
    }

    private boolean isFinished() {
        return timer.hasReached(stayTime) && posY == lastY;
    }

    public double getHeight() {
        return height;
    }

    public enum Type {SUCCESS, INFO, WARNING, ERROR}
}