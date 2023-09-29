/* Decompiler 24ms, total 186ms, lines 138 */
package me.notmarra.notcredits.utility;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import me.notmarra.notcredits.Notcredits;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

public class Updater {
   private Plugin plugin;
   private String currentVersion;
   private String pluginName;
   private String serverUrl;

   public Updater(Plugin plugin, String currentVersion, String pluginName, String serverUrl) {
      this.plugin = plugin;
      this.currentVersion = currentVersion;
      this.pluginName = pluginName;
      this.serverUrl = serverUrl;
   }

   public void checkForUpdates() {
      String latestVersion = this.getLatestVersion();
      if (latestVersion != null && !latestVersion.equals(this.currentVersion)) {
         Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "----------------------------------------------------------------");
         Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "A new version of " + this.pluginName + " is available!");
         Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "Current version: " + this.currentVersion + " / Latest version: " + latestVersion);
         Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "Download it at: https://www.spigotmc.org/resources/notcredits.109773/");
         Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "----------------------------------------------------------------");
      } else {
         Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "[NotCredits]  is up to date!");
      }

   }

   private String getLatestVersion() {
      try {
         HttpURLConnection connection = (HttpURLConnection)(new URL(this.serverUrl)).openConnection();
         connection.setRequestMethod("GET");
         connection.setConnectTimeout(5000);
         connection.setReadTimeout(5000);
         InputStream inputStream = connection.getInputStream();
         BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
         String latestVersion = bufferedReader.readLine();
         bufferedReader.close();
         inputStream.close();
         connection.disconnect();
         return latestVersion;
      } catch (IOException var5) {
         return null;
      }
   }

   public void checkAndAddMissingLangStrings() {
      File langDir = new File(Notcredits.main.getDataFolder(), "lang");
      File[] langFiles = langDir.listFiles();
      if (langFiles == null) {
         Bukkit.getLogger().warning("[NotCredits] Lang directory not found!");
      } else {
         File[] var3 = langFiles;
         int var4 = langFiles.length;

         String langCode;
         for(int var5 = 0; var5 < var4; ++var5) {
            File file = var3[var5];
            if (file.isFile() && file.getName().endsWith(".yml")) {
               YamlConfiguration langConfig = YamlConfiguration.loadConfiguration(file);
               String langFileName = file.getName();
               langCode = langFileName.substring(0, langFileName.lastIndexOf("."));
               InputStream resourceStream = this.getClass().getResourceAsStream("/lang/" + langFileName);
               if (resourceStream == null) {
                  Bukkit.getLogger().warning(String.format("[NotCredits] Lang file %s not found in resources.", langCode));
               } else {
                  YamlConfiguration resourceConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(resourceStream, StandardCharsets.UTF_8));
                  boolean changed = false;
                  Iterator var13 = resourceConfig.getKeys(true).iterator();

                  while(var13.hasNext()) {
                     String key = (String)var13.next();
                     if (!langConfig.contains(key)) {
                        langConfig.set(key, resourceConfig.get(key));
                        changed = true;
                     }
                  }

                  if (changed) {
                     try {
                        langConfig.save(file);
                        Bukkit.getLogger().info(String.format("[NotCredits] Lang file %s updated with missing strings from resources.", langCode));
                     } catch (IOException var16) {
                        Bukkit.getLogger().warning(String.format("[NotCredits] Error saving lang file %s: %s", langCode, var16.getMessage()));
                     }
                  }
               }
            }
         }

         File configFile = new File(this.plugin.getDataFolder(), "config.yml");
         YamlConfiguration config = YamlConfiguration.loadConfiguration(configFile);
         InputStream resourceStream = this.plugin.getResource("config.yml");
         if (resourceStream != null) {
            YamlConfiguration resourceConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(resourceStream, StandardCharsets.UTF_8));
            boolean changed = false;
            Iterator var22 = resourceConfig.getKeys(true).iterator();

            while(var22.hasNext()) {
               langCode = (String)var22.next();
               if (!config.contains(langCode)) {
                  config.set(langCode, resourceConfig.get(langCode));
                  changed = true;
               }
            }

            if (changed) {
               try {
                  config.save(configFile);
                  Bukkit.getLogger().info("[NotCredits] config.yml updated with missing keys from resources.");
               } catch (IOException var15) {
                  Bukkit.getLogger().warning("[NotCredits] Error saving config.yml: " + var15.getMessage());
               }
            }
         } else {
            Bukkit.getLogger().warning("[NotCredits] config.yml not found in resources.");
         }

      }
   }
}
