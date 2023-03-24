package space.coffos.lija.impl.orders;

import com.mojang.realmsclient.gui.ChatFormatting;
import org.lwjgl.input.Keyboard;
import space.coffos.lija.LiJA;
import space.coffos.lija.api.element.Element;
import space.coffos.lija.api.order.Order;
import space.coffos.lija.util.entity.PlayerUtils;

public class BindOrder implements Order {

    @Override
    public boolean run(String... args) {

        if (args.length == 4) {
            Element m = LiJA.INSTANCE.elementManager.getElement(args[2]);
            if (args[1].equalsIgnoreCase("add") && m != null) {
                m.setKeyCode(Keyboard.getKeyIndex(args[3].toUpperCase()));
                PlayerUtils.tellPlayer("The Bind for " + m.getName() + " has been set to " + Keyboard.getKeyName(m.getKeyCode()) + ".", false);
                LiJA.INSTANCE.fileManager.saveFiles();
                return true;
            }
        } else if (args.length == 3) {
            Element m = LiJA.INSTANCE.elementManager.getElement(args[2]);
            if (args[1].equalsIgnoreCase("remove") && m != null) {
                m.setKeyCode(Keyboard.KEY_NONE);
                PlayerUtils.tellPlayer("The Bind for " + m.getName() + " has been set to " + Keyboard.getKeyName(m.getKeyCode()) + ".", false);
                LiJA.INSTANCE.fileManager.saveFiles();
                return true;
            }
        } else if (args.length == 2) {
            if (args[1].equalsIgnoreCase("clear")) {
                LiJA.INSTANCE.elementManager.getContents().forEach(module -> module.setKeyCode(Keyboard.KEY_NONE));
                PlayerUtils.tellPlayer("All binds have been cleared.", false);
                LiJA.INSTANCE.fileManager.saveFiles();
                return true;
            }
        }


        return false;
    }

    @Override
    public String usage() {
        return "USAGE: " + ChatFormatting.GRAY + "[ " + ChatFormatting.WHITE + "bind <add/remove/clear> <element> <key>" + ChatFormatting.GRAY + " ]";
    }
}