package com.notmarra.notcredits;

import com.notmarra.notcredits.util.LangFiles;
import com.notmarra.notcredits.util.Updater;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public final class Notcredits extends JavaPlugin {
   private static Notcredits instance;
   FileConfiguration config = this.getConfig();
   Updater updater;

   public Notcredits() {
      this.updater = new Updater(this, "NotCredits", this.getDescription().getVersion(), "1", "1", "ver");
   }

   @Override
   public void onEnable() {
      instance = this;
      this.saveDefaultConfig();

      this.updater.checkForUpdates();
      updater.checkFilesAndUpdate("config.yml", "lang/en.yml");

      //create lang files
      LangFiles.createLang();

      this.getLogger().info("Enabled successfully!");
   }
   @Override
   public void onDisable() {
      this.getLogger().info("Disabled successfully!");
   }

   public static Notcredits getInstance() {
      return instance;
   }
}
