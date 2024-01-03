package com.notmarra.notcredits.utilities;

import com.notmarra.notcredits.Notcredits;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

public class Files {
    public static void createFolder(String name) {
        File folder = new File(Notcredits.getInstance().getDataFolder(), name);
        if (!folder.exists()) {
            try {
                folder.mkdir();
            } catch (Exception var3) {
                var3.printStackTrace();
            }
        }
    }
    public static void createFile(String name) {
        File file = new File(Notcredits.getInstance().getDataFolder().getAbsolutePath(), name);
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            Notcredits.getInstance().saveResource(name, false);
        }

        try {
            Reader reader = new InputStreamReader(Notcredits.getInstance().getResource(name));
            YamlConfiguration.loadConfiguration(reader);
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

