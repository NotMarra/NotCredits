/* Decompiler 10ms, total 138ms, lines 129 */
package me.notmarra.notcredits;

import me.notmarra.notcredits.data.Database;
import me.notmarra.notcredits.listeners.Economy_NotCredits;
import me.notmarra.notcredits.listeners.Placeholders;
import me.notmarra.notcredits.utilities.*;
import net.milkbowl.vault.economy.Economy;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;
import org.checkerframework.checker.units.qual.N;

import java.util.logging.Logger;

public final class Notcredits extends JavaPlugin {
   private static Notcredits instance;
   FileConfiguration config;
   Updater updater;

   public Notcredits() {
      this.updater = new Updater(this, this.getDescription().getVersion(), this.getDescription().getName(), "https://www.spigotmc.org/resources/notcredits-mysql-sqlite-hexcolors-custom-currency.109773/");
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
      this.getCommand("credits").setExecutor(new CommandCreator());
      this.getCommand("credits").setTabCompleter(new TabCompletion());
      this.updater.checkForUpdates();
      Updater.checkFilesAndUpdate("config.yml", "lang/en.yml", "lang/cz.yml");


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
      this.getLogger().info("[NotCredits] Plugin reloaded successfully!");
   }
}
