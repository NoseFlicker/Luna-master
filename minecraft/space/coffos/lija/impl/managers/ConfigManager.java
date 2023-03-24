package space.coffos.lija.impl.managers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraft.client.Minecraft;
import space.coffos.lija.LiJA;
import space.coffos.lija.api.config.Config;
import space.coffos.lija.api.manager.Handler;

import java.io.File;
import java.io.IOException;

public class ConfigManager extends Handler<Config> {

    private final Gson gson;

    public ConfigManager() {
        gson = new GsonBuilder().setPrettyPrinting().create();
        makeDir();
    }

    private void makeDir() {
        File directory = new File(Minecraft.getMinecraft().mcDataDir.toString() + "/" + LiJA.INSTANCE.subManager.getClient() + "/" + "Configs");
        if (!directory.exists()) directory.mkdirs();
    }

    public void addConfig(String name) {
        Config config = new Config(name, new File(Minecraft.getMinecraft().mcDataDir.toString() + "/" + LiJA.INSTANCE.subManager.getClient() + "/" + "Configs" + "/" + name), gson);
        config.saveConfig();
        getContents().add(config);
    }

    public boolean removeConfig(String name) {

        Config config = getConfigByName(name);

        if (config == null)
            return false;

        getContents().remove(config);

        try {
            config.delete();
        } catch (IOException ignored) {
        }

        return true;
    }

    public boolean configExists(String name) {
        return getContents().stream().anyMatch(config -> config.getName().equalsIgnoreCase(name));
    }

    public Config getConfigByName(String name) {
        return getContents().stream().filter(config -> config.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }
}