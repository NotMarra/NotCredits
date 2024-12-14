package com.notmarra.notcredits.util;

import com.notmarra.notcredits.NotCredits;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LanguageManager {
    private static Map<String, String> messages = new HashMap<>();
    private static Map<String, List<String>> messageLists = new HashMap<>();
    private static String currentLang;

    public static void loadMessages() {
        messages.clear();
        messageLists.clear();

        currentLang = NotCredits.getInstance().getConfig().getString("lang", "en");
        File langFile = new File(NotCredits.getInstance().getDataFolder(), "lang/" + currentLang + ".yml");
        File enFile = new File(NotCredits.getInstance().getDataFolder(), "lang/en.yml");

        YamlConfiguration langConfig = YamlConfiguration.loadConfiguration(langFile);
        YamlConfiguration enConfig = YamlConfiguration.loadConfiguration(enFile);

        for (String key : langConfig.getKeys(true)) {
            if (langConfig.isString(key)) {
                messages.put(key, langConfig.getString(key));
            } else if (langConfig.isList(key)) {
                messageLists.put(key, langConfig.getStringList(key));
            }
        }

        // Load English messages as fallback
        for (String key : enConfig.getKeys(true)) {
            if (!messages.containsKey(key) && enConfig.isString(key)) {
                messages.put(key, enConfig.getString(key));
            } else if (!messageLists.containsKey(key) && enConfig.isList(key)) {
                messageLists.put(key, enConfig.getStringList(key));
            }
        }
    }

    public static String getMessage(String key) {
        return messages.getOrDefault(key, "Message not found: " + key);
    }

    public static List<String> getMessageList(String key) {
        return messageLists.getOrDefault(key, Collections.singletonList("Message list not found: " + key));
    }

}
