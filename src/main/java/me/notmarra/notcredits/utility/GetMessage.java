/* Decompiler 4ms, total 164ms, lines 45 */
package me.notmarra.notcredits.utility;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import me.notmarra.notcredits.Notcredits;
import org.bukkit.configuration.file.YamlConfiguration;

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
      List<String> translatedMessages = new ArrayList();
      Iterator var4 = messages.iterator();

      while(var4.hasNext()) {
         String message = (String)var4.next();
         translatedMessages.add(HexColors.translate(message));
      }

      return translatedMessages;
   }
}
