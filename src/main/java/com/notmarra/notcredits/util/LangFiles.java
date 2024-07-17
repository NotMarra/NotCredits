package com.notmarra.notcredits.util;

import org.bukkit.Bukkit;

public class LangFiles {
    public static void createLang() {
        String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];

        if (version.startsWith("v1_16") || version.startsWith("v1_17") || version.startsWith("v1_18") || version.startsWith("v1_19") || version.startsWith("v1_20") || version.startsWith("v1_21")) {
            Files.createFile("lang/en.yml");
        } else {
            Files.createFileAs("lang/en_nh.yml", "lang/en.yml");
        }

    }
}
