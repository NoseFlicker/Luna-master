package space.coffos.lija.impl.gui.mainmenu.button;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StringUtils;
import space.coffos.lija.util.math.Timer;
import space.coffos.lija.util.render.RenderUtils;

import java.awt.*;
import java.util.concurrent.TimeUnit;

import static space.coffos.lija.impl.managers.FontManager.standard;

public class GuiRainbowButton extends GuiButton {

    private static final ResourceLocation buttonTextures = new ResourceLocation("textures/gui/widgets.png");
    protected final Timer timer = new Timer();
    public int xPosition, yPosition, id;
    private float fadeSaturation;
    protected int width, height;
    public String displayString;
    private boolean hovered;
    public boolean enabled;
    private int transition;
    public boolean visible;

    public GuiRainbowButton(int buttonId, int x, int y, String buttonText) {
        this(buttonId, x, y, 200, 20, buttonText);
    }

    public GuiRainbowButton(int buttonId, int x, int y, int widthIn, int heightIn, String buttonText) {
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
            // Fade saturation - could be made a lot easier
            int secondsPassed = (int) TimeUnit.MILLISECONDS.toSeconds(timer.getTime());
            if (secondsPassed >= 4) timer.reset();
            if (secondsPassed >= 0 && secondsPassed <= 1 || fadeSaturation <= 0.2f) fadeSaturation += 0.02f;
            else if (secondsPassed >= 1) fadeSaturation -= 0.02f;
            if (fadeSaturation >= 1.0f && !(fadeSaturation <= 0.2f)) fadeSaturation -= 0.02f;
            // End fading saturation
            mc.getTextureManager().bindTexture(buttonTextures);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            hovered = mouseX >= xPosition && mouseY >= yPosition && mouseX < xPosition + width && mouseY < yPosition + height;
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
            GlStateManager.blendFunc(770, 771);
            if (getTransition() > 0) transition = (getTransition() - 1);
            if (!enabled)
                RenderUtils.R2DUtils.drawBorderedRect(xPosition, yPosition, xPosition + width, yPosition + height, 0.5f, new Color(0, 0, 0, 120).getRGB(), new Color(0, 0, 0).getRGB());
            else if (hovered) {
                RenderUtils.R2DUtils.drawBorderedRect(xPosition, yPosition, xPosition + width, yPosition + height, 0.5f, new Color(0, 0, 0, 120).getRGB(), new Color(195, 61, 255).getRGB());
                transition = mc.fontRendererObj.getStringWidth(StringUtils.stripControlCodes(displayString)) - 10;
            } else
                RenderUtils.R2DUtils.drawBorderedRect(xPosition, yPosition, xPosition + width, yPosition + height, 0.5f, new Color(0, 0, 0, 120).getRGB(), RenderUtils.getRainbow(6000, 15, fadeSaturation).getRGB());
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
}