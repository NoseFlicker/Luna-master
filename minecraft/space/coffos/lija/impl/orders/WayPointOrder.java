package space.coffos.lija.impl.orders;

import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.client.Minecraft;
import space.coffos.lija.LiJA;
import space.coffos.lija.api.order.Order;
import space.coffos.lija.api.waypoint.WayPoint;
import space.coffos.lija.impl.files.WayPointsFile;
import space.coffos.lija.impl.gui.notifications.ClientNotification;
import space.coffos.lija.impl.gui.notifications.NotificationUtil;
import space.coffos.lija.util.entity.PlayerUtils;

import static space.coffos.lija.api.element.Element.mc;

public class WayPointOrder implements Order {

    @Override
    public boolean run(String... args) {

        if (args.length == 2) {
            if (args[1].equalsIgnoreCase("clear")) {
                LiJA.INSTANCE.waypointManager.getContents().clear();
                NotificationUtil.sendClientMessage("Cleared all WayPoints.", 4000, ClientNotification.Type.SUCCESS);
                saveFile();
                return true;
            } else if (args[1].equalsIgnoreCase("list")) {
                if (LiJA.INSTANCE.waypointManager.getContents().isEmpty())
                    NotificationUtil.sendClientMessage("There's no WayPoints available.", 4000, ClientNotification.Type.SUCCESS);
                else {
                    NotificationUtil.sendClientMessage("Gathering WayPoints list...", 3500, ClientNotification.Type.SUCCESS);
                    LiJA.INSTANCE.waypointManager.getContents().forEach((WayPoint waypoint) -> PlayerUtils.tellPlayer("Name: " + ChatFormatting.WHITE + waypoint.getName() + ChatFormatting.GRAY + " Server: " + ChatFormatting.WHITE + (waypoint.getServerIP() != null ? waypoint.getServerIP() : "SinglePlayer"), false));
                }
                return true;
            }
        } else if (args.length == 3) {
            if (args[1].equalsIgnoreCase("add")) {
                LiJA.INSTANCE.waypointManager.addWaypoint(args[2], mc.getCurrentServerData(), mc.thePlayer.getPosition());
                PlayerUtils.tellPlayer("A waypoint with the name " + args[2] + " has been added.", false);
                saveFile();
                return true;
            } else if (args[1].equalsIgnoreCase("remove")) {
                WayPoint waypoint = LiJA.INSTANCE.waypointManager.getWayPoint(args[2], Minecraft.getMinecraft().getCurrentServerData());
                if (waypoint != null) {
                    LiJA.INSTANCE.waypointManager.getContents().remove(waypoint);
                    PlayerUtils.tellPlayer("A waypoint with the name " + args[2] + " has been removed.", false);
                    saveFile();
                } else PlayerUtils.tellPlayer("A waypoint with the name " + args[2] + " can not be found.", false);
                return true;
            }
        }

        return false;
    }

    private void saveFile() {
        try {
            LiJA.INSTANCE.fileManager.getFile(WayPointsFile.class).saveFile();
        } catch (Exception ignored) {
        }
    }

    @Override
    public String usage() {
        return "USAGE: " + ChatFormatting.GRAY + "[ " + ChatFormatting.WHITE + "waypoint <list/add/remove/clear> <name>" + ChatFormatting.GRAY + " ]";
    }
}