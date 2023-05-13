package me.notmarra.notcredits.utility;

import me.notmarra.notcredits.Notcredits;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import static org.bukkit.Bukkit.getLogger;

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
        String latestVersion = getLatestVersion();
        if (latestVersion != null && !latestVersion.equals(currentVersion)) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "----------------------------------------------------------------");
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "A new version of " + plugin.getName() + " is available!");
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "Current version: " + currentVersion + " / Latest version: " + latestVersion);
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "Download it at: " + serverUrl);
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "----------------------------------------------------------------");
        } else {
            Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + plugin.getName() + " is up to date!");
        }
    }

    private String getLatestVersion() {
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(serverUrl).openConnection();
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
        } catch (IOException exception) {
            return null;
        }
    }


    public void checkAndAddMissingLangStrings() {
        File langDir = new File(Notcredits.main.getDataFolder(), "lang");
        File[] langFiles = langDir.listFiles();

        if (langFiles == null) {
            getLogger().warning("[NotCredits] Lang directory not found!");
            return;
        }

        for (File file : langFiles) {
            if (file.isFile() && file.getName().endsWith(".yml")) {
                YamlConfiguration langConfig = YamlConfiguration.loadConfiguration(file);
                String langFileName = file.getName();
                String langCode = langFileName.substring(0, langFileName.lastIndexOf("."));

                // load lang file from resources
                InputStream resourceStream = getClass().getResourceAsStream("/lang/" + langFileName);
                if (resourceStream != null) {
                    YamlConfiguration resourceConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(resourceStream, StandardCharsets.UTF_8));
                    boolean changed = false;

                    // Check for missing strings in lang config
                    for (String key : resourceConfig.getKeys(true)) {
                        if (!langConfig.contains(key)) {
                            langConfig.set(key, resourceConfig.get(key));
                            changed = true;
                        }
                    }

                    // Save updated lang config if there were changes
                    if (changed) {
                        try {
                            langConfig.save(file);
                            getLogger().info(String.format("[NotCredits] Lang file %s updated with missing strings from resources.", langCode));
                        } catch (IOException ex) {
                            getLogger().warning(String.format("[NotCredits] Error saving lang file %s: %s", langCode, ex.getMessage()));
                        }
                    }
                } else {
                    getLogger().warning(String.format("[NotCredits] Lang file %s not found in resources.", langCode));
                }
            }
        }
    }


}
