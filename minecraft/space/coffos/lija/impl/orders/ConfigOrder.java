package space.coffos.lija.impl.orders;

import com.mojang.realmsclient.gui.ChatFormatting;
import space.coffos.lija.LiJA;
import space.coffos.lija.impl.elements.luna.combat.antibot.AntiBot;
import space.coffos.lija.impl.elements.luna.movement.speed.Speed;
import space.coffos.lija.impl.elements.luna.player.InvCleaner;
import space.coffos.lija.impl.elements.luna.player.autoarmor.AutoArmor;
import space.coffos.lija.api.order.Order;
import space.coffos.lija.impl.elements.luna.combat.KillAura;
import space.coffos.lija.impl.elements.luna.movement.flight.Flight;
import space.coffos.lija.impl.elements.luna.player.NoSlow;
import space.coffos.lija.impl.files.ConfigsFile;
import space.coffos.lija.impl.gui.notifications.ClientNotification;
import space.coffos.lija.impl.gui.notifications.NotificationUtil;
import space.coffos.lija.util.entity.PlayerUtils;

import java.io.IOException;

public class ConfigOrder implements Order {


    @Override
    public boolean run(String... args) {

        if (args.length == 2) {
            if (args[1].equalsIgnoreCase("clear")) {
                LiJA.INSTANCE.configManager.getContents().clear();
                NotificationUtil.sendClientMessage("All configs have been purged.", 4000, ClientNotification.Type.SUCCESS);
                return true;
            } else if (args[1].equalsIgnoreCase("hypixel") | args[1].equalsIgnoreCase("watchdog")) {
                KillAura.range.setValDouble(4.4);
                KillAura.cps.setValDouble(11);
                KillAura.autoBlock.setValBoolean(true);
                KillAura.priority.setValString("Angle");
                KillAura.ticks.setValDouble(0);
                if (!LiJA.INSTANCE.elementManager.getElement(AntiBot.class).isToggled())
                    LiJA.INSTANCE.elementManager.getElement(AntiBot.class).toggle();
                KillAura.creatures.setValBoolean(false);
                KillAura.invisible.setValBoolean(false);
                Speed.mode.setValString("New");
                AntiBot.mode.setValString("Advanced");
                AntiBot.remove.setValBoolean(true);
                AntiBot.notify.setValBoolean(false);
                Flight.mode.setValString("Hypixel");
                AutoArmor.mode.setValString("Normal");
                InvCleaner.mode.setValString("Normal");
                NoSlow.release.setValBoolean(true);
                NotificationUtil.sendClientMessage("Done! Settings for " + args[1] + " has been applied!", 4000, ClientNotification.Type.SUCCESS);
                return true;
            } else if (args[1].equalsIgnoreCase("list")) {
                if (LiJA.INSTANCE.configManager.getContents().isEmpty()) {
                    NotificationUtil.sendClientMessage("There are no custom-made configs available.", 4000, ClientNotification.Type.ERROR);
                } else {
                    NotificationUtil.sendClientMessage("Printing all the custom config names in the chat.", 4000, ClientNotification.Type.WARNING);
                    LiJA.INSTANCE.configManager.getContents().forEach(config -> PlayerUtils.tellPlayer("Name: " + config.getName(), false));
                }
                return true;
            }
        } else if (args.length == 3) {
            if (args[1].equalsIgnoreCase("save")) {

                if (LiJA.INSTANCE.configManager.configExists(args[2])) {
                    NotificationUtil.sendClientMessage("A config with the name " + args[2] + " already exists, aborted.", 4000, ClientNotification.Type.ERROR);
                    return true;
                }

                LiJA.INSTANCE.configManager.addConfig(args[2]);
                try {
                    LiJA.INSTANCE.fileManager.getFile(ConfigsFile.class).saveFile();
                } catch (IOException ignored) {
                }
                NotificationUtil.sendClientMessage("Config saved as " + args[2], 4000, ClientNotification.Type.SUCCESS);
                return true;

            } else if (args[1].equalsIgnoreCase("delete")) {

                if (!LiJA.INSTANCE.configManager.configExists(args[2])) {
                    NotificationUtil.sendClientMessage("A config with the name " + args[2] + " does not exist.", 4000, ClientNotification.Type.ERROR);
                    return true;
                }

                boolean result = LiJA.INSTANCE.configManager.removeConfig(args[2]);

                if (result)
                    NotificationUtil.sendClientMessage("Config removed.", 4000, ClientNotification.Type.SUCCESS);
                else
                    NotificationUtil.sendClientMessage("Config couldn't be removed.", 4000, ClientNotification.Type.ERROR);
                return true;
            } else if (args[1].equalsIgnoreCase("load")) {
                if (!LiJA.INSTANCE.configManager.configExists(args[2])) {
                    NotificationUtil.sendClientMessage("A config with the name " + args[2] + " does not exist.", 4000, ClientNotification.Type.ERROR);
                    return true;
                }

                LiJA.INSTANCE.configManager.getConfigByName(args[2]).loadConfig();
                NotificationUtil.sendClientMessage("Config loaded.", 4000, ClientNotification.Type.SUCCESS);

                return true;
            }
        }

        return false;
    }

    @Override
    public String usage() {
        return "USAGE: " + ChatFormatting.GRAY + "[ " + ChatFormatting.WHITE + "config <save/delete/load/list/hypixel> <custom config name> " + ChatFormatting.GRAY + " ]";
    }
}