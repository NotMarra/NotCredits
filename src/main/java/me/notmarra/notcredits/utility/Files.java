package me.notmarra.notcredits.utility;

import me.notmarra.notcredits.Notcredits;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.*;

public class Files {

    public static void createFolder(String name) {
        File folder = new File(Notcredits.main.getDataFolder(), name);

        if (!folder.exists()) {
            try {
                folder.mkdir();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void createFile(String name) {
        File file = new File(Notcredits.main.getDataFolder().getAbsolutePath(), name);
        if (!file.exists()){
            file.getParentFile().mkdirs();
            Notcredits.main.saveResource(name, false);
        }
        try {
            Reader reader = new InputStreamReader(Notcredits.main.getResource(name));
            YamlConfiguration.loadConfiguration(reader);
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
