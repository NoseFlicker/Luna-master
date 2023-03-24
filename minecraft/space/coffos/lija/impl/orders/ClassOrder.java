package space.coffos.lija.impl.orders;

import com.mojang.realmsclient.gui.ChatFormatting;
import space.coffos.lija.api.compiler.DynamicCompiler;
import space.coffos.lija.api.order.Order;
import space.coffos.lija.util.entity.PlayerUtils;

public class ClassOrder implements Order {

    private boolean handleClass(String command, String path, String file) {
        switch (command.toLowerCase()) {
            case "add": {
                try {
                    DynamicCompiler.implementClass(path, file);
                } catch (Exception e) {
                    PlayerUtils.tellPlayer("An error has occured.", false);
                    e.printStackTrace();
                }
                return true;
            }
            default: {
                return false;
            }
        }
    }

    @Override
    public boolean run(String... args) {
        if (args.length == 4) {
            String command = args[1];
            String path = args[2].replace("[", "").replace("]", "");
            String file = args[3].replace("[", "").replace("]", "");
            return handleClass(command, path, file);
        }
        return false;
    }

    @Override
    public String usage() {
        return "USAGE: " + ChatFormatting.GRAY + "[ " + ChatFormatting.WHITE + "class add <com.example.test.Main> <path-java-file>" + ChatFormatting.GRAY + " ]";
    }
}