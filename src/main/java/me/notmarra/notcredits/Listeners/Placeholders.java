/* Decompiler 8ms, total 143ms, lines 77 */
package me.notmarra.notcredits.Listeners;

import java.sql.SQLException;
import java.text.DecimalFormat;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.notmarra.notcredits.Notcredits;
import me.notmarra.notcredits.Data.Database;
import org.bukkit.entity.Player;

public class Placeholders extends PlaceholderExpansion {
   public boolean canRegister() {
      return true;
   }

   public String getAuthor() {
      return "NotMarra";
   }

   public String getIdentifier() {
      return "NotCredits";
   }

   public String getVersion() {
      return Notcredits.main.getDescription().getVersion();
   }

   public String onPlaceholderRequest(Player player, String identifier) {
      if (player == null) {
         return "";
      } else if (identifier.equals("credits")) {
         String pname = player.getUniqueId().toString();
         double credits = 0.0D;

         try {
            credits = Database.database.getCreditsByUUID(pname);
         } catch (SQLException var7) {
            var7.printStackTrace();
         }

         if (Notcredits.main.getConfig().getBoolean("balance_decimal")) {
            DecimalFormat decimalFormat = new DecimalFormat(Notcredits.main.getConfig().getString("balance_format"));
            return String.valueOf(decimalFormat.format(credits));
         } else {
            return String.valueOf(Math.round(credits));
         }
      } else {
         if (identifier.startsWith("top_")) {
            String[] parts = identifier.split("_");
            int position;
            if (parts.length == 2) {
               position = Integer.parseInt(parts[1]);

               try {
                  if (Math.round(Database.database.getCreditsByOrder(position)) == -1L) {
                     return "NaN";
                  }

                  return String.valueOf(Math.round(Database.database.getCreditsByOrder(position)));
               } catch (SQLException var9) {
                  var9.printStackTrace();
               }
            } else if (parts.length == 3 && parts[0].equals("top") && parts[1].equals("name")) {
               position = Integer.parseInt(parts[2]);

               try {
                  return Database.database.getPlayerByOrder(position);
               } catch (SQLException var8) {
                  var8.printStackTrace();
               }
            }
         }

         return null;
      }
   }
}
