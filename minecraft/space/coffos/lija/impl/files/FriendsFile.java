package space.coffos.lija.impl.files;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import space.coffos.lija.LiJA;
import space.coffos.lija.api.file.CustomFile;
import space.coffos.lija.api.friend.Friend;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.CopyOnWriteArrayList;

public class FriendsFile extends CustomFile {

    public FriendsFile(Gson gson, File file) {
        super(gson, file);
    }

    @Override
    public void loadFile() throws IOException {
        FileReader inFile = new FileReader(getFile());
        LiJA.INSTANCE.friendManager.setContents(getGSON().fromJson(inFile, new TypeToken<CopyOnWriteArrayList<Friend>>() {
        }.getType()));
        inFile.close();

        if (LiJA.INSTANCE.friendManager.getContents() == null)
            LiJA.INSTANCE.friendManager.setContents(new CopyOnWriteArrayList<>());
    }

    @Override
    public void saveFile() throws IOException {
        FileWriter printWriter = new FileWriter(getFile());
        printWriter.write(getGSON().toJson(LiJA.INSTANCE.friendManager.getContents()));
        printWriter.close();
    }
}