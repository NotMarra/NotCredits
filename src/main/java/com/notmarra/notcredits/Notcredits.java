package com.notmarra.notcredits;

import com.notmarra.notcredits.events.PlayerJoin;
import com.notmarra.notcredits.util.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public final class Notcredits extends JavaPlugin {
   private static Notcredits instance;
   FileConfiguration config = this.getConfig();
   Updater updater;

   @Override
   public void onEnable() {
      instance = this;
      this.config = this.getConfig();
      this.config.options().copyDefaults(true);
      this.saveDefaultConfig();

      this.updater = new Updater(this, "NotCredits", this.getDescription().getVersion(), "1", "1", "https://github.com/NotMarra/NotCredits/releases");

      DatabaseManager.getInstance(this).setupDB();

      LangFiles.createLang();
      this.getServer().getPluginManager().registerEvents(new PlayerJoin(), this);
      this.getCommand("credits").setExecutor(new CommandCreator());
      this.getCommand("nc").setExecutor(new CommandCreator());
      this.getCommand("notcredits").setExecutor(new CommandCreator());
      this.getCommand("credits").setTabCompleter(new TabCompletion());
      this.getCommand("nc").setTabCompleter(new TabCompletion());
      this.getCommand("notcredits").setTabCompleter(new TabCompletion());

      this.updater.checkForUpdates();
      updater.checkFilesAndUpdate("config.yml", "lang/en.yml");

      this.getLogger().info("Enabled successfully!");
   }
   @Override
   public void onDisable() {
      if (DatabaseManager.getInstance(this).isConnected()) {
         DatabaseManager.getInstance(this).close();
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
