package com.notmarra.notcredits.utilities;

import com.notmarra.notcredits.Notcredits;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.List;

public class GetMessage {
    public static String getMessage(String message) {
        File file = new File(Notcredits.getInstance().getDataFolder() + "/lang/" + Notcredits.getInstance().getConfig().getString("lang") + ".yml");
        YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(file);
        if (yamlConfiguration.getString(message) == null) {
            File defaultFile = new File(Notcredits.getInstance().getDataFolder() + "/lang/en.yml");
            YamlConfiguration defaultYamlConfiguration = YamlConfiguration.loadConfiguration(defaultFile);
            return defaultYamlConfiguration.getString(message);
        } else {
            return yamlConfiguration.getString(message);
        }
    }

    public static String getMessageConsole(String message) {
        File file = new File(Notcredits.getInstance().getDataFolder() + "/lang/" + Notcredits.getInstance().getConfig().getString("lang") + ".yml");
        YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(file);
        if (yamlConfiguration.getString(message) == null) {
            File defaultFile = new File(Notcredits.getInstance().getDataFolder() + "/lang/en.yml");
            YamlConfiguration defaultYamlConfiguration = YamlConfiguration.loadConfiguration(defaultFile);
            //remove things in like <white>, <#colorcode>, <bold> from the message
            return defaultYamlConfiguration.getString(message).replaceAll("<.*?>", "");
        } else {
            return yamlConfiguration.getString(message).replaceAll("<.*?>", "");
        }
    }

    public static List<String> getStringList(String key) {
        File file = new File(Notcredits.getInstance().getDataFolder() + "/lang/" + Notcredits.getInstance().getConfig().getString("lang") + ".yml");
        YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(file);
        if (yamlConfiguration.getStringList(key) == null) {
            File defaultFile = new File(Notcredits.getInstance().getDataFolder() + "/lang/en.yml");
            YamlConfiguration defaultYamlConfiguration = YamlConfiguration.loadConfiguration(defaultFile);
            return defaultYamlConfiguration.getStringList(key);
        } else {
            return yamlConfiguration.getStringList(key);
        }
    }
}
