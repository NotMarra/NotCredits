/* Decompiler 2ms, total 137ms, lines 24 */
package me.notmarra.notcredits.Commands;

import java.sql.SQLException;
import me.notmarra.notcredits.utility.GetMessage;
import me.notmarra.notcredits.utility.TextReplace;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BaseCommand {
   public static void execute(CommandSender sender, GetMessage message) {
      if (sender instanceof Player) {
         Player p = (Player)sender;

         try {
            String var10001 = message.getString("prefix");
            p.sendMessage(var10001 + TextReplace.replaceCredits(p, message.getString("credits")));
         } catch (SQLException var4) {
            var4.printStackTrace();
         }
      }

   }
}
