package me.notmarra.notcredits.utilities;

import me.notmarra.notcredits.Notcredits;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GetMessage {
    public static String getMessage(String message) {
        File file = new File(Notcredits.getInstance().getDataFolder() + "/lang/" + Notcredits.getInstance().getConfig().getString("lang") + ".yml");
        YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(file);
        return yamlConfiguration.getString(message);
    }

    public static List<String> getStringList(String key) {
        File file = new File(Notcredits.getInstance().getDataFolder() + "/lang/" + Notcredits.getInstance().getConfig().getString("lang") + ".yml");
        YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(file);

        return yamlConfiguration.getStringList(key);
    }
}
