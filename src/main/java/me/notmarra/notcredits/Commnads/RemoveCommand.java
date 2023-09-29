/* Decompiler 10ms, total 129ms, lines 103 */
package me.notmarra.notcredits.Commands;

import java.sql.SQLException;
import java.util.logging.Logger;
import me.notmarra.notcredits.Data.Database;
import me.notmarra.notcredits.utility.GetMessage;
import me.notmarra.notcredits.utility.TextReplace;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RemoveCommand {
   public static void execute(CommandSender sender, String[] args, GetMessage message) {
      String playerName;
      Player player;
      double amount;
      double credits;
      double final_credits;
      String var10001;
      if (sender instanceof Player) {
         Player p = (Player)sender;
         if (!p.hasPermission("credits.admin.remove")) {
            var10001 = message.getString("prefix");
            p.sendMessage(var10001 + message.getString("no_perm"));
         }

         if (args.length == 3 && p.hasPermission("credits.admin.remove")) {
            playerName = args[1];
            player = Bukkit.getPlayer(playerName);
            if (player != null) {
               amount = 0.0D;

               try {
                  amount = Double.parseDouble(args[2]);
               } catch (NumberFormatException var15) {
                  var10001 = message.getString("prefix");
                  p.sendMessage(var10001 + message.getString("not_number"));
               }

               if (amount < 0.0D) {
                  var10001 = message.getString("prefix");
                  sender.sendMessage(var10001 + message.getString("not_positive_number"));
               }

               try {
                  credits = Database.database.getCreditsByUUID(player.getUniqueId().toString());
                  final_credits = credits - amount;
                  Database.database.setCreditsByUUID(player.getUniqueId().toString(), final_credits);
                  var10001 = message.getString("prefix");
                  p.sendMessage(var10001 + TextReplace.replacePlayerAndCredits(player, amount, message.getString("remove_credits")));
                  var10001 = message.getString("prefix");
                  player.sendMessage(var10001 + TextReplace.replaceAmount(amount, message.getString("credits_remove")));
               } catch (SQLException var14) {
                  var14.printStackTrace();
               }
            } else {
               var10001 = message.getString("prefix");
               p.sendMessage(var10001 + message.getString("player_not_found"));
            }
         }

         if (args.length != 3 && p.hasPermission("credits.admin.remove")) {
            var10001 = message.getString("prefix");
            p.sendMessage(var10001 + message.getString("invalid_use_remove"));
         }
      } else if (args.length == 3) {
         playerName = args[1];
         player = Bukkit.getPlayer(playerName);
         if (player != null) {
            amount = 0.0D;

            try {
               amount = Double.parseDouble(args[2]);
            } catch (NumberFormatException var13) {
               Bukkit.getServer().getLogger().info("[NotCredits] " + message.getString("not_number"));
            }

            if (amount < 0.0D) {
               Bukkit.getServer().getLogger().info("[NotCredits] " + message.getString("not_positive_number"));
            }

            try {
               credits = Database.database.getCreditsByUUID(player.getUniqueId().toString());
               final_credits = credits - amount;
               Database.database.setCreditsByUUID(player.getUniqueId().toString(), final_credits);
               Logger var10000 = Bukkit.getServer().getLogger();
               var10001 = TextReplace.replacePlayerAndCredits(player, amount, message.getString("remove_credits"));
               var10000.info("[NotCredits] " + var10001);
               var10001 = message.getString("prefix");
               player.sendMessage(var10001 + TextReplace.replaceAmount(amount, message.getString("remove_credits")));
            } catch (SQLException var12) {
               var12.printStackTrace();
            }
         } else {
            Bukkit.getServer().getLogger().info("[NotCredits] " + message.getString("player_not_found"));
         }
      } else {
         Bukkit.getServer().getLogger().info("[NotCredits] " + message.getString("invalid_use_remove"));
      }

   }
}
