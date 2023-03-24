package space.coffos.lija.impl.gui.mainmenu;

import net.minecraft.client.gui.*;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.ISaveFormat;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.GL11;
import space.coffos.lija.LiJA;
import space.coffos.lija.impl.gui.mainmenu.button.GuiRainbowButton;
import space.coffos.lija.impl.managers.SubClients;
import space.coffos.lija.util.general.dynamic.DynamicVariables;
import space.coffos.lija.util.general.dynamic.Threader;
import space.coffos.lija.util.render.FontUtils;

import java.io.IOException;
import java.net.URI;

import static space.coffos.lija.util.general.dynamic.Threader.newThread;

public class GuiSubClients extends GuiScreen implements GuiYesNoCallback {

    private static Logger logger = LogManager.getLogger();
    private final Object field_104025_t = new Object();
    private GuiRainbowButton luna, temperance;
    private String field_92025_p;
    private String field_146972_A;
    private String field_104024_v;

    private int field_92022_t;
    private int field_92021_u;
    private int field_92020_v;
    private int field_92019_w;

    public GuiSubClients() {

        field_92025_p = "";
    }

    /**
     * Called from the main game loop to update the screen.
     */
    public void updateScreen() {
        mc.gameSettings.keyBindJump.pressed = false;
    }

    /**
     * Returns true if this GUI should pause the game when it is displayed in single-player
     */
    public boolean doesGuiPauseGame() {
        return false;
    }

    /**
     * Fired when a key is typed (except F11 who toggle full screen). This is the equivalent of
     * KeyListener.keyTyped(KeyEvent e). Args : character (character on the key), keyCode (lwjgl Keyboard key code)
     */
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
    }

    /**
     * Adds the buttons (and other controls) to the screen in question.
     */
    public void initGui() {
        newThread(() -> {
            mc.gameSettings.keyBindJump.pressed = false;
            int var3 = height / 4 + 48;

            LiJA.INSTANCE.api.refreshAPI();
            addSingleplayerMultiplayerButtons(var3);
            temperance.enabled = (boolean) DynamicVariables.pullVar("S:Temperance", false);
            luna.enabled = (boolean) DynamicVariables.pullVar("S:Luna", false);

            synchronized (field_104025_t) {
                int field_92023_s = fontRendererObj.getStringWidth(field_92025_p);
                int field_92024_r = fontRendererObj.getStringWidth(field_146972_A);
                int var5 = Math.max(field_92023_s, field_92024_r);
                field_92022_t = (width - var5) / 2;
                field_92021_u = ((GuiRainbowButton) buttonList.get(0)).yPosition - 24;
                field_92020_v = field_92022_t + var5;
                field_92019_w = field_92021_u + 24;
            }
        });
    }

    /**
     * Adds Singleplayer and Multiplayer buttons on Main Menu for players who have bought the game.
     */
    private void addSingleplayerMultiplayerButtons(int p_73969_1_) {
        buttonList.add(luna = new GuiRainbowButton(1, width / 2 - 100, p_73969_1_, I18n.format("Luna")));
        buttonList.add(temperance = new GuiRainbowButton(2, width / 2 - 100, p_73969_1_ + 24, I18n.format("Temperance")));
    }

    protected void actionPerformed(GuiButton button) throws IOException {
        switch (((GuiRainbowButton) button).id) {
            case 0:
                this.mc.displayGuiScreen(new GuiOptions(this, this.mc.gameSettings));
                break;
            case 1:
                LiJA.INSTANCE.subManager.loadClient(SubClients.Luna);
                this.mc.displayGuiScreen(new GuiMainMenu());
                break;
            case 2:
                LiJA.INSTANCE.subManager.loadClient(SubClients.Temperance);
                this.mc.displayGuiScreen(new GuiMainMenu());
                break;
        }
    }

    public void confirmClicked(boolean result, int id) {
        if (result && id == 12) {
            ISaveFormat var6 = this.mc.getSaveLoader();
            var6.flushCache();
            var6.deleteWorldDirectory("Demo_World");
            this.mc.displayGuiScreen(this);
        } else if (id == 13) {
            if (result) try {
                Class var3 = Class.forName("java.awt.Desktop");
                Object var4 = var3.getMethod("getDesktop").invoke(null);
                var3.getMethod("browse", URI.class).invoke(var4, new URI(this.field_104024_v));
            } catch (Throwable var5) {
                logger.error("Couldn\'t open link", var5);
            }

            this.mc.displayGuiScreen(this);
        }
    }

    /**
     * Draws the screen and all the components in it. Args : mouseX, mouseY, renderPartialTicks
     */
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(width, height);
        LiJA.INSTANCE.fontManager.comfortaa_sub.drawCenteredString("What client do you want to use?", width >> 1, height >> 2, -1);
        /*
         * Replace strings
         */
        //dString = dString.replace("]", "").replace("[", "").replace("CC:", "§");
        /*
         * Draw the message from 'dString'
         */
        GlStateManager.pushMatrix();
        GlStateManager.scale(0.55, 0.55, 0.55);
        FontUtils.drawStringWithShadow("LiJA Identifier: LiJA_" + LiJA.INSTANCE.build[1].replace(" ", "") + "", 2, 10, -1);
        GlStateManager.popMatrix();
        //FontUtils.drawStringWithShadow(dString, this.width - this.fontRendererObj.getStringWidth(dString) - 2, this.height - 10, -1);

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    public void renderBackground(int par1, int par2) {
        GL11.glDisable(2929);
        GL11.glDepthMask(false);
        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glDisable(3008);
        this.mc.getTextureManager().bindTexture(new ResourceLocation(LiJA.INSTANCE.backgroundGray));
        Tessellator var3 = Tessellator.getInstance();
        var3.getWorldRenderer().startDrawingQuads();
        var3.getWorldRenderer().addVertexWithUV(0.0D, par2, -90.0D, 0.0D, 1.0D);
        var3.getWorldRenderer().addVertexWithUV(par1, par2, -90.0D, 1.0D, 1.0D);
        var3.getWorldRenderer().addVertexWithUV(par1, 0.0D, -90.0D, 1.0D, 0.0D);
        var3.getWorldRenderer().addVertexWithUV(0.0D, 0.0D, -90.0D, 0.0D, 0.0D);
        var3.draw();
        GL11.glDepthMask(true);
        GL11.glEnable(2929);
        GL11.glEnable(3008);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    }

    /**
     * Called when the mouse is clicked. Args : mouseX, mouseY, clickedButton
     */
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);

        synchronized (this.field_104025_t) {
            if (this.field_92025_p.length() > 0 && mouseX >= this.field_92022_t && mouseX <= this.field_92020_v && mouseY >= this.field_92021_u && mouseY <= this.field_92019_w) {
                GuiConfirmOpenLink var5 = new GuiConfirmOpenLink(this, this.field_104024_v, 13, true);
                var5.disableSecurityWarning();
                this.mc.displayGuiScreen(var5);
            }
        }
    }
}