package space.coffos.lija.impl.files;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import net.minecraft.client.Minecraft;
import space.coffos.lija.LiJA;
import space.coffos.lija.api.file.CustomFile;
import space.coffos.lija.api.order.Order;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Arrays;

/*
 * @author Jay, 2018
 */
public class OrdersFile extends CustomFile {

    private final File directory = new File(Minecraft.getMinecraft().mcDataDir + "/" + LiJA.INSTANCE.subManager.getClient() + "/Orders");

    public OrdersFile(Gson gson, File file) {
        super(gson, file);
    }

    @Override
    public void loadFile() {
        //not to be loaded as commands shouldn't be edited.
    }

    @Override
    public void saveFile() {
        LiJA.INSTANCE.orderManager.getOrders().forEach((aliases, command) -> {
            makeDir();
            try (PrintWriter writer = new PrintWriter(getFile(command))) {
                JsonObject object = new JsonObject();
                object.addProperty("aliases", Arrays.asList(aliases).toString());
                object.addProperty("usage", command.usage());
                JsonObject initialObject = new JsonObject();
                initialObject.add(Arrays.asList(aliases).get(0), object);
                writer.println(getGSON().toJson(initialObject));
                writer.flush();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        });
    }

    private void makeDir() {
        if (!directory.exists()) directory.mkdirs();
    }

    private File getFile(Order command) {
        return new File(directory, command.getClass().getName().substring(LiJA.INSTANCE.classedObjects[0].length()) + ".json");
    }
}