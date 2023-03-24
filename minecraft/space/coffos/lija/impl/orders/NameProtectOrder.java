package space.coffos.lija.impl.orders;

import com.mojang.realmsclient.gui.ChatFormatting;
import space.coffos.lija.impl.elements.luna.render.NameProtect;
import space.coffos.lija.api.order.Order;
import space.coffos.lija.impl.gui.notifications.ClientNotification;
import space.coffos.lija.impl.gui.notifications.NotificationUtil;

/**
 * @author Zhn17
 * <-> 2018-04-25 <-> 12:45
 **/
public class NameProtectOrder implements Order {

    private boolean handleName(String command, String name) {
        switch (command.toLowerCase()) {
            case "set": {
                NameProtect.name = name.contains("&") ? name.replace("&", "§") : name;
                NotificationUtil.sendClientMessage(name.contains("&") ? "Changed your fake-name to: " + name + " (with color-codes)." : "Changed your fake-name to: " + name + ".", 4000, ClientNotification.Type.SUCCESS);
                return true;
            }
            default: {
                return false;
            }
        }
    }

    @Override
    public boolean run(String... args) {
        if (args.length == 3) {
            String command = args[1];
            String name = args[2];
            return handleName(command, name);
        }
        return false;
    }

    @Override
    public String usage() {
        return "USAGE: " + ChatFormatting.GRAY + "[ " + ChatFormatting.WHITE + "nameprotect set <new name>" + ChatFormatting.GRAY + " ]";
    }
}