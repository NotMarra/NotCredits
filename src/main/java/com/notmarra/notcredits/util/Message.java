package com.notmarra.notcredits.util;

import com.notmarra.notcredits.Notcredits;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map;

public class Message {

    public static String getMessage(String message) {
        return LanguageManager.getMessage(message);
    }

    public static List<String> getMessageList(String message) {
        return LanguageManager.getMessageList(message);
    }

    public static String getPrefix() {
        return LanguageManager.getMessage("prefix");
    }

    public static void sendMessage(Player player, String message_path, Boolean isConsole, Map<String, String> replacements) {
        String message = getMessage(message_path);
        transformText(player, isConsole, replacements, message);
    }

    public static void sendRawMessage(Player player, String message, Boolean isConsole, Map<String, String> replacements) {
        transformText(player, isConsole, replacements, message);
    }

    private static void transformText(Player player, Boolean isConsole, Map<String, String> replacements, String message) {
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

        if (player != null && !isConsole) {
            if (Notcredits.getInstance().nmsHandler != null) {
                Notcredits.getInstance().nmsHandler.sendChatMessage(player, message);
            } else {
                player.sendMessage(message);
            }
        }
        if (isConsole) {
            message = stripFormatting(message);
            Bukkit.getLogger().info(message);
        }
    }

    private static String stripFormatting(String input) {
        String withoutMiniMessage = input.replaceAll("<[^>]+>", "");

        return withoutMiniMessage.replaceAll("§[0-9a-fk-or]", "");
    }
}
