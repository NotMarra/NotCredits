package com.notmarra.notcredits.utilities;

import com.notmarra.notcredits.Notcredits;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class Updater {
    private final Plugin plugin;
    private final String currentVersion;
    private final String pluginName;
    private final String pluginURL;

    public Updater(Plugin plugin, String currentVersion, String pluginName, String pluginURL) {
        this.plugin = plugin;
        this.currentVersion = currentVersion;
        this.pluginName = pluginName;
        this.pluginURL = pluginURL;
    }


    public void checkForUpdates() {
        String latestVersion = getLatestVersion();
        if (latestVersion != null) {
            if (currentVersion.contains("SNAPSHOT")) {
                plugin.getLogger().warning("You are running on a snapshot build of " + pluginName + "!");
            } else if (currentVersion.contains("DEV")){
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
        String url = "https://api.github.com/repos/NotMarra/NotCredits/releases/latest";

        if (currentVersion.contains("SNAPSHOT") || currentVersion.contains("DEV")) {
            return currentVersion;
        } else {
            String version = getResponse(url);
            if (version != null) {
                return version;
            }
        }

        return null;
    }

    private String getResponse(String url) {
        try {
            String realUrl = url;
            HttpURLConnection connection = (HttpURLConnection) new URL(realUrl).openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);
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
            String response = stringBuilder.toString();
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

    public static void checkFilesAndUpdate(String... fileNames) {
        for (String fileName : fileNames) {
            update(fileName);
        }
    }

    private static void update(String fileName) {
        File file = new File(Notcredits.getInstance().getDataFolder().getAbsolutePath(), fileName);
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            Notcredits.getInstance().saveResource(fileName, false);
        } else {
            try {
                YamlConfiguration actualConfig = YamlConfiguration.loadConfiguration(file);

                URL url = new URL("https://raw.githubusercontent.com/NotMarra/NotCredits/master/src/main/resources/" + fileName);
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
