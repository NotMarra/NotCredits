package com.notmarra.notcredits;

import com.notmarra.notcredits.events.Economy_NotCredits;
import com.notmarra.notcredits.events.Placeholders;
import com.notmarra.notcredits.events.PlayerJoin;
import com.notmarra.notcredits.util.*;
import net.milkbowl.vault.economy.Economy;
import org.bstats.bukkit.Metrics;
import org.bstats.charts.SimplePie;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.List;

public final class NotCredits extends JavaPlugin {

    private static NotCredits instance;
    FileConfiguration config = this.getConfig();
    Updater updater;

    @Override
    public void onEnable() {
        instance = this;
        this.config = this.getConfig();
        this.config.options().copyDefaults(true);
        this.saveDefaultConfig();

        this.updater = new Updater(this, "NotCredits", this.getDescription().getVersion(), "1", "1", "https://github.com/NotMarra/NotCredits/releases");
        updater.checkFilesAndUpdate();

        LangFiles.createLang();
        LanguageManager.loadMessages();

        this.getServer().getPluginManager().registerEvents(new PlayerJoin(), this);
        this.getCommand("credits").setExecutor(new CommandCreator());
        this.getCommand("nc").setExecutor(new CommandCreator());
        this.getCommand("notcredits").setExecutor(new CommandCreator());
        this.getCommand("credits").setTabCompleter(new TabCompletion());
        this.getCommand("nc").setTabCompleter(new TabCompletion());
        this.getCommand("notcredits").setTabCompleter(new TabCompletion());

        Bukkit.getServer().getScheduler().runTaskTimerAsynchronously(this, () -> {
            this.updater.checkForUpdates();
        }, 0L, 432000L);

        if (this.config.getBoolean("vault")) {
            if (Bukkit.getPluginManager().getPlugin("Vault") != null) {
                Bukkit.getServicesManager().register(Economy.class, new Economy_NotCredits(), this, ServicePriority.Normal);
                this.getLogger().info("Successfully connected to plugin vault!");
            } else {
                this.getLogger().severe("Vault is enabled in config, but not found");
                this.getLogger().severe("Shutting down the plugin...");
                this.getServer().getPluginManager().disablePlugin(this);
            }
        }

        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            (new Placeholders(this)).register();
            this.getLogger().info("Successfully loaded placeholders!");
        } else {
            this.getLogger().info("PlaceholderAPI not found, not loading placeholders!");
        }

        Metrics metrics = new Metrics(this, 18464);

        metrics.addCustomChart(new SimplePie("language", () -> {
            return config.getString("lang", "unknown").toUpperCase();
        }));

        metrics.addCustomChart(new SimplePie("database_type", () -> {
            return config.getString("data.type", "unknown").toUpperCase();
        }));

        this.getLogger().info("Enabled successfully!");
    }

    @Override
    public void onDisable() {
        if (DatabaseManager.getInstance(this).isConnected()) {
            DatabaseManager.getInstance(this).close();
        }
        this.getLogger().info("Disabled successfully!");
    }

    public static NotCredits getInstance() {
        return instance;
    }

    public static final List<String> SUPPORTED_LANGUAGES = Arrays.asList(
            "en", "cz", "zhcn.yml", "ptbr"
    );

    public void reload() {
        this.reloadConfig();
        this.config = this.getConfig();
        LanguageManager.loadMessages();
        this.getLogger().info("Plugin reloaded successfully!");
    }
}
