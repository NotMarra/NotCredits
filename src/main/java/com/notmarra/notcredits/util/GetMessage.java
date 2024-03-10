package com.notmarra.notcredits.util;

import com.notmarra.notcredits.Notcredits;

import java.util.List;

public class GetMessage {
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
}
