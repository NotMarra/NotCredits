package com.notmarra.notcredits;

import com.notmarra.notcredits.events.Economy_NotCredits;
import com.notmarra.notcredits.events.Placeholders;
import com.notmarra.notcredits.events.PlayerJoin;
import com.notmarra.notcredits.nms.NMSHandler;
import com.notmarra.notcredits.nms.versions.NMSHandler_HS;
import com.notmarra.notcredits.nms.versions.NMSHandler_Legacy;
import com.notmarra.notcredits.util.*;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.milkbowl.vault.economy.Economy;
import org.bstats.bukkit.Metrics;
import org.bstats.charts.SimplePie;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public final class Notcredits extends JavaPlugin {
   private static Notcredits instance;
   FileConfiguration config = this.getConfig();
   Updater updater;
   public NMSHandler nmsHandler;
   public BukkitAudiences adventure;

   @Override
   public void onEnable() {
      instance = this;
      this.config = this.getConfig();
      this.config.options().copyDefaults(true);
      this.saveDefaultConfig();

      this.updater = new Updater(this, "NotCredits", this.getDescription().getVersion(), "1", "1", "https://github.com/NotMarra/NotCredits/releases");

      DatabaseManager.getInstance(this).setupDB();

      this.nmsHandler = getNMSHandler();

      this.adventure = BukkitAudiences.create(this);

      LangFiles.createLang();
      LanguageManager.loadMessages();

      this.getServer().getPluginManager().registerEvents(new PlayerJoin(), this);
      this.getCommand("credits").setExecutor(new CommandCreator());
      this.getCommand("nc").setExecutor(new CommandCreator());
      this.getCommand("notcredits").setExecutor(new CommandCreator());
      this.getCommand("credits").setTabCompleter(new TabCompletion());
      this.getCommand("nc").setTabCompleter(new TabCompletion());
      this.getCommand("notcredits").setTabCompleter(new TabCompletion());

      this.updater.checkForUpdates();
      updater.checkFilesAndUpdate("config.yml", "lang/en.yml");

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

   public static Notcredits getInstance() {
      return instance;
   }

   public void reload() {
      this.reloadConfig();
      this.config = this.getConfig();
      LanguageManager.loadMessages();
      this.getLogger().info("Plugin reloaded successfully!");
   }

   public static final List<String> MINIMESSAGE_SUPPORTED_VERSIONS = Arrays.asList(
           "1.16.1", "1.16.2", "1.16.3", "1.16.4", "1.16.5",
           "1.17", "1.17.1",
           "1.18", "1.18.1", "1.18.2",
           "1.19", "1.19.1", "1.19.2", "1.19.3", "1.19.4",
           "1.20", "1.20.1", "1.20.2", "1.20.3", "1.20.4", "1.20.5", "1.20.6",
           "1.21", "1.21.1"
   );

   public static final List<String> SUPPORTED_LANGUAGES = Arrays.asList(
           "en", "cz", "zhcn", "ptbr"
   );

   private NMSHandler getNMSHandler() {
      String version = Bukkit.getVersion().split("MC: ")[1].split("\\)")[0];

      getLogger().info("Using NMS version: " + version);

      if (MINIMESSAGE_SUPPORTED_VERSIONS.contains(version)) {
         getLogger().info("Using MiniMessage Handler.");
         return new NMSHandler_HS();
      } else {
         getLogger().info("Using Legacy chat Handler.");
         return new NMSHandler_Legacy();
      }
   }

   public void setBalance(String uuid, double amount) {
      DatabaseManager.getInstance(this).setBalance(uuid, amount);
   }

    public double getBalance(String uuid) {
        return DatabaseManager.getInstance(this).getBalance(uuid);
    }

    public void setBalanceByName(String name, double amount) {
        DatabaseManager.getInstance(this).setBalanceByPlayerName(name, amount);
    }

    public double getBalanceByName(String name) {
        return DatabaseManager.getInstance(this).getBalanceByPlayerName(name);
    }

    public double getBalanceByOrder(int order) {
        return DatabaseManager.getInstance(this).getBalanceByOrder(order);
    }

    public String getTopPlayerName(int order) {
        return DatabaseManager.getInstance(this).getPlayerByBalance(order);
    }

    public List<Map<String, Double>> getTopPlayersWithBalance(int position, int amount) {
        return DatabaseManager.getInstance(this).getPlayersByBalance(position, amount);
    }
}
