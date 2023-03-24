package space.coffos.lija.impl.gui.alt;

import com.mojang.authlib.Agent;
import com.mojang.authlib.exceptions.AuthenticationException;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import org.lwjgl.input.Keyboard;
import space.coffos.lija.impl.managers.CustomFileManager;
import space.coffos.lija.LiJA;

import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.io.IOException;
import java.net.Proxy;

public class GuiAddAlt extends GuiScreen {
    private final GuiAltManager manager;
    private GuiPasswordField password;

    private class AddAltThread extends Thread {
        private final String password;
        private final String username;

        AddAltThread(String username, String password) {
            this.username = username;
            this.password = password;
            status = "\2477Waiting...";
        }

        private void checkAndAddAlt(String username, String password) {
            YggdrasilAuthenticationService service = new YggdrasilAuthenticationService(Proxy.NO_PROXY, "");
            YggdrasilUserAuthentication auth = (YggdrasilUserAuthentication) service.createUserAuthentication(Agent.MINECRAFT);
            auth.setUsername(username);
            auth.setPassword(password);
            try {
                auth.logIn();
                LiJA.INSTANCE.altManager.getAlts().add(new Alt(username, password));
                CustomFileManager.saveAlts();
                status = ("\247aAlt added. (" + username + ")");
            } catch (AuthenticationException e) {
                status = "\2474Alt failed!";
                e.printStackTrace();
            }
        }

        public void run() {
            if (password.equals("")) {
                LiJA.INSTANCE.altManager.getAlts().add(new Alt(username, ""));
                CustomFileManager.saveAlts();
                status = ("\247aAlt added. (" + username + " - offline Name)");
                return;
            }
            status = "\2471Trying alt...";
            checkAndAddAlt(username, password);
        }
    }

    private String status = "\247eWaiting...";
    private GuiTextField username;

    GuiAddAlt(GuiAltManager manager) {
        this.manager = manager;
    }

    protected void actionPerformed(GuiButton button) {
        switch (button.id) {
            case 0:
                AddAltThread login = new AddAltThread(username.getText(), password.getText());
                login.start();
                break;
            case 1:
                mc.displayGuiScreen(manager);
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
        }
    }

    public void drawScreen(int i, int j, float f) {
        drawDefaultBackground();

        drawCenteredString(fontRendererObj, "Add Alt", width / 2, 20, -1);
        username.drawTextBox();
        password.drawTextBox();
        if (username.getText().isEmpty())
            drawString(mc.fontRendererObj, "Username / E-Mail", width / 2 - 96, 66, -7829368);
        if (password.getText().isEmpty())
            drawString(mc.fontRendererObj, "Password", width / 2 - 96, 106, -7829368);

        drawCenteredString(fontRendererObj, status, width / 2, 30, -1);

        super.drawScreen(i, j, f);
    }

    public void initGui() {
        Keyboard.enableRepeatEvents(true);
        buttonList.clear();
        buttonList.add(new GuiButton(0, width / 2 - 100, height / 4 + 92 + 12, "Login"));
        buttonList.add(new GuiButton(1, width / 2 - 100, height / 4 + 116 + 12, "Back"));
        buttonList.add(new GuiButton(2, width / 2 - 100, height / 4 + 116 + 36, "Import user:pass"));
        username = new GuiTextField(1, mc.fontRendererObj, width / 2 - 100, 60, 200, 20);
        password = new GuiPasswordField(mc.fontRendererObj, width / 2 - 100, 100, 200, 20);
    }

    protected void keyTyped(char par1, int par2) {
        username.textboxKeyTyped(par1, par2);
        password.textBoxTyped(par1, par2);
        if (par1 == '\t' && (username.isFocused() || password.isFocused())) {
            username.setFocused(!username.isFocused());
            password.setFocused(!password.isFocused());
        }
        if (par1 == '\r') actionPerformed((GuiButton) buttonList.get(0));
    }

    protected void mouseClicked(int par1, int par2, int par3) {
        try {
            super.mouseClicked(par1, par2, par3);
        } catch (IOException e) {
            e.printStackTrace();
        }
        username.mouseClicked(par1, par2, par3);
        password.mouseClicked(par1, par2, par3);
    }
}