/* Decompiler 3ms, total 138ms, lines 30 */
package me.notmarra.notcredits.utility;

import java.sql.SQLException;
import java.text.DecimalFormat;
import me.notmarra.notcredits.Notcredits;
import org.bukkit.entity.Player;

public class TextReplace {
   public static String replaceCredits(Player player, String text) throws SQLException {
      double credits = Database.database.getCreditsByUUID(player.getUniqueId().toString());
      if (Notcredits.main.getConfig().getBoolean("balance_decimal")) {
         DecimalFormat decimalFormat = new DecimalFormat(Notcredits.main.getConfig().getString("balance_format"));
         return text.replace("%credits%", String.valueOf(decimalFormat.format(credits)));
      } else {
         return text.replace("%credits%", String.valueOf(Math.round(credits)));
      }
   }

   public static String replacePlayerAndCredits(Player player, double amount, String message) {
      String text = message.replace("%player%", player.getName()).replace("%amount%", String.valueOf(amount));
      return text;
   }

   public static String replaceAmount(double amount, String message) {
      String text = message.replace("%amount%", String.valueOf(amount));
      return text;
   }
}
