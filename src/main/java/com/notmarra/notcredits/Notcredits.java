package com.notmarra.notcredits;

import com.notmarra.notcredits.util.Files;
import com.notmarra.notcredits.util.LangFiles;
import com.notmarra.notcredits.util.Updater;
import net.milkbowl.vault.economy.Economy;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

public final class Notcredits extends JavaPlugin {
   private static Notcredits instance;
   FileConfiguration config = this.getConfig();
   Updater updater;

   public Notcredits() {
      this.updater = new Updater(this, "NotCredits", this.getDescription().getVersion(), config.getString("ver"), Files.getStringFromFile("lang/en.yml", "ver"), "https://github.com/NotMarra/NotCredits/releases");
   }

   @Override
   public void onEnable() {
      instance = this;
      this.saveDefaultConfig();

      this.updater.checkForUpdates();

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
