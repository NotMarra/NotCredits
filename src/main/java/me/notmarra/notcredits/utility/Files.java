package me.notmarra.notcredits.utility;

import me.notmarra.notcredits.Notcredits;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.*;
import java.util.Arrays;

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

    public void updateLanguageFiles() {
        File langFolder = new File(Notcredits.getInstance().getDataFolder(), "lang");
        if (!langFolder.exists()) {
            langFolder.mkdir();
        }

        // Iterate over each language file
        for (String lang : Arrays.asList("cz", "en")) {
            File langFile = new File(langFolder, lang + ".yml");
            // Check if the language file already exists
            if (!langFile.exists()) {
                // If the language file doesn't exist, create it from the resource file
                Notcredits.getInstance().saveResource("lang/" + lang + ".yml", false);
            } else {
                // If the language file exists, load it and merge it with the resource file
                YamlConfiguration langConfig = YamlConfiguration.loadConfiguration(langFile);
                YamlConfiguration resourceConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(
                        Notcredits.getInstance().getResource("lang/" + lang + ".yml")));

                // Merge the resource file into the language file
                langConfig.setDefaults(resourceConfig);
                langConfig.options().copyDefaults(true);

                // Save the updated language file
                try {
                    langConfig.save(langFile);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
