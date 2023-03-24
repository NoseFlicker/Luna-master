package space.coffos.lija.impl.managers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraft.client.Minecraft;
import space.coffos.lija.LiJA;
import space.coffos.lija.api.element.Element;
import space.coffos.lija.api.file.CustomFile;
import space.coffos.lija.api.manager.Handler;
import space.coffos.lija.impl.files.*;
import space.coffos.lija.impl.gui.alt.Alt;

import java.io.*;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static space.coffos.lija.api.element.Element.mc;

public class CustomFileManager extends Handler<CustomFile> {

    private static File ALT = CustomFileManager.getConfigFile("Alts");
    private static File LAST_ALT = CustomFileManager.getConfigFile("LastAlt");

    private Gson gson;
    private File directory;

    public CustomFileManager() {
        if (LiJA.INSTANCE.subManager.getClient().equalsIgnoreCase("LiJA")) return;
        gson = new GsonBuilder().serializeNulls().setPrettyPrinting().create();
        directory = new File(mc.mcDataDir.toString() + "/" + LiJA.INSTANCE.subManager.getClient());
        makeDirectory();
        registerFiles();
        loadLastAlt();
        loadAlts();
    }

    void updateDirectory() {
        gson = new GsonBuilder().serializeNulls().setPrettyPrinting().create();
        directory = new File(mc.mcDataDir.toString() + "/" + LiJA.INSTANCE.subManager.getClient());
        makeDirectory();
        registerFiles();
        loadLastAlt();
        loadAlts();
        ALT = CustomFileManager.getConfigFile("Alts");
        LAST_ALT = CustomFileManager.getConfigFile("LastAlt");
        ElementsFile.instance.updateDirectory();
    }

    private void makeDirectory() {
        if (LiJA.INSTANCE.subManager.getClient().equalsIgnoreCase("LiJA")) return;
        if (!directory.exists()) directory.mkdir();
    }

    private static void loadLastAlt() {
        if (LiJA.INSTANCE.subManager.getClient().equalsIgnoreCase("LiJA")) return;
        try {
            String s;
            if (!LAST_ALT.exists()) {
                PrintWriter printWriter = new PrintWriter(new FileWriter(LAST_ALT));
                printWriter.println();
                printWriter.close();
            } else if (LAST_ALT.exists()) {
                BufferedReader bufferedReader = new BufferedReader(new FileReader(LAST_ALT));
                while ((s = bufferedReader.readLine()) != null) {
                    if (s.contains("\t")) s = s.replace("\t", "    ");
                    if (s.contains("    ")) {
                        String[] parts = s.split(" {4}");
                        String[] account = parts[1].split(":");
                        LiJA.INSTANCE.altManager.setLastAlt(account.length == 2 ? new Alt(account[0], account[1], parts[0]) : new Alt(account[0], IntStream.range(2, account.length).mapToObj(i -> ":" + account[i]).collect(Collectors.joining("", account[1], "")), parts[0]));
                    } else {
                        String[] account = s.split(":");
                        LiJA.INSTANCE.altManager.setLastAlt(account.length == 1 ? new Alt(account[0], "") : account.length == 2 ? new Alt(account[0], account[1]) : new Alt(account[0], IntStream.range(2, account.length).mapToObj(i -> ":" + account[i]).collect(Collectors.joining("", account[1], ""))));
                    }
                }
                bufferedReader.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void loadAlts() {
        if (LiJA.INSTANCE.subManager.getClient().equalsIgnoreCase("LiJA")) return;
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(ALT));
            String s;
            if (!ALT.exists()) {
                PrintWriter printWriter = new PrintWriter(new FileWriter(ALT));
                printWriter.println();
                printWriter.close();
            } else if (ALT.exists()) {
                while ((s = bufferedReader.readLine()) != null) {
                    if (s.contains("\t")) s = s.replace("\t", "    ");
                    if (s.contains("    ")) {
                        String[] parts = s.split(" {4}");
                        String[] account = parts[1].split(":");
                        LiJA.INSTANCE.altManager.getAlts().add(account.length == 2 ? new Alt(account[0], account[1], parts[0]) : new Alt(account[0], IntStream.range(2, account.length).mapToObj(i -> ":" + account[i]).collect(Collectors.joining("", account[1], "")), parts[0]));
                    } else {
                        String[] account = s.split(":");
                        if (account.length == 1) LiJA.INSTANCE.altManager.getAlts().add(new Alt(account[0], ""));
                        else if (account.length == 2) try {
                            LiJA.INSTANCE.altManager.getAlts().add(new Alt(account[0], account[1]));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        else
                            LiJA.INSTANCE.altManager.getAlts().add(new Alt(account[0], IntStream.range(2, account.length).mapToObj(i -> ":" + account[i]).collect(Collectors.joining("", account[1], ""))));
                    }
                }
            }
            bufferedReader.close();
        } catch (Exception ignored) {
        }
    }

    public static void saveAlts() {
        if (LiJA.INSTANCE.subManager.getClient().equalsIgnoreCase("LiJA")) return;
        try {
            PrintWriter printWriter = new PrintWriter(ALT);
            List<Alt> alts = LiJA.INSTANCE.altManager.getAlts();
            alts.forEach(alt -> printWriter.println(alt.getMask().equals("") ? alt.getUsername() + ":" + alt.getPassword() : alt.getMask() + "    " + alt.getUsername() + ":" + alt.getPassword()));
            printWriter.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static File getConfigFile(String name) {
        File file = new File(getConfigDir(), String.format("%s.txt", name));
        if (!file.exists() && !LiJA.INSTANCE.currentSubClient.toString().equalsIgnoreCase("LiJA")) {
            try {
                file.createNewFile();
            } catch (IOException ignored) {
            }
        }
        return file;
    }

    private static File getConfigDir() {
        File file = new File(Minecraft.getMinecraft().mcDataDir, LiJA.INSTANCE.subManager.getClient());
        System.out.println(file.getName());
        if (!file.exists() && !LiJA.INSTANCE.currentSubClient.toString().equalsIgnoreCase("LiJA")) file.mkdir();
        return file;
    }

    private void registerFiles() {
        if (LiJA.INSTANCE.subManager.getClient().equalsIgnoreCase("LiJA")) return;
        getContents().add(new FriendsFile(gson, new File(directory, "Friends.json")));
        getContents().add(new WayPointsFile(gson, new File(directory, "Waypoints.json")));
        getContents().add(new ConfigsFile(gson, new File(directory.toString() + "/" + "Configs", "Configs.json")));
        getContents().add(new ElementsFile(gson, null));
        getContents().add(new OrdersFile(gson, null));
    }

    public void loadFiles() {
        if (LiJA.INSTANCE.subManager.getClient().equalsIgnoreCase("LiJA")) return;
        CopyOnWriteArrayList<CustomFile> contents = getContents();
        contents.forEach(file -> {
            try {
                if (file.getClass() == ElementsFile.class) return; // Skip ElementsFile as it'll be loaded directly when a sub-client is set.
                file.loadFile();
            } catch (IOException e) {
                System.out.println("Failed to load files");
                e.printStackTrace();
            }
        });
    }

    public void saveFiles() {
        if (LiJA.INSTANCE.subManager.getClient().equalsIgnoreCase("LiJA")) return;
        CopyOnWriteArrayList<CustomFile> contents = getContents();
        for (CustomFile file : contents)
            try {
                file.saveFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
    }

    public CustomFile getFile(Class<? extends CustomFile> clazz) {
        CopyOnWriteArrayList<CustomFile> contents = getContents();
        return contents.stream().filter(file -> file.getClass() == clazz).findFirst().orElse(null);
    }
}