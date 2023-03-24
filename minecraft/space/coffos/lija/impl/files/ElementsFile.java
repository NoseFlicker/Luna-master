package space.coffos.lija.impl.files;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.client.Minecraft;
import space.coffos.lija.LiJA;
import space.coffos.lija.api.element.Element;
import space.coffos.lija.api.file.CustomFile;
import space.coffos.lija.api.setting.Setting;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class ElementsFile extends CustomFile {

    public static ElementsFile instance;

    public File directory = new File(Minecraft.getMinecraft().mcDataDir.toString() + "/" + LiJA.INSTANCE.subManager.getClient() + "/" + "Modules");

    public ElementsFile(Gson gson, File file) {
        super(gson, file);
        instance = this; // <-- hacky way of accessing non-static content.
    }

    public void updateDirectory() {
        directory = new File(Minecraft.getMinecraft().mcDataDir.toString() + "/" + LiJA.INSTANCE.subManager.getClient() + "/" + "Modules");
        loadFile();
    }

    @Override
    public void loadFile() {
        LiJA.INSTANCE.elementManager.getContents().forEach(module -> {

            /* Makes the element directory if it does not  exist. */
            makeDir();

            try (FileReader reader = new FileReader(getFile(module))) {

                /* Gets the json object from the file.*/
                JsonObject jsonObject = getGSON().fromJson(reader, JsonObject.class);

                /* Checks if the element has a toggled object, and if it does it checks if its true.
                 * If its true it toggles the element.
                 */
                if (jsonObject.has("toggled") && Boolean.parseBoolean(jsonObject.get("toggled").getAsString()) & !module.getName().equalsIgnoreCase("Scaffold") | Boolean.parseBoolean(jsonObject.get("toggled").getAsString()) & !module.getName().equalsIgnoreCase("Speed"))
                    module.toggle();

                /* Checks if the element has a key object and if it does it sets the keycode for it. */
                if (jsonObject.has("key")) module.setKeyCode(jsonObject.get("key").getAsInt());

                /* Gets the settings for the element. */
                ArrayList<Setting> settings = LiJA.INSTANCE.settingManager.getSettingsByElement(module);

                /* Checks if the element has settings and saves them. */
                if (settings != null && jsonObject.has("settings")) {

                    JsonArray jsonArray = (JsonArray) jsonObject.get("settings");

                    for (JsonElement jsonElement : jsonArray) {

                        /* Goes through all the settings for the element and sets there appropriate value. */
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

    @Override
    public void saveFile() {
        LiJA.INSTANCE.elementManager.getContents().forEach(module -> {

            /* Makes the element directory if it does not  exist. */
            makeDir();

            try (FileWriter writer = new FileWriter(getFile(module))) {

                /* Making a JSON object from the element info. */
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("toggled", module.isToggled());
                jsonObject.addProperty("key", module.getKeyCode());

                /* Gets the settings for the element. */
                ArrayList<Setting> settings = LiJA.INSTANCE.settingManager.getSettingsByElement(module);

                /* Checks if the element has settings and saves them. */
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

                /* Writing the json object for the element.*/
                writer.write(getGSON().toJson(jsonObject));

            } catch (IOException ignored) {
            }
        });
    }

    private void makeDir() {
        if (!directory.exists()) directory.mkdirs();
    }

    private File getFile(Element module) {
        return new File(directory, module.getName() + ".json");
    }
}