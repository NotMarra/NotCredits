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
   @Override
   public void onEnable() {
      instance = this;
      this.config = this.getConfig();
      this.config.options().copyDefaults(true);
      this.saveDefaultConfig();

      CheckDBSettings checkDBSettings = new CheckDBSettings();
      checkDBSettings.CheckDBSettings();

      Database.getInstance().Database();
      Database.getInstance().initializeTables();

      if (this.config.getBoolean("vault")) {
         if (Bukkit.getPluginManager().getPlugin("Vault") != null) {
            Bukkit.getServicesManager().register(Economy.class, new Economy_NotCredits(), this, ServicePriority.Normal);
            Bukkit.getServer().getLogger().info("[NotCredits] Successfully connected to plugin vault!");
         } else {
            this.getLogger().warning("[NotCredits] Vault is enabled in config, but Vault not found");
            this.getLogger().warning("[NotCredits] Shutting down the plugin...");
            this.getServer().getPluginManager().disablePlugin(this);
         }
      }

      if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
         (new Placeholders()).register();
         Bukkit.getServer().getLogger().info("[NotCredits] Successfully loaded placeholders!");
      } else {
         Bukkit.getServer().getLogger().info("[NotCredits] PlaceholderAPI not found, not loading placeholders!");
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


      Bukkit.getServer().getLogger().info("[NotCredits] Enabled successfully!");
   }
   @Override
   public void onDisable() {
      Database.getInstance().disconnectFromDB();

      Bukkit.getServer().getLogger().info("[NotCredits] Disabled successfully!");
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
