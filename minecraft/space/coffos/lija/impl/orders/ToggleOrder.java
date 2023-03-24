package space.coffos.lija.impl.orders;

import com.mojang.realmsclient.gui.ChatFormatting;
import space.coffos.lija.LiJA;
import space.coffos.lija.api.element.Element;
import space.coffos.lija.api.order.Order;
import space.coffos.lija.impl.gui.notifications.ClientNotification;
import space.coffos.lija.impl.gui.notifications.NotificationUtil;
import space.coffos.lija.util.entity.PlayerUtils;

public class ToggleOrder implements Order {

    @Override
    public boolean run(String... args) {
        if (args.length == 2) {
            try {
                Element m = LiJA.INSTANCE.elementManager.getElement(args[1]);
                if (!m.isCompatible()) return false;
                if (args[1].equalsIgnoreCase(m.getName()))
                    m.toggle();
                PlayerUtils.tellPlayer(m.getName() + (m.isToggled() ? ChatFormatting.GREEN + " on." : ChatFormatting.RED + " off."), false);
            } catch (Exception e) {
                NotificationUtil.sendClientMessage("Element not found.", 4000, ClientNotification.Type.ERROR);
            }
            return true;
        }
        return false;
    }

    @Override
    public String usage() {
        return "USAGE: " + ChatFormatting.GRAY + "[ " + ChatFormatting.WHITE + "toggle <element>" + ChatFormatting.GRAY + " ]";
    }
}