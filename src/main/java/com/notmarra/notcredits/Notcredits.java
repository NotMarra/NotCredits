package com.notmarra.notcredits;

import com.notmarra.notcredits.util.DatabaseManager;
import com.notmarra.notcredits.util.LangFiles;
import com.notmarra.notcredits.util.Updater;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public final class Notcredits extends JavaPlugin {
   private static Notcredits instance;
   FileConfiguration config = this.getConfig();
   Updater updater;
   DatabaseManager db = new DatabaseManager();


   public Notcredits() {
      this.updater = new Updater(this, "NotCredits", this.getDescription().getVersion(), "1", "1", "https://github.com/NotMarra/NotCredits/releases");
   }

   @Override
   public void onEnable() {
      instance = this;
      this.saveDefaultConfig();

      //setup database
      db.setupDB();

      //create lang files
      LangFiles.createLang();

      //check for updates
      this.updater.checkForUpdates();
      updater.checkFilesAndUpdate("config.yml", "lang/en.yml");

      this.getLogger().info("Enabled successfully!");
   }
   @Override
   public void onDisable() {
      if (db.isConnected()) {
         db.close();
      }
      this.getLogger().info("Disabled successfully!");
   }

   public static Notcredits getInstance() {
      return instance;
   }
}
