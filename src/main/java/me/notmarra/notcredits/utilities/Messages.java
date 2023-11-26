package me.notmarra.notcredits.utilities;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.jetbrains.annotations.NotNull;

public class Messages {
    public static String messageGetString(String key) {
        String prefix = GetMessage.getMessage("prefix");
        String message = GetMessage.getMessage(key);
        String finalMessage = prefix + message;
        return finalMessage;
    }

    public static String messageGetStringList(String key) {
        String message = GetMessage.getStringList(key).toString();
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
