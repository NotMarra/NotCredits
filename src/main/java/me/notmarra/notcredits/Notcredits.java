package me.notmarra.notcredits;

import me.notmarra.notcredits.Listeners.Placeholders;
import me.notmarra.notcredits.Listeners.playerJoin;
import me.notmarra.notcredits.utility.CommandCreator;
import me.notmarra.notcredits.utility.Files;
import me.notmarra.notcredits.Data.Database;

import me.notmarra.notcredits.utility.GetMessage;
import me.notmarra.notcredits.utility.TabCompletion;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;

public final class Notcredits extends JavaPlugin {

    FileConfiguration config;
    public static Notcredits main;
    private Database database;
    Files files = new Files();

    @Override
    public void onEnable() {
        // Plugin startup logic
        config = getConfig();
        getConfig().options().copyDefaults(true);
        saveDefaultConfig();
        main = this;

        //Database
            // checking the settings in the configuration file
                if (getConfig().getString("data").equals("MySQL")) {
                    if (getConfig().getString("mysql_hostname").equals("")
                            || getConfig().getString("mysql_port").equals("")
                            || getConfig().getString("mysql_username").equals("")
                            || getConfig().getString("mysql_database").equals("")
                            || getConfig().getString("mysql_password").equals("")) {
                        getLogger().warning("[NotCredits] The MySQL database settings in the configuration file are not complete.");
                        getLogger().warning("[NotCredits] Shutting down the plugin...");
                        Bukkit.getPluginManager().disablePlugin(this);
                        return;
                    }
                } else if (getConfig().getString("data").equals("SQLite")) {
                    if (getConfig().getString("data_file").equals("")) {
                        getLogger().warning("[NotCredits] The SQLite database settings in the configuration file are not complete.");
                        getLogger().warning("[NotCredits] Shutting down the plugin...");
                        Bukkit.getPluginManager().disablePlugin(this);
                        return;
                    }
                } else {
                    getLogger().warning("[NotCredits] Incorrectly set database type in configuration file.");
                    getLogger().warning("[NotCredits] Shutting down the plugin...");
                    Bukkit.getPluginManager().disablePlugin(this);
                    return;
                }
            //db intialize
                try {
                    database = Database.getInstance();
                    database.initializeDatabase();
                } catch (SQLException e) {
                    e.printStackTrace();
                }

        //lang
        Files.createFolder("lang");
        Files.createFile("lang/en.yml");
        Files.createFile("lang/cz.yml");
        files.updateLanguageFiles();

        //tab complete
        getCommand("credits").setTabCompleter(new TabCompletion());

        // commands
        this.getCommand("credits").setExecutor(new CommandCreator(this));

        //register events
        getServer().getPluginManager().registerEvents(new playerJoin(), this);

        //placeholders
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new Placeholders().register();
            Bukkit.getServer().getLogger().info("[NotCredits] Successfully loaded placeholders!");
        } else {
            Bukkit.getServer().getLogger().info("[NotCredits] PlaceholderAPI not found, not loading placeholders!");
        }

        // start msg
        Bukkit.getServer().getLogger().info("[NotCredits] Loaded successfully");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic

        try {
            database.closeConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        // off msg
        Bukkit.getServer().getLogger().info("[NotCredits] Disabled successfully!");
    }

    public static Notcredits getInstance() {
        return main;
    }

    public Database getDatabase() {
        return database;
    }

    public void reload() {
        main.reloadConfig();
        String lang = main.getConfig().getString("lang", "en");
        GetMessage message = new GetMessage();
        message.setLang(lang);
        getLogger().info("[NotCredits] Plugin reloaded successfully!");
    }


}
