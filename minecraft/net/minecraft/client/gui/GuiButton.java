package net.minecraft.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StringUtils;
import space.coffos.lija.util.general.dynamic.DynamicVariables;
import space.coffos.lija.util.render.RenderUtils;
import space.coffos.lija.util.render.TransitionUtils;

import java.awt.*;

import static space.coffos.lija.impl.managers.FontManager.standard;

public class GuiButton extends Gui {

    static final ResourceLocation buttonTextures = new ResourceLocation("textures/gui/widgets.png");
    protected int width;
    protected int height;
    public int xPosition;
    public int yPosition;
    public String displayString;

    private int transition;

    public int id;
    public boolean enabled;
    public boolean visible;
    protected boolean hovered;

    public GuiButton(int buttonId, int x, int y, String buttonText) {
        this(buttonId, x, y, 200, 20, buttonText);
    }

    public GuiButton(int buttonId, int x, int y, int widthIn, int heightIn, String buttonText) {
        this.width = 200;
        this.height = 20;
        this.enabled = true;
        this.visible = true;
        this.id = buttonId;
        this.xPosition = x;
        this.yPosition = y;
        this.width = widthIn;
        this.height = heightIn;
        this.displayString = buttonText;
    }

    public GuiButton() {
    }

    protected int getHoverState(boolean mouseOver) {
        byte var2 = 1;
        if (!enabled) var2 = 0;
        else if (mouseOver) var2 = 2;
        return var2;
    }

    private int getTransition() {
        return transition;
    }

    public void drawButton(Minecraft mc, int mouseX, int mouseY) {
        if (visible) {
            mc.getTextureManager().bindTexture(buttonTextures);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            hovered = mouseX >= xPosition && mouseY >= yPosition && mouseX < xPosition + width && mouseY < yPosition + height;
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
            GlStateManager.blendFunc(770, 771);
            if (getTransition() > 0) transition = (getTransition() - 1);
            if (!enabled)
                RenderUtils.R2DUtils.drawBorderedRect(xPosition, yPosition, xPosition + width, yPosition + height, 1, new Color(0, 0, 0, 120).getRGB(), new Color(0, 0, 0).getRGB());
            else if (hovered) {
                // FIXME: Make a dynamic variable for each button with a boolean value, checking if the specified button is being hovered.
                //DynamicVariables.initVar("T:" + this.displayString, new TransitionUtils(), false, false);
                RenderUtils.R2DUtils.drawBorderedRect(xPosition, yPosition, xPosition + width, yPosition + height, 0.5f, new Color(0, 0, 0, 120).getRGB(), new Color(195, 61, 255).getRGB());
                //System.out.println(((TransitionUtils) DynamicVariables.pullVar("T:" + this.displayString, false)).transitionTo(xPosition, xPosition + width, 0.1f));
                //RenderUtils.R2DUtils.drawRect(xPosition, yPosition, ((TransitionUtils) DynamicVariables.pullVar("T:" + this.displayString, false)).transitionTo(xPosition, xPosition + width, 0.1f), yPosition + height, new Color(0, 0, 0, 120).getRGB());
                //transition = mc.fontRendererObj.getStringWidth(StringUtils.stripControlCodes(displayString)) - 10;
            } else {
                //((TransitionUtils) DynamicVariables.pullVar("T:" + this.displayString, false)).resetTransition(true);
                RenderUtils.R2DUtils.drawBorderedRect(xPosition, yPosition, xPosition + width, yPosition + height, 0.5f, new Color(0, 0, 0, 120).getRGB(), new Color(160, 57, 255, 145).getRGB());
            }
            //if (!hovered) {
            //    System.out.println("Nope " + this.displayString);
            //}
            mouseDragged(mc, mouseX, mouseY);
            int var6 = 14737632;
            if (!enabled) var6 = 10526880;
            else if (hovered) var6 = 16777120;
            standard.drawCenteredString(displayString, xPosition + width / 2, yPosition + (height - 8) / 2, var6);
        }
    }

    protected void mouseDragged(Minecraft mc, int mouseX, int mouseY) {
    }

    public void mouseReleased(int mouseX, int mouseY) {
    }

    public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
        return enabled && visible && mouseX >= xPosition && mouseY >= yPosition && mouseX < xPosition + width && mouseY < yPosition + height;
    }

    public boolean isMouseOver() {
        return hovered;
    }

    public void drawButtonForegroundLayer(int mouseX, int mouseY) {
    }

    public void playPressSound(SoundHandler soundHandlerIn) {
        soundHandlerIn.playSound(PositionedSoundRecord.createPositionedSoundRecord(new ResourceLocation("gui.button.press"), 1.0F));
    }

    public int getButtonWidth() {
        return width;
    }

    void func_175211_a(int p_175211_1_) {
        width = p_175211_1_;
    }
}