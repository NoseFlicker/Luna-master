package space.coffos.lija.impl.orders;

import com.mojang.realmsclient.gui.ChatFormatting;
import space.coffos.lija.LiJA;
import space.coffos.lija.api.order.Order;
import space.coffos.lija.util.entity.PlayerUtils;

public class FriendOrder implements Order {

    private boolean handleFriend(String command, String name) {
        return handleFriend(command, name, name);
    }

    private boolean handleFriend(String command, String name, String nickname) {
        switch (command.toLowerCase()) {
            case "add": {
                LiJA.INSTANCE.friendManager.addFriend(name, nickname);
                PlayerUtils.tellPlayer("Added \"" + name + "\".", false);
                return true;
            }
            case "remove": {
                LiJA.INSTANCE.friendManager.deleteFriend(name);
                PlayerUtils.tellPlayer("Deleted \"" + name + "\".", false);
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
            return handleFriend(command, name);
        } else if (args.length == 4) {
            String command = args[1];
            String name = args[2];
            String nickname = args[3];
            return handleFriend(command, name, nickname);
        }
        return false;
    }

    @Override
    public String usage() {
        return "USAGE: " + ChatFormatting.GRAY + "[ " + ChatFormatting.WHITE + "friend <add/remove> <name> <nick>" + ChatFormatting.GRAY + " ]";
    }
}