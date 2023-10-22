/* Decompiler 10ms, total 138ms, lines 129 */
package me.notmarra.notcredits;

import java.sql.Connection;
import java.sql.SQLException;

import me.notmarra.notcredits.listeners.Economy_NotCredits;
import me.notmarra.notcredits.listeners.Placeholders;
import me.notmarra.notcredits.listeners.PlayerJoin;
import me.notmarra.notcredits.utility.CommandCreator;
import me.notmarra.notcredits.utility.Files;
import me.notmarra.notcredits.utility.GetMessage;
import me.notmarra.notcredits.utility.TabCompletion;
import me.notmarra.notcredits.utility.Updater;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public final class Notcredits extends JavaPlugin {
   private Economy_NotCredits economy;
   private Connection connection;
   FileConfiguration config;
   private static Notcredits main;
   String pluginVersion = this.getDescription().getVersion();
   String pluginName = this.getName();
   String serverUrl = "https://raw.githubusercontent.com/NotMarra/NotCredits/main/version.txt";
   Updater updater;

   public Notcredits() {
      this.updater = new Updater(this, this.pluginVersion, this.pluginName, this.serverUrl);
   }

   public void onEnable() {
      this.config = this.getConfig();
      ConfigurationSection data = this.config.getConfigurationSection("data");
      ConfigurationSection mysql = data.getConfigurationSection("mysql");
      this.getConfig().options().copyDefaults(true);
      this.saveDefaultConfig();
      main = this;
      if (data.getString("type").equalsIgnoreCase("MySQL")) {
         if (mysql.getString("hostname").equals("") || mysql.getString("port").equals("") || mysql.getString("username").equals("") || mysql.getString("database").equals("") || mysql.getString("password").equals("")) {
            this.getLogger().warning("[NotCredits] The MySQL database settings in the configuration file are not complete.");
            this.getLogger().warning("[NotCredits] Shutting down the plugin...");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
         }
      } else {
         if (!data.getString("type").equalsIgnoreCase("SQLite")) {
            this.getLogger().warning("[NotCredits] Incorrectly set database type in configuration file.");
            this.getLogger().warning("[NotCredits] Shutting down the plugin...");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
         }

         if (data.getString("file").equals("")) {
            this.getLogger().warning("[NotCredits] The SQLite database settings in the configuration file are not complete.");
            this.getLogger().warning("[NotCredits] Shutting down the plugin...");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
         }
      }

      try {
         Database.database = Database.getInstance();
         connection = Database.database.getConnection();
         Database.database.initializeDatabase();
      } catch (SQLException var5) {
         var5.printStackTrace();
         Bukkit.getPluginManager().disablePlugin(this);
      }

      int dbInterval = 300;
      new BukkitRunnable() {
         @Override
         public void run() {
            if (connection != null) {
               try {
                  if (!connection.isValid(2)) {
                     Database.database = Database.getInstance();
                     connection = Database.database.getConnection();
                  }
               } catch (SQLException e) {
                   throw new RuntimeException(e);
               }
            }
         }
      }.runTaskTimer(this, 0, dbInterval * 20);

      if (this.config.getBoolean("vault")) {
         if (Bukkit.getPluginManager().getPlugin("Vault") != null) {
            Bukkit.getServicesManager().register(Economy.class, new Economy_NotCredits(), this, ServicePriority.Normal);
            Bukkit.getServer().getLogger().info("[NotCredits] Successfully connected to plugin vault!");
         } else {
            this.getLogger().warning("[NotCredits] Vault is enabled in config, but plugin not found");
            this.getLogger().warning("[NotCredits] Shutting down the plugin...");
            this.getServer().getPluginManager().disablePlugin(this);
         }
      }

      Files.createFolder("lang");
      Files.createFile("lang/en.yml");
      Files.createFile("lang/cz.yml");
      this.updater.checkAndAddMissingLangStrings();
      this.getCommand("credits").setTabCompleter(new TabCompletion());
      this.getCommand("credits").setExecutor(new CommandCreator(this));
      this.getServer().getPluginManager().registerEvents(new PlayerJoin(), this);
      if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
         (new Placeholders()).register();
         Bukkit.getServer().getLogger().info("[NotCredits] Successfully loaded placeholders!");
      } else {
         Bukkit.getServer().getLogger().info("[NotCredits] PlaceholderAPI not found, not loading placeholders!");
      }

      this.updater.checkForUpdates();
      Bukkit.getServer().getLogger().info("[NotCredits] Loaded successfully");
   }

   public void onDisable() {
      try {
         Database.database.closeConnection();
      } catch (SQLException var2) {
         var2.printStackTrace();
      }

      Bukkit.getServer().getLogger().info("[NotCredits] Disabled successfully!");
   }

   public static Notcredits getInstance() {
      return main;
   }


   public void reload() {
      main.reloadConfig();
      String lang = main.getConfig().getString("lang", "en");
      GetMessage message = new GetMessage();
      message.setLang(lang);
      this.getLogger().info("[NotCredits] Plugin reloaded successfully!");
   }
}
