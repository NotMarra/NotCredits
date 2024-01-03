package com.notmarra.notcredits.utilities;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class Messages {
    public static String messageGetString(String key) {
        String prefix = GetMessage.getMessage("prefix");
        String message = GetMessage.getMessage(key);
        String finalMessage = prefix + message;
        return finalMessage;
    }

    public static List<String> messageGetStringList(String key) {
        List<String> message = GetMessage.getStringList(key);
        return message;
    }

    public static String messageReplace(String message, String replace, String replacedText) {
        return message.replace(replace, replacedText);
    }

    public static String messageReplaceMultiple(String message, String[] replace, String[] replacedText) {
        String finalMessage = message;
        for (int i = 0; i < replace.length; i++) {
            finalMessage = finalMessage.replace(replace[i], replacedText[i]);
        }
        return finalMessage;
    }

    public static @NotNull Component mm(String message) {
        MiniMessage mm = MiniMessage.miniMessage();
        return mm.deserialize(message);
    }
}
