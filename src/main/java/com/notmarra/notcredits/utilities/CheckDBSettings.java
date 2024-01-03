package com.notmarra.notcredits.utilities;

import com.notmarra.notcredits.Notcredits;
import org.bukkit.Bukkit;

public class CheckDBSettings {
    public void CheckDBSettings() {
        if (Notcredits.getInstance().getConfig().getString("data.type").equalsIgnoreCase("MySQL")) {
            if (Notcredits.getInstance().getConfig().getString("data.mysql.host").equals("") || Notcredits.getInstance().getConfig().getString("data.mysql.username").equals("") || Notcredits.getInstance().getConfig().getString("data.mysql.database").equals("") || Notcredits.getInstance().getConfig().getString("data.mysql.password").equals("")) {
                Bukkit.getServer().getLogger().warning("[NotCredits] The MySQL database settings in the configuration file are not complete.");
                Bukkit.getServer().getLogger().warning("[NotCredits] Shutting down the plugin...");
                Bukkit.getPluginManager().disablePlugin(Notcredits.getInstance());
                return;
            }
        } else {
            if (!Notcredits.getInstance().getConfig().getString("data.type").equalsIgnoreCase("SQLite")) {
                Bukkit.getServer().getLogger().warning("[NotCredits] Incorrectly set database type in configuration file.");
                Bukkit.getServer().getLogger().warning("[NotCredits] Shutting down the plugin...");
                Bukkit.getPluginManager().disablePlugin(Notcredits.getInstance());
                return;
            }

            if (Notcredits.getInstance().getConfig().getString("data.file").equals("")) {
                Bukkit.getServer().getLogger().warning("[NotCredits] The SQLite database settings in the configuration file are not complete.");
                Bukkit.getServer().getLogger().warning("[NotCredits] Shutting down the plugin...");
                Bukkit.getPluginManager().disablePlugin(Notcredits.getInstance());
                return;
            }
        }
    }
}
