package space.coffos.lija.impl.files;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import space.coffos.lija.LiJA;
import space.coffos.lija.api.file.CustomFile;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;


public class ConfigsFile extends CustomFile {


    public ConfigsFile(Gson gson, File file) {
        super(gson, file);
    }

    @Override
    public void loadFile() throws IOException {
        if (LiJA.INSTANCE.subManager.getClient().equalsIgnoreCase("LiJA")) return;
        FileReader fr = new FileReader(getFile());

        JsonArray jsonArray = getGSON().fromJson(fr, JsonArray.class);

        if (jsonArray == null) {
            fr.close();
            return;
        }

        for (JsonElement jsonElement : jsonArray) {
            JsonObject jsonObject = jsonElement.getAsJsonObject();

            if (jsonObject.has("name"))
                LiJA.INSTANCE.configManager.addConfig(jsonObject.get("name").getAsString());

        }

        fr.close();
    }

    @Override
    public void saveFile() throws IOException {
        if (LiJA.INSTANCE.subManager.getClient().equalsIgnoreCase("LiJA")) return;
        FileWriter fw = new FileWriter(getFile());

        JsonArray jsonArray = new JsonArray();

        LiJA.INSTANCE.configManager.getContents().forEach(config -> {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("name", config.getName());
            jsonArray.add(jsonObject);
        });

        fw.write(getGSON().toJson(jsonArray));

        fw.close();
    }
}