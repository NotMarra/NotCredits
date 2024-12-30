package com.notmarra.notcredits.util;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

import static com.notmarra.notcredits.NotCredits.SUPPORTED_LANGUAGES;

public class Updater {
    private final Plugin plugin;
    private final String pluginName;
    private final String currentVersion;
    private final String pluginURL;
    private final String configVersion;
    private final String langVersion;

    public Updater(Plugin plugin, String pluginName, String currentVersion, String configVersion, String langVersion, String pluginURL) {
        this.plugin = plugin;
        this.pluginName = pluginName;
        this.currentVersion = currentVersion;
        this.pluginURL = pluginURL;
        this.configVersion = configVersion;
        this.langVersion = langVersion;
    }
    public void checkForUpdates() {
        String latestVersion = getLatestVersion();
        if (latestVersion != null) {
            if (currentVersion.contains("SNAPSHOT")) {
                plugin.getLogger().warning("You are running on a snapshot build of " + pluginName + "!");
            } else if (currentVersion.contains("DEV")) {
                plugin.getLogger().warning("You are running on a development build of " + pluginName + "!");
            } else if (currentVersion.equals(latestVersion) || Double.parseDouble(currentVersion) > Double.parseDouble(latestVersion)) {
                plugin.getLogger().info("You are running the latest version of " + pluginName + "!");
            } else {
                plugin.getLogger().warning("-----------------------------------------------------");
                plugin.getLogger().warning("There is a new version of " + pluginName + " available!");
                plugin.getLogger().warning("You are running on version v" + currentVersion + ", the latest version is v" + latestVersion + ".");
                plugin.getLogger().warning("Download it from " + pluginURL);
                plugin.getLogger().warning("-----------------------------------------------------");
            }
        } else {
            plugin.getLogger().warning("Failed to check for updates!");
        }
    }

    private String getLatestVersion() {
        if (currentVersion.contains("SNAPSHOT") || currentVersion.contains("DEV")) {
            return currentVersion;
        } else {
            return getResponse();
        }
    }

    private String getResponse() {
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL("https://api.github.com/repos/NotMarra/NotCredits/releases/latest").openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);
            String response = getStringFromURL(connection);
            if (response.contains("\"message\":\"Not Found\"")) {
                return null;
            }
            String version = response.substring(response.indexOf("\"tag_name\":\"") + 12, response.indexOf("\",\"target_commitish\""));
            if (version.startsWith("v")) {
                version = version.substring(1);
                return version;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @NotNull
    private static String getStringFromURL(HttpURLConnection connection) throws IOException {
        InputStream inputStream = connection.getInputStream();
        BufferedReader bufferedReader = new BufferedReader(new java.io.InputStreamReader(inputStream));
        StringBuilder stringBuilder = new StringBuilder();
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            stringBuilder.append(line);
            stringBuilder.append("\n");
        }
        bufferedReader.close();
        inputStream.close();
        return stringBuilder.toString();
    }

    public void checkFilesAndUpdate() {
        if (!Objects.equals(plugin.getConfig().getString("ver"), configVersion)) {
            updateFile("config.yml", YamlConfiguration.loadConfiguration(new File("config.yml")), configVersion);
        }
        for (String lang : SUPPORTED_LANGUAGES) {
            String fileName = "lang/" + lang + ".yml";
            File langFile = new File(plugin.getDataFolder(), fileName);
            YamlConfiguration langConfig = YamlConfiguration.loadConfiguration(langFile);

            if (!Objects.equals(langConfig.getString("ver"), langVersion)) {
                updateFile(fileName, langConfig, langVersion);
            }
        }
    }

    private void updateFile(String fileName, YamlConfiguration fileConfig, String newVersion) {
        try {
            InputStream defaultConfigStream = plugin.getResource(fileName);
            if (defaultConfigStream != null) {
                YamlConfiguration defaultConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(defaultConfigStream, StandardCharsets.UTF_8));

                for (String key : defaultConfig.getKeys(true)) {
                    if (!fileConfig.contains(key)) {
                        fileConfig.set(key, defaultConfig.get(key));
                    }
                }

                fileConfig.set("ver", newVersion);
                fileConfig.save(new File(plugin.getDataFolder(), fileName));
                plugin.getLogger().info("Updated " + fileName + " to version " + newVersion);
            }
        } catch (IOException e) {
            plugin.getLogger().warning("Failed to update " + fileName + ": " + e.getMessage());
        }
    }
}
