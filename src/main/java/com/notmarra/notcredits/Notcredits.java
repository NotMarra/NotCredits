package com.notmarra.notcredits;

import com.notmarra.notcredits.data.Database;
import com.notmarra.notcredits.listeners.Playerjoin;
import com.notmarra.notcredits.listeners.Economy_NotCredits;
import com.notmarra.notcredits.listeners.Placeholders;
import com.notmarra.notcredits.utilities.*;
import net.milkbowl.vault.economy.Economy;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

import static com.notmarra.notcredits.utilities.Updater.checkFilesAndUpdate;

public final class Notcredits extends JavaPlugin {
   private static Notcredits instance;
   FileConfiguration config;
   Updater updater;
   public Notcredits() {
      this.updater = new Updater(this, this.getDescription().getVersion(), this.getDescription().getName(), "https://github.com/NotMarra/NotCredits/releases");
   }

   public Credits Credits;
   @Override
   public void onEnable() {
      instance = this;
      this.config = this.getConfig();
      this.config.options().copyDefaults(true);
      this.saveDefaultConfig();
      Credits = new Credits();

      CheckDBSettings checkDBSettings = new CheckDBSettings();
      checkDBSettings.CheckDBSettings();

      Database.getInstance().Database();
      Database.getInstance().initializeTables();


      if (this.config.getBoolean("vault")) {
         if (Bukkit.getPluginManager().getPlugin("Vault") != null) {
            Bukkit.getServicesManager().register(Economy.class, new Economy_NotCredits(), this, ServicePriority.Normal);
            this.getLogger().info("Successfully connected to plugin vault!");
         } else {
            this.getLogger().severe("Vault is enabled in config, but Vault not found");
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

      Lang.createLang();
      this.getServer().getPluginManager().registerEvents(new Playerjoin(), this);
      this.getCommand("credits").setExecutor(new CommandCreator());
      this.getCommand("nc").setExecutor(new CommandCreator());
      this.getCommand("notcredits").setExecutor(new CommandCreator());
      this.getCommand("credits").setTabCompleter(new TabCompletion());
      this.getCommand("nc").setTabCompleter(new TabCompletion());
      this.getCommand("notcredits").setTabCompleter(new TabCompletion());

      this.updater.checkForUpdates();

      checkFilesAndUpdate("config.yml", "lang/en.yml", "lang/cz.yml");

      this.getLogger().info("Enabled successfully!");

      if (CheckVersion.isSpigot()) {
         this.getLogger().severe("Your server is running on spigot!");
         this.getLogger().severe("This plugin is not compatible with spigot!");
         this.getLogger().severe("Please use PaperMC instead!");
         this.getLogger().severe("Shutting down the plugin...");
         this.getServer().getPluginManager().disablePlugin(this);
      }

   }
   @Override
   public void onDisable() {
      if (Database.getInstance().isConnected()) {
            Database.getInstance().disconnectFromDB();
      }

      this.getLogger().info("Disabled successfully!");
   }

   public static Notcredits getInstance() {
      return instance;
   }

   public void reload() {
      this.reloadConfig();
      this.config = this.getConfig();
      this.getLogger().info("Plugin reloaded successfully!");
   }
}
