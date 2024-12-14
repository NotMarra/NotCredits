package com.notmarra.notcredits;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public final class NotCredits extends JavaPlugin {

    private static NotCredits instance;
    FileConfiguration config = this.getConfig();

    @Override
    public void onEnable() {
        instance = this;
        // Plugin startup logic

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static NotCredits getInstance() {
        return instance;
    }
}
