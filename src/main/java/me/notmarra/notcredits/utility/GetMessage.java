package me.notmarra.notcredits.utility;

import me.notmarra.notcredits.Notcredits;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class GetMessage {
    private static YamlConfiguration lang;

    public GetMessage() {
        Object langFileFromConfig = Notcredits.main.getConfig().get("lang", "en");
        String langFile = langFileFromConfig + ".yml";
        File langFileName = new File(Notcredits.getInstance().getDataFolder().getAbsolutePath() + "/lang", langFile);
        lang = YamlConfiguration.loadConfiguration(langFileName);
    }

    public void setLang(String lang) {
        Object langFileFromConfig = Notcredits.main.getConfig().getString("lang", "en");
        String langFile = langFileFromConfig + ".yml";
        File langFileName = new File(Notcredits.getInstance().getDataFolder().getAbsolutePath() + "/lang", langFile);
        GetMessage.lang = YamlConfiguration.loadConfiguration(langFileName);
    }

    public String getString(String key) {
        String message = lang.getString(key, key);
        return HexColors.translate(message);
    }

    public List<String> getStringList(String key) {
        List<String> messages = lang.getStringList(key);
        List<String> translatedMessages = new ArrayList<>();
        for (String message : messages) {
            translatedMessages.add(HexColors.translate(message));
        }
        return translatedMessages;
    }

}
