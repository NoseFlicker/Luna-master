package space.coffos.lija.api.file;

import com.google.gson.Gson;
import space.coffos.lija.LiJA;

import java.io.File;
import java.io.IOException;

public abstract class CustomFile {

    private Gson gson;
    private File file;

    public CustomFile(Gson gson, File file) {
        this.gson = gson;
        this.file = file;
        makeDirectory();
    }

    protected CustomFile() {
    }

    private void makeDirectory() {
        if (LiJA.INSTANCE.subManager.getClient().equalsIgnoreCase("LiJA")) return;
        if (file != null && !file.exists()) try {
            file.createNewFile();
        } catch (IOException ignored) {
        }
    }

    public abstract void loadFile() throws IOException;

    public abstract void saveFile() throws IOException;

    public File getFile() {
        return file;
    }

    protected Gson getGSON() {
        return gson;
    }
}