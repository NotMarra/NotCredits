/* Decompiler 2ms, total 131ms, lines 29 */
package me.notmarra.notcredits.Commands;

import me.notmarra.notcredits.Notcredits;
import me.notmarra.notcredits.utility.GetMessage;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ReloadCommand {
   public static void execute(CommandSender sender, String[] args, GetMessage message, Notcredits plugin) {
      if (sender instanceof Player) {
         Player p = (Player)sender;
         String var10001;
         if (!p.hasPermission("credits.admin.reload")) {
            var10001 = message.getString("prefix");
            p.sendMessage(var10001 + message.getString("no_perm"));
         } else {
            plugin.reload();
            var10001 = message.getString("prefix");
            p.sendMessage(var10001 + message.getString("reload"));
         }
      } else {
         plugin.reload();
         Bukkit.getServer().getLogger().info("[NotCredits] " + message.getString("reload"));
      }

   }
}
