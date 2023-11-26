package me.notmarra.notcredits.utilities;

import me.notmarra.notcredits.Notcredits;
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
            if (!currentVersion.equals(latestVersion)) {
                plugin.getLogger().warning("There is a new version of " + pluginName + " available! You are running version " + currentVersion + ", the latest version is " + latestVersion + ". Download it at " + pluginURL);
            } else {
                plugin.getLogger().info("You are running the latest version of " + pluginName + "!");
            }
        } else {
            plugin.getLogger().warning("Failed to check for updates!");
        }
    }

    private String getLatestVersion() {
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL("https://raw.githubusercontent.com/notmarra/NotCredits/main/version.txt").openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);
            InputStream inputStream = connection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new java.io.InputStreamReader(inputStream));
            String latestVersion = bufferedReader.readLine();
            bufferedReader.close();
            inputStream.close();
            return latestVersion;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void checkFilesAndUpdate(String... fileNames) {
        for (String fileName : fileNames) {
            update(fileName);
        }
    }
    private static void update(String fileName) {
        //update files in plugin folder with files from github
        File file = new File(Notcredits.getInstance().getDataFolder(), fileName);
        if (!file.exists()) {
            Notcredits.getInstance().saveResource(fileName, false);
        } else {
            try {
                URL url = new URL("https://raw.githubusercontent.com/NotMarra/NotCredits/main/src/main/resources/" + fileName);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
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
                String newFileContent = stringBuilder.toString();
                String oldFileContent = YamlConfiguration.loadConfiguration(file).saveToString();
                if (!newFileContent.equals(oldFileContent)) {
                    OutputStream outputStream = new FileOutputStream(file);
                    outputStream.write(newFileContent.getBytes(StandardCharsets.UTF_8));
                    outputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
