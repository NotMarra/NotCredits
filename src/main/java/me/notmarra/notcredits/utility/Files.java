/* Decompiler 4ms, total 128ms, lines 40 */
package me.notmarra.notcredits.utility;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import me.notmarra.notcredits.Notcredits;
import org.bukkit.configuration.file.YamlConfiguration;

public class Files {
   public static void createFolder(String name) {
      File folder = new File(Notcredits.main.getDataFolder(), name);
      if (!folder.exists()) {
         try {
            folder.mkdir();
         } catch (Exception var3) {
            var3.printStackTrace();
         }
      }

   }

   public static void createFile(String name) {
      File file = new File(Notcredits.main.getDataFolder().getAbsolutePath(), name);
      if (!file.exists()) {
         file.getParentFile().mkdirs();
         Notcredits.main.saveResource(name, false);
      }

      try {
         Reader reader = new InputStreamReader(Notcredits.main.getResource(name));
         YamlConfiguration.loadConfiguration(reader);
         reader.close();
      } catch (IOException var3) {
         var3.printStackTrace();
      }

   }
}
