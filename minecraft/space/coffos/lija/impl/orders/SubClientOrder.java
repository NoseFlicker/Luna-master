package space.coffos.lija.impl.orders;

import com.mojang.realmsclient.gui.ChatFormatting;
import space.coffos.lija.LiJA;
import space.coffos.lija.api.order.Order;
import space.coffos.lija.impl.managers.SubClients;
import space.coffos.lija.util.entity.PlayerUtils;

public class SubClientOrder implements Order {

    @Override
    public boolean run(String... args) {
        LiJA.INSTANCE.subManager.loadClient(SubClients.Luna);
        PlayerUtils.tellPlayer(LiJA.INSTANCE.subManager.getClient(), false);
        return true;
    }

    @Override
    public String usage() {
        return "USAGE: " + ChatFormatting.GRAY + "[ " + ChatFormatting.WHITE + "subclient" + ChatFormatting.GRAY + " ]";
    }
}