package space.coffos.lija.api.config;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import space.coffos.lija.LiJA;
import space.coffos.lija.api.setting.Setting;
import space.coffos.lija.api.element.Element;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class Config {

    private String name;
    private File directory;
    private Gson gson;

    public Config(String name, File directory, Gson gson) {
        this.name = name;
        this.directory = directory;
        this.gson = gson;
    }

    public void loadConfig() {

        makeDir();

        LiJA.INSTANCE.elementManager.getToggledElements().forEach(Element::toggle);

        LiJA.INSTANCE.elementManager.getContents().forEach(module -> {

            try (FileReader reader = new FileReader(getFile(module))) {

                JsonObject jsonObject = gson.fromJson(reader, JsonObject.class);

                if (jsonObject.has("toggled")) {
                    if (jsonObject.get("toggled").getAsBoolean())
                        module.toggle();
                }

                if (jsonObject.has("key"))
                    module.setKeyCode(jsonObject.get("key").getAsInt());

                ArrayList<Setting> settings = LiJA.INSTANCE.settingManager.getSettingsByElement(module);

                if (settings != null && jsonObject.has("settings")) {

                    JsonArray jsonArray = (JsonArray) jsonObject.get("settings");

                    for (JsonElement jsonElement : jsonArray) {

                        settings.stream()
                                .filter(setting -> jsonElement.getAsJsonObject().has(setting.getName()))
                                .forEach(setting -> {
                                    if (setting.isCheck())
                                        setting.setValBoolean(Boolean.parseBoolean(jsonElement.getAsJsonObject().get(setting.getName()).getAsString()));
                                    else if (setting.isCombo())
                                        setting.setValString(jsonElement.getAsJsonObject().get(setting.getName()).getAsString());
                                    else
                                        setting.setValDouble(Double.parseDouble(jsonElement.getAsJsonObject().get(setting.getName()).getAsString()));
                                });
                    }
                }

            } catch (IOException ignored) {
            }
        });


    }

    public void saveConfig() {

        makeDir();

        LiJA.INSTANCE.elementManager.getContents().forEach(module -> {

            try (FileWriter writer = new FileWriter(getFile(module))) {

                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("toggled", module.isToggled());
                jsonObject.addProperty("key", module.getKeyCode());

                ArrayList<Setting> settings = LiJA.INSTANCE.settingManager.getSettingsByElement(module);

                if (settings != null) {

                    JsonArray jsonArray = new JsonArray();
                    JsonObject jsonObject1 = new JsonObject();

                    settings.forEach(setting -> {
                        if (setting.isCheck())
                            jsonObject1.addProperty(setting.getName(), setting.getValBoolean());
                        else if (setting.isCombo())
                            jsonObject1.addProperty(setting.getName(), setting.getValStringForSaving());
                        else
                            jsonObject1.addProperty(setting.getName(), setting.getValDouble());
                    });

                    jsonArray.add(jsonObject1);
                    jsonObject.add("settings", jsonArray);

                }

                writer.write(gson.toJson(jsonObject));

            } catch (IOException ignored) {
            }

        });
    }


    public void delete() throws IOException {
        delete(directory);
    }

    private void delete(File f) throws IOException {
        if (f.isDirectory()) for (File c : Objects.requireNonNull(f.listFiles()))
            delete(c);
        assert f.delete() : "Failed to delete file: " + f;
    }


    private File getFile(Element module) {
        return new File(directory, module.getName() + ".json");
    }

    private void makeDir() {
        if (!directory.exists())
            directory.mkdirs();
    }

    public File getDirectory() {
        return directory;
    }

    public String getName() {
        return name;
    }
}