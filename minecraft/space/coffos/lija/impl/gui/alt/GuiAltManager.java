package space.coffos.lija.impl.gui.alt;

import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import space.coffos.lija.LiJA;
import space.coffos.lija.impl.managers.CustomFileManager;
import space.coffos.lija.util.general.dynamic.Threader;
import space.coffos.lija.util.render.RenderUtils;

import java.awt.*;
import java.io.IOException;
import java.util.Random;

public class GuiAltManager extends GuiScreen {

    private GuiButton login;
    private GuiButton remove;
    private GuiButton rename;
    private GuiButton random;
    private AltLoginThread loginThread;
    private int offset;
    Alt selectedAlt = null;
    private String status = "\247fWaiting...";
    private static final ResourceLocation background = new ResourceLocation(LiJA.INSTANCE.backgroundGray);

    public GuiAltManager() {
        Threader.newThread(CustomFileManager::saveAlts);
    }

    public void actionPerformed(GuiButton button) {
        switch (button.id) {
            case 0:
                if (loginThread == null) {
                    mc.displayGuiScreen(null);
                } else {
                    if (!loginThread.getStatus().equals("\2477Logging in...") && !loginThread.getStatus().equals("\2471Do not hit back! \2477Logging in...")) {
                        mc.displayGuiScreen(null);
                        break;
                    }
                    loginThread.setStatus("\2471Do not hit back! \247eLogging in...");
                }
                break;
            case 1:
                loginThread = new AltLoginThread(selectedAlt.getUsername(), selectedAlt.getPassword());
                loginThread.start();
                break;
            case 2:
                if (loginThread != null) {
                    loginThread = null;
                }
                LiJA.INSTANCE.altManager.getAlts().remove(selectedAlt);
                status = "\2474Removed.";
                selectedAlt = null;
                LiJA.INSTANCE.fileManager.saveFiles();
                break;
            case 3:
                mc.displayGuiScreen(new GuiAddAlt(this));
                break;
            case 4:
                mc.displayGuiScreen(new GuiAltLogin(this));
                break;
            case 5:
                if (LiJA.INSTANCE.altManager.getAlts().size() != 0) {
                    Alt randomAlt = LiJA.INSTANCE.altManager.getAlts().get(new Random().nextInt(LiJA.INSTANCE.altManager.getAlts().size()));
                    loginThread = new AltLoginThread(randomAlt.getUsername(), randomAlt.getPassword());
                    loginThread.start();
                } else
                    status = ChatFormatting.RED + "Alt list is empty! Can't use a random alt if there's no alts to choose from.";
                break;
            case 6:
                mc.displayGuiScreen(new GuiRenameAlt(this));
                break;
            case 7:
                Alt lastAlt = LiJA.INSTANCE.altManager.getLastAlt();
                if (lastAlt == null) {
                    if (loginThread == null) status = "\247eThere is no last used alt!";
                    else loginThread.setStatus("\247eThere is no last used alt!");
                } else {
                    loginThread = new AltLoginThread(lastAlt.getUsername(), lastAlt.getPassword());
                    loginThread.start();
                }
                break;
        }
    }

    public void drawScreen(int par1, int par2, float par3) {
        GuiAltLogin.time = 100;
        if (Mouse.hasWheel()) {
            int wheel = Mouse.getDWheel();
            if (wheel < 0) {
                offset += 26;
                if (offset < 0) offset = 0;
            } else if (wheel > 0) {
                offset -= 26;
                if (offset < 0) offset = 0;
            }
        }
        drawDefaultBackground();
        renderBackground(width, height);
        drawString(fontRendererObj, mc.session.getUsername(), 10, 10, 14540253);
        drawCenteredString(fontRendererObj, "Account Manager - " + LiJA.INSTANCE.altManager.getAlts().size(), width / 2, 10, -1);
        drawCenteredString(fontRendererObj, loginThread == null ? status : loginThread.getStatus(), width / 2, 20, -1);
        RenderUtils.drawBorderedRectNameTag(50.0F, 33.0F, width - 50, height - 50, 0.7f, new Color(195, 61, 255, 175).getRGB(), Integer.MIN_VALUE);
        GL11.glPushMatrix();
        prepareScissorBox(width, height - 50);
        GL11.glEnable(3089);
        int y = 38;
        for (Alt alt : LiJA.INSTANCE.altManager.getAlts())
            if (isAltInArea(y)) {
                if (alt == selectedAlt) if (isMouseOverAlt(par1, par2, y - offset) && Mouse.isButtonDown(0))
                    RenderUtils.drawBorderedRectNameTag(52.0F, y - offset - 4, width - 52, y - offset + 20, 2.0f, new Color(37, 37, 37, 255).getRGB(), new Color(37, 37, 37, 79).getRGB());
                else if (isMouseOverAlt(par1, par2, y - offset))
                    RenderUtils.drawBorderedRectNameTag(52.0F, y - offset - 4, width - 52, y - offset + 20, 2.0f, new Color(37, 37, 37, 255).getRGB(), new Color(37, 37, 37, 145).getRGB());
                else
                    RenderUtils.drawBorderedRectNameTag(52.0F, y - offset - 4, width - 52, y - offset + 20, 2.0f, new Color(37, 37, 37, 255).getRGB(), new Color(37, 37, 37, 145).getRGB());
                else if (isMouseOverAlt(par1, par2, y - offset))
                    RenderUtils.drawBorderedRectNameTag(52.0F, y - offset - 4, width - 52, y - offset + 20, 2.0f, new Color(37, 37, 37, 255).getRGB(), new Color(37, 37, 37, 145).getRGB());
                drawCenteredString(fontRendererObj, alt.getMask().equals("") ? alt.getUsername() : alt.getMask(), width / 2, y - offset, -1);
                drawCenteredString(fontRendererObj, alt.getPassword().equals("") ? "\247cCracked" : alt.getPassword().replace(".", "*"), width / 2, y - offset + 10, 5592405);
                y += 26;
            }
        GL11.glDisable(3089);
        GL11.glPopMatrix();
        super.drawScreen(par1, par2, par3);
        if (selectedAlt == null) login.enabled = remove.enabled = rename.enabled = false;
        else login.enabled = remove.enabled = rename.enabled = true;
        random.enabled = !LiJA.INSTANCE.altManager.getAlts().isEmpty();
        if (Keyboard.isKeyDown(200)) {
            offset -= 26;
            if (offset < 0) offset = 0;
        } else if (Keyboard.isKeyDown(208)) {
            offset += 26;
            if (offset < 0) offset = 0;
        }
    }

    public void initGui() {
        buttonList.add(new GuiButton(0, width / 2 + 4 + 76, height - 24, 75, 20, "Cancel"));
        buttonList.add(login = new GuiButton(1, width / 2 - 154, height - 48, 100, 20, "Login"));
        buttonList.add(remove = new GuiButton(2, width / 2 - 74, height - 24, 70, 20, "Remove"));
        buttonList.add(new GuiButton(3, width / 2 + 4 + 50, height - 48, 100, 20, "Add"));
        buttonList.add(new GuiButton(4, width / 2 - 50, height - 48, 100, 20, "Direct Login"));
        buttonList.add(random = new GuiButton(5, width / 2 - 154, height - 24, 70, 20, "Random"));
        buttonList.add(rename = new GuiButton(6, width / 2 + 4, height - 24, 70, 20, "Rename"));
        login.enabled = remove.enabled = rename.enabled = random.enabled = false;
    }

    private boolean isAltInArea(int y) {
        return y - offset <= height - 50;
    }

    private boolean isMouseOverAlt(int x, int y, int y1) {
        return x >= 52 && y >= y1 - 4 && x <= width - 52 && y <= y1 + 20 && y >= 33 && x <= width && y <= height - 50;
    }

    protected void mouseClicked(int par1, int par2, int par3) {
        if (offset < 0) offset = 0;
        int y = 38 - offset;
        for (Alt alt : LiJA.INSTANCE.altManager.getAlts()) {
            if (isMouseOverAlt(par1, par2, y)) {
                if (alt == selectedAlt) {
                    actionPerformed((GuiButton) buttonList.get(1));
                    return;
                }
                selectedAlt = alt;
            }
            y += 26;
        }
        try {
            super.mouseClicked(par1, par2, par3);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void prepareScissorBox(float x2, float y2) {
        int factor = RenderUtils.getScaledRes().getScaleFactor();
        GL11.glScissor((int) (0.0F * factor), (int) ((RenderUtils.getScaledRes().getScaledHeight() - y2) * factor),
                (int) ((x2 - 0.0F) * factor), (int) ((y2 - 33.0F) * factor));
    }

    private void renderBackground(int par1, int par2) {
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glDepthMask(false);
        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        mc.getTextureManager().bindTexture(background);
        Tessellator var3 = Tessellator.getInstance();
        var3.getWorldRenderer().startDrawingQuads();
        var3.getWorldRenderer().addVertexWithUV(0.0D, par2, -90.0D, 0.0D, 1.0D);
        var3.getWorldRenderer().addVertexWithUV(par1, par2, -90.0D, 1.0D, 1.0D);
        var3.getWorldRenderer().addVertexWithUV(par1, 0.0D, -90.0D, 1.0D, 0.0D);
        var3.getWorldRenderer().addVertexWithUV(0.0D, 0.0D, -90.0D, 0.0D, 0.0D);
        var3.draw();
        GL11.glDepthMask(true);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    }
}