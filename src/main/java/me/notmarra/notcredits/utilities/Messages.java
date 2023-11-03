package me.notmarra.notcredits.utilities;

import me.notmarra.notcredits.Notcredits;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.jetbrains.annotations.NotNull;

public class Messages {
    public static String getString(String key) {
        String prefix = GetMessage.getMessage("prefix");
        String message = GetMessage.getMessage(key);
        String finalMessage = prefix + message;
        return finalMessage;
    }

    public static String getStringList(String key) {
        String message = GetMessage.getStringList(key).toString();
        return message;
    }

    public static String replace(String message, String replace, String replacedText) {
        return message.replace(replace, replacedText);
    }

    //replace multiple placeholders
    public static String replaceMultiple(String message, String[] replace, String[] replacedText) {
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
