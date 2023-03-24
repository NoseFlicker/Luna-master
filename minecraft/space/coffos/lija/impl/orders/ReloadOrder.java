package space.coffos.lija.impl.orders;

import com.mojang.realmsclient.gui.ChatFormatting;
import space.coffos.lija.LiJA;
import space.coffos.lija.api.order.Order;
import space.coffos.lija.util.entity.PlayerUtils;

public class ReloadOrder implements Order {

    @Override
    public boolean run(String... args) {
        PlayerUtils.tellPlayer("Reloading...", false);
        for (int i = 0; i < 2; i++) LiJA.INSTANCE.fileManager.loadFiles();
        return true;
    }

    @Override
    public String usage() {
        return "USAGE: " + ChatFormatting.GRAY + "[ " + ChatFormatting.WHITE + "reload" + ChatFormatting.GRAY + " ]";
    }
}