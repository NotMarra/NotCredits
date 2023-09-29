/* Decompiler 2ms, total 130ms, lines 24 */
package me.notmarra.notcredits.Commands;

import java.util.Iterator;
import java.util.List;
import me.notmarra.notcredits.utility.GetMessage;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class HelpCommand {
   public static void execute(CommandSender sender, GetMessage message) {
      if (sender instanceof Player) {
         Player p = (Player)sender;
         List<String> list = message.getStringList("help");
         Iterator var4 = list.iterator();

         while(var4.hasNext()) {
            String text = (String)var4.next();
            p.sendMessage(text);
         }
      }

   }
}
