package space.coffos.lija.impl.elements.luna.render;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.opengl.GL11;
import space.coffos.lija.api.element.Category;
import space.coffos.lija.api.element.Element;
import space.coffos.lija.api.element.ElementStructure;
import space.coffos.lija.api.event.EventRegister;
import space.coffos.lija.api.setting.BooleanHandler;
import space.coffos.lija.api.setting.DoubleHandler;
import space.coffos.lija.api.setting.ModeHandler;
import space.coffos.lija.api.setting.Setting;
import space.coffos.lija.impl.events.EventRender3D;
import space.coffos.lija.util.render.RenderUtils;

import java.awt.*;
import java.util.function.IntFunction;
import java.util.stream.IntStream;

@ElementStructure(name = "ESP", category = Category.RENDER, clientType = "Luna")
public class ESP extends Element {

    @ModeHandler(name = "Mode", currentOption = "Outline", options = {"Outline", "CSGO", "Fill"}, locked = false)
    public static Setting mode;

    @ModeHandler(name = "Fill Type", currentOption = "Custom", options = {"Custom", "Rainbow"}, locked = true)
    public static Setting colorType;

    @DoubleHandler(name = "Fill Red", currentValue = 255, maxValue = 255, minValue = 0, onlyInt = true, locked = true)
    public static Setting fillColorR;

    @DoubleHandler(name = "Fill Red", currentValue = 255, maxValue = 255, minValue = 0, onlyInt = true, locked = true)
    public static Setting fillColorG;

    @DoubleHandler(name = "Fill Red", currentValue = 255, maxValue = 255, minValue = 0, onlyInt = true, locked = true)
    public static Setting fillColorB;

    @BooleanHandler(name = "Chests", booleanValue = true)
    public static Setting chests;

    @EventRegister
    public void onRender(EventRender3D e) {
        if (mode.getValString().equalsIgnoreCase("Fill") & fillColorR.isLockedDouble() & fillColorG.isLockedDouble() & fillColorB.isLockedDouble() & colorType.getValString().equalsIgnoreCase("Custom")) {
            fillColorR.setLockedDouble(false);
            fillColorG.setLockedDouble(false);
            fillColorB.setLockedDouble(false);
            colorType.setLockedMode(false);
        } else if (!mode.getValString().equalsIgnoreCase("Fill") & !fillColorR.isLockedDouble() & !fillColorG.isLockedDouble() & !fillColorB.isLockedDouble() | !colorType.getValString().equalsIgnoreCase("Custom")) {
            fillColorR.setLockedDouble(true);
            fillColorG.setLockedDouble(true);
            fillColorB.setLockedDouble(true);
            colorType.setLockedMode(true);
        }
        colorType.setLockedMode(!mode.getValString().equalsIgnoreCase("Fill"));
        if (mode.getValString().equalsIgnoreCase("CSGO")) {
            if (!isToggled()) return;

            IntStream.range(0, mc.theWorld.loadedEntityList.size()).mapToObj((IntFunction<Object>) mc.theWorld.loadedEntityList::get).filter(o -> o instanceof EntityLivingBase).map(o -> (EntityLivingBase) o).filter(ent -> ent != mc.thePlayer & ent instanceof EntityPlayer & !ent.isInvisible()).forEachOrdered(ent -> {
                float posX = (float) ((float) ent.lastTickPosX + (ent.posX - ent.lastTickPosX) * mc.timer.renderPartialTicks);
                float posY = (float) ((float) ent.lastTickPosY + (ent.posY - ent.lastTickPosY) * mc.timer.renderPartialTicks);
                float posZ = (float) ((float) ent.lastTickPosZ + (ent.posZ - ent.lastTickPosZ) * mc.timer.renderPartialTicks);
                drawESP(posX - RenderManager.renderPosX, posY - RenderManager.renderPosY, posZ - RenderManager.renderPosZ);
            });
        }
    }

    private static void drawESP(double posX, double posY, double posZ) {
        GlStateManager.pushMatrix();
        GlStateManager.translate(posX, posY, posZ);
        GL11.glNormal3f(0.0F, 0.0F, 0.0F);
        GlStateManager.rotate(-RenderManager.playerViewY, 0.0F, 1.0F, 0.0F);
        GlStateManager.scale(-0.1D, -0.1D, 0.1D);
        GL11.glDisable(2896);
        GL11.glDisable(2929);
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GlStateManager.func_179098_w();
        GlStateManager.depthMask(true);

        drawRectCorner(-7.25F, -20.6F, -1.0F, -20.5F, new Color(0, 0, 0).getRGB());
        drawRectCorner(6.25F, -20.6F, -1.0F, -20.5F, new Color(0, 0, 0).getRGB());
        drawRectCorner(-7.25F, -20.5F, -7.1F, 2.5F, new Color(0, 0, 0).getRGB());
        drawRectCorner(-7.25F, 2.5F, 6.5F, 2.6F, new Color(0, 0, 0).getRGB());
        drawRectCorner(6.25f, -2.5F, 6.35F, 2.75f, new Color(0, 0, 0).getRGB());
        drawRectCorner(6.25F, -1.0F, 6.35F, -20.5F, new Color(0, 0, 0).getRGB());

        GL11.glDisable(3042);
        GL11.glEnable(2929);
        GlStateManager.popMatrix();
    }

    private static void drawRectCorner(float x, float y, float x1, float y1, int color) {
        RenderUtils.enableGL2DESP();
        Gui.drawRect(x, y, x1, y1, color);
        RenderUtils.disableGL2DESP();
    }
}