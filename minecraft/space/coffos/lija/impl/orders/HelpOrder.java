package space.coffos.lija.impl.orders;

import com.mojang.realmsclient.gui.ChatFormatting;
import space.coffos.lija.LiJA;
import space.coffos.lija.api.order.Order;
import space.coffos.lija.util.entity.PlayerUtils;

public class HelpOrder implements Order {

    @Override
    public boolean run(String... args) {
        PlayerUtils.tellPlayer("Here is a list of all the orders:", false);
        LiJA.INSTANCE.orderManager.getOrders().values().forEach(command -> PlayerUtils.tellPlayer(command.usage(), false));
        return true;
    }

    @Override
    public String usage() {
        return "USAGE: " + ChatFormatting.GRAY + "[ " + ChatFormatting.WHITE + "help" + ChatFormatting.GRAY + " ]";
    }
}