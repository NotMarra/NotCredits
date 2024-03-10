package com.notmarra.notcredits.util;

import com.notmarra.notcredits.Notcredits;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;

public class Files {
    public static String getStringFromFile(String path, String string) {
        File file = new File(path);
        YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(file);
        if(yamlConfiguration.contains(string)) {
            return yamlConfiguration.getString(string);
        } else {
            return null;
        }
    }

    public static List<String> getStringListFromFile(String path, String string) {
        File file = new File(path);
        YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(file);
        if(yamlConfiguration.contains(string)) {
            return yamlConfiguration.getStringList(string);
        } else {
            return null;
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
