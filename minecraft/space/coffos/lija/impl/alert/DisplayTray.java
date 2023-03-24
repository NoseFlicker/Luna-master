package space.coffos.lija.impl.alert;

import java.awt.*;
import java.awt.TrayIcon.MessageType;

/**
 * @author Zhn17
 * <-> 2018-05-28 <-> 20:13
 **/
public class DisplayTray {

    public static void displayTray(String sub, String msg, MessageType type) throws AWTException {
        if (SystemTray.isSupported()) {
            SystemTray tray = SystemTray.getSystemTray();

            Image image = Toolkit.getDefaultToolkit().createImage("icon.png");
            TrayIcon trayIcon = new TrayIcon(image, "");
            trayIcon.setImageAutoSize(true);
            trayIcon.setToolTip("");
            tray.add(trayIcon);

            trayIcon.displayMessage(sub, msg, type);
        } else
            System.out.println("Can't display notification! Not supported on this OS version!");
    }
}
