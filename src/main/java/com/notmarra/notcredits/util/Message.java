package com.notmarra.notcredits.util;

import com.notmarra.notcredits.Notcredits;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map;

public class Message {

    static NMSMessageSender messageSender = new NMSMessageSender();

    public static String getMessage(String message) {
        if (Files.getStringFromFile(Notcredits.getInstance().getDataFolder().getAbsolutePath() + "/lang/" + Notcredits.getInstance().getConfig().getString("lang") + ".yml", message) != null) {
            return Files.getStringFromFile(Notcredits.getInstance().getDataFolder().getAbsolutePath() + "/lang/" + Notcredits.getInstance().getConfig().getString("lang") + ".yml", message);
        } else {
            return Files.getStringFromFile(Notcredits.getInstance().getDataFolder().getAbsolutePath() + "/lang/en.yml", message);
        }
    }

    public static List<String> getMessageList(String message) {
        if (Files.getStringListFromFile(Notcredits.getInstance().getDataFolder().getAbsolutePath() + "/lang/" + Notcredits.getInstance().getConfig().getString("lang") + ".yml", message) != null) {
            return Files.getStringListFromFile(Notcredits.getInstance().getDataFolder().getAbsolutePath() + "/lang/" + Notcredits.getInstance().getConfig().getString("lang") + ".yml", message);
        } else {
            return Files.getStringListFromFile(Notcredits.getInstance().getDataFolder().getAbsolutePath() + "/lang/en.yml", message);
        }
    }

    public static String getPrefix() {
        return Files.getStringFromFile(Notcredits.getInstance().getDataFolder().getAbsolutePath() + "/lang/" + Notcredits.getInstance().getConfig().getString("lang") + ".yml", "prefix");
    }


    public static void sendMessage(Player player, String message, Boolean isConsole, Map<String, String> replacements) {
        if (message.contains("%prefix%") && !isConsole) {
            message = message.replace("%prefix%", getPrefix());
        } else {
            message = message.replace("%prefix%", "");
        }

        if (replacements != null) {
            for (Map.Entry<String, String> entry : replacements.entrySet()) {
                message = message.replace("%" + entry.getKey() + "%", entry.getValue());
            }
        }

        if (player != null) {
            messageSender.sendChatMessage(player, message);
        }
        if (isConsole) {
            Bukkit.getLogger().info(message);
        }
    }
}
