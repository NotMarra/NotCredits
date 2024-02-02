package com.notmarra.notcredits.utilities;

public class CheckVersion {
    public static boolean isSpigot() {
        String version = org.bukkit.Bukkit.getServer().getVersion();
        return version.toLowerCase().contains("spigot");
    }
}
