package space.coffos.lija.impl.managers;

import org.reflections.Reflections;
import space.coffos.lija.LiJA;
import space.coffos.lija.api.element.Element;
import space.coffos.lija.util.entity.PlayerUtils;

/**
 * Made by Certain.
 * At 2019-11-25 : 17:26
 */

public class SubClientManager {

    public void loadClient(SubClients subClient) {
        if (LiJA.INSTANCE.elementManager == null || subClient == SubClients.LiJA) return;
        LiJA.INSTANCE.currentSubClient = subClient;
        LiJA.INSTANCE.currentFormat = subClient == SubClients.Luna ? LiJA.INSTANCE.format[2] : subClient == SubClients.Temperance ? LiJA.INSTANCE.format[0] : LiJA.INSTANCE.format[2];
        LiJA.INSTANCE.fileManager.updateDirectory();
        LiJA.INSTANCE.fileManager.saveFiles();
        LiJA.INSTANCE.updateTitle();
        for (Element e : LiJA.INSTANCE.elementManager.getToggledElements()) if (!e.isCompatible()) e.toggle();
        PlayerUtils.tellPlayer("Reloading...", false);
        for (int i = 0; i < 2; i++) LiJA.INSTANCE.fileManager.loadFiles();
    }

    /**
     * Return the current sub-client's name.
     */
    public String getClient() {
        return LiJA.INSTANCE.currentSubClient.toString();
    }
}