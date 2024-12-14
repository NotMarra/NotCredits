package com.notmarra.notcredits.util;

import com.notmarra.notcredits.NotCredits;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.*;
import java.nio.file.StandardCopyOption;
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
        File file = new File(NotCredits.getInstance().getDataFolder().getAbsolutePath(), name);
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            NotCredits.getInstance().saveResource(name, false);
        }

        try {
            Reader reader = new InputStreamReader(NotCredits.getInstance().getResource(name));
            YamlConfiguration.loadConfiguration(reader);
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void createFileAs(String source, String name) {
        File targetFile = new File(NotCredits.getInstance().getDataFolder(), name);
        if (!targetFile.exists()) {
            targetFile.getParentFile().mkdirs();
            try (InputStream inputStream = NotCredits.getInstance().getResource(source)) {
                if (inputStream != null) {
                    java.nio.file.Files.copy(inputStream, targetFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                } else {
                    NotCredits.getInstance().getLogger().warning("Resource not found: " + source);
                    return;
                }
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        }

        try {
            Reader reader = new InputStreamReader(NotCredits.getInstance().getResource(name));
            YamlConfiguration.loadConfiguration(reader);
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
