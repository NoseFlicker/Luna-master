package space.coffos.lija.impl.files;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import space.coffos.lija.LiJA;
import space.coffos.lija.api.file.CustomFile;
import space.coffos.lija.api.waypoint.WayPoint;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.CopyOnWriteArrayList;

public class WayPointsFile extends CustomFile {

    public WayPointsFile(Gson gson, File file) {
        super(gson, file);
    }

    @Override
    public void loadFile() throws IOException {

        FileReader inFile = new FileReader(getFile());
        LiJA.INSTANCE.waypointManager.setContents(getGSON().fromJson(inFile, new TypeToken<CopyOnWriteArrayList<WayPoint>>() {
        }.getType()));
        inFile.close();

        if (LiJA.INSTANCE.waypointManager.getContents() == null)
            LiJA.INSTANCE.waypointManager.setContents(new CopyOnWriteArrayList<>());

    }

    @Override
    public void saveFile() throws IOException {
        FileWriter printWriter = new FileWriter(getFile());
        printWriter.write(getGSON().toJson(LiJA.INSTANCE.waypointManager.getContents()));
        printWriter.close();
    }
}