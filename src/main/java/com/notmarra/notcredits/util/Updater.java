package com.notmarra.notcredits.util;

import com.notmarra.notcredits.Notcredits;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

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

    public void checkFilesAndUpdate(String... files) {
        for (String fileName : files) {
            if (fileName.equals("config.yml") && !Objects.equals(Notcredits.getInstance().getConfig().getString("ver"), Updater.this.configVersion)) {
                update(fileName);
            } else if (!Objects.equals(Files.getStringFromFile(Notcredits.getInstance().getDataFolder().getAbsolutePath() + "/" + fileName, "ver"), Updater.this.langVersion))
                if (fileName.contains("lang/")) {
                    update(fileName);
                }
        }
    }

    private static void update(String fileName) {
        File file = new File(Notcredits.getInstance().getDataFolder().getAbsolutePath(), fileName);
        if (!file.exists()) {
            Files.createFile(fileName);
        } else {
            try {
                YamlConfiguration actualConfig = YamlConfiguration.loadConfiguration(file);
                String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
                URL url;

                if (version.startsWith("v1_16") || version.startsWith("v1_17") || version.startsWith("v1_18") || version.startsWith("v1_19") || version.startsWith("v1_20") || version.startsWith("v1_21")) {
                    url = new URL("https://raw.githubusercontent.com/NotMarra/NotCredits/master/src/main/resources/" + fileName);
                } else {
                    url = new URL("https://raw.githubusercontent.com/NotMarra/NotCredits/master/src/main/resources/" + fileName.replace(".yml", "_nh.yml"));
                }

                InputStreamReader urlReader = new InputStreamReader(url.openStream(), StandardCharsets.UTF_8);
                YamlConfiguration gitConfig = YamlConfiguration.loadConfiguration(urlReader);

                for (String key : gitConfig.getKeys(true)) {
                    if (!actualConfig.contains(key)) {
                        actualConfig.set(key, gitConfig.get(key));
                    }
                }

                actualConfig.save(file);
                urlReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}
