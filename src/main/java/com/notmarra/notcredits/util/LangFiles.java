package com.notmarra.notcredits.util;

import org.bukkit.Bukkit;

import static com.notmarra.notcredits.Notcredits.MINIMESSAGE_SUPPORTED_VERSIONS;
import static com.notmarra.notcredits.Notcredits.SUPPORTED_LANGUAGES;

public class LangFiles {
    public static void createLang() {
        String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];

        if (MINIMESSAGE_SUPPORTED_VERSIONS.contains(version)) {
            for (String lang : SUPPORTED_LANGUAGES) {
                Files.createFile("lang/" + lang + ".yml");
            }
        } else {
            for (String lang : SUPPORTED_LANGUAGES) {
                Files.createFileAs("lang/" + lang + "_nh.yml", "lang/" + lang + ".yml");
            }
        }

    }
}
