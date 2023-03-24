package space.coffos.lija.impl.orders;

import com.mojang.realmsclient.gui.ChatFormatting;
import org.apache.commons.validator.routines.UrlValidator;
import space.coffos.lija.api.order.Order;
import space.coffos.lija.api.sound.SoundHelper;
import space.coffos.lija.impl.gui.notifications.ClientNotification;
import space.coffos.lija.impl.gui.notifications.NotificationUtil;

public class PlayOrder implements Order {

    @Override
    public boolean run(String... args) {
        // URL Verifying

        UrlValidator validator = new UrlValidator();

        if (args.length == 2) {
            if (validator.isValid(args[1])) {
                NotificationUtil.sendClientMessage("Trying to play MP3-File...", 4000, ClientNotification.Type.INFO);
                SoundHelper.started = true;
                SoundHelper.createThread(args[1]);
                SoundHelper.music.start();
                return true;
            } else if (args[1].equalsIgnoreCase("stop") & SoundHelper.started & SoundHelper.music.isAlive()) {
                SoundHelper.started = false;
                SoundHelper.music.stop();
            }
        }
        return false;
    }

    @Override
    public String usage() {
        return "USAGE: " + ChatFormatting.GRAY + "[ " + ChatFormatting.WHITE + "play <url-to-mp3> | play stop" + ChatFormatting.GRAY + " ]";
    }
}