package space.coffos.lija.impl.gui.alt;

import com.mojang.authlib.Agent;
import com.mojang.authlib.exceptions.AuthenticationException;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Session;
import space.coffos.lija.impl.alert.DisplayTray;

import java.awt.*;
import java.net.Proxy;

public class AltLoginThread extends Thread {

    private final Minecraft mc = Minecraft.getMinecraft();
    private final String password;
    private String status;
    private final String username;

    AltLoginThread(String username, String password) {
        super("Alt Login Thread");
        this.username = username;
        this.password = password;
        this.status = "\247eWaiting...";
    }

    public void updateScreen() {
        GuiAltLogin.time = 100;
    }

    private Session createSession(String username, String password) {
        YggdrasilAuthenticationService service = new YggdrasilAuthenticationService(Proxy.NO_PROXY, "");
        YggdrasilUserAuthentication auth = (YggdrasilUserAuthentication) service.createUserAuthentication(Agent.MINECRAFT);
        auth.setUsername(username);
        auth.setPassword(password);
        try {
            auth.logIn();
            return new Session(auth.getSelectedProfile().getName(), auth.getSelectedProfile().getId().toString(), auth.getAuthenticatedToken(), "mojang");
        } catch (AuthenticationException ignored) {
        }
        return null;
    }

    public String getStatus() {
        return this.status;
    }

    public void run() {
        if (password.isEmpty()) {
            mc.session = new Session(this.username, "", "", "mojang");
            status = ("\247aLogged in. (" + this.username + " - offline Name)");
            if (GuiAltLogin.isGeneratedAlt) {
                try {
                    DisplayTray.displayTray("LiJA", "Alt Generated! Logged in.", TrayIcon.MessageType.INFO);
                } catch (AWTException e) {
                    e.printStackTrace();
                }
            }
            return;
        }
        status = "\2471Logging in...";
        Session auth = createSession(username, password);
        if (auth == null) status = "\2474Login failed!";
        else {
            status = ("\247aLogged in. (" + auth.getUsername() + ")");
            mc.session = auth;
        }
    }

    public void setStatus(String status) {
        this.status = status;
    }
}