package space.coffos.lija.impl.gui.alt;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import org.lwjgl.input.Keyboard;
import space.coffos.lija.api.genster.Genster;
import space.coffos.lija.util.general.dynamic.DynamicVariables;

import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;

public class GuiAltLogin extends GuiScreen {

    private final GuiScreen previousScreen;
    private GuiPasswordField password;
    private AltLoginThread thread;
    private GuiTextField username;
    static boolean isGeneratedAlt;
    private GuiButton buttonGen;
    public static int time;

    GuiAltLogin(GuiScreen previousScreen) {
        this.previousScreen = previousScreen;
    }

    protected void actionPerformed(GuiButton button) {
        switch (button.id) {
            case 1:
                mc.displayGuiScreen(previousScreen);
                break;
            case 2:
                String data;
                try {
                    data = (String) Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor);
                } catch (Exception ignored) {
                    break;
                }
                if (data.contains(":")) {
                    String[] credentials = data.split(":");
                    username.setText(credentials[0]);
                    password.setText(credentials[1]);
                }
                break;
            case 3:
                time += 100;
                isGeneratedAlt = true;
                Genster.initGenster();
                thread = new AltLoginThread((String) DynamicVariables.pullVar("GEN:name", true), (String) DynamicVariables.pullVar("GEN:pass", true));
                thread.start();
                break;
            case 4:
                mc.displayGuiScreen(new GuiMultiplayer(null));
                break;
            case 0:
                thread = new AltLoginThread(username.getText(), password.getText());
                thread.start();
        }
    }

    public void drawScreen(int x, int y, float z) {
        drawDefaultBackground();
        username.drawTextBox();
        password.drawTextBox();
        drawCenteredString(mc.fontRendererObj, "Alt Login", width / 2, 20, -1);
        drawCenteredString(mc.fontRendererObj, thread == null ? "\247aWaiting..." : thread.getStatus(),
                width / 2, 29, -1);
        if (username.getText().isEmpty()) {
            isGeneratedAlt = false;
            drawString(mc.fontRendererObj, "Username / E-Mail", width / 2 - 96, 66, -7829368);
        }
        if (password.getText().isEmpty()) {
            isGeneratedAlt = false;
            drawString(mc.fontRendererObj, "Password", width / 2 - 96, 106, -7829368);
        }
        buttonGen.enabled = false;
        super.drawScreen(x, y, z);
    }

    public void initGui() {
        int var3 = height / 4 + 24;
        buttonList.add(new GuiButton(0, width / 2 - 100, var3 + 72 + 12, "Login"));
        buttonList.add(new GuiButton(1, width / 2 - 100, var3 + 72 + 12 + 24, "Back"));
        buttonList.add(new GuiButton(2, width / 2 - 100, (height / 4 + 119 + 37), "Import user:pass"));
        buttonList.add(new GuiButton(4, width / 2 - 100, (int) ((height >> 2) + 143 + 37.000000001D), "Servers"));
        buttonList.add(buttonGen = new GuiButton(3, width / 2 - 100, (int) ((height >> 2) + 167.8 + 37.000000001D), "Generate"));
        username = new GuiTextField(1, mc.fontRendererObj, width / 2 - 100, 60, 200, 20);
        password = new GuiPasswordField(mc.fontRendererObj, width / 2 - 100, 100, 200, 20);
        username.setFocused(true);
        Keyboard.enableRepeatEvents(true);
    }

    protected void keyTyped(char character, int key) {
        try {
            super.keyTyped(character, key);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (character == '\t') {
            if (!username.isFocused() & (!password.isFocused())) username.setFocused(true);
            else {
                username.setFocused(password.isFocused());
                password.setFocused(!username.isFocused());
            }
        }
        if (character == '\r') {
            actionPerformed((GuiButton) buttonList.get(0));
        }
        username.textboxKeyTyped(character, key);
        password.textBoxTyped(character, key);
    }

    protected void mouseClicked(int x, int y, int button) {
        try {
            super.mouseClicked(x, y, button);
        } catch (IOException e) {
            e.printStackTrace();
        }
        username.mouseClicked(x, y, button);
        password.mouseClicked(x, y, button);
    }

    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
    }

    public void updateScreen() {
        username.updateCursorCounter();
        password.updateCursorCounter();
        if (time > 0) {
            buttonGen.enabled = false;
            time -= 0.5;
        } else buttonGen.enabled = true;
    }
}