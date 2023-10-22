/* Decompiler 9ms, total 136ms, lines 100 */
package me.notmarra.notcredits.Commands;

import java.sql.SQLException;
import java.util.logging.Logger;

import me.notmarra.notcredits.utility.GetMessage;
import me.notmarra.notcredits.utility.TextReplace;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetCommand {
   public static void execute(CommandSender sender, String[] args, GetMessage message) {
      String var10001;
      String playerName;
      Player player;
      double amount;
      if (sender instanceof Player) {
         Player p = (Player)sender;
         if (!p.hasPermission("credits.admin.set")) {
            if (args.length == 3) {
               playerName = args[1];
               player = Bukkit.getPlayer(playerName);
               if (player != null) {
                  amount = 0.0D;

                  try {
                     amount = Double.parseDouble(args[2]);
                  } catch (NumberFormatException var10) {
                     Bukkit.getServer().getLogger().info("[NotCredits] " + message.getString("not_number"));
                  }

                  if (amount < 0.0D) {
                     Bukkit.getServer().getLogger().info("[NotCredits] " + message.getString("not_positive_number"));
                  }

                  try {
                     Database.database.setCreditsByUUID(player.getUniqueId().toString(), amount);
                     Logger var10000 = Bukkit.getServer().getLogger();
                     var10001 = TextReplace.replacePlayerAndCredits(player, amount, message.getString("set_credits"));
                     var10000.info("[NotCredits] " + var10001);
                     var10001 = message.getString("prefix");
                     player.sendMessage(var10001 + TextReplace.replaceAmount(amount, message.getString("credits_set")));
                  } catch (SQLException var9) {
                     var9.printStackTrace();
                  }

                  return;
               } else {
                  Bukkit.getServer().getLogger().info("[NotCredits] " + message.getString("player_not_found"));
                  return;
               }
            } else {
               Bukkit.getServer().getLogger().info("[NotCredits] " + message.getString("invalid_use_set"));
               return;
            }
         }
      }

      if (args.length == 3) {
         playerName = args[1];
         player = Bukkit.getPlayer(playerName);
         if (player != null) {
            amount = 0.0D;

            try {
               amount = Double.parseDouble(args[2]);
            } catch (NumberFormatException var12) {
               var10001 = message.getString("prefix");
               sender.sendMessage(var10001 + message.getString("not_number"));
               return;
            }

            if (amount < 0.0D) {
               var10001 = message.getString("prefix");
               sender.sendMessage(var10001 + message.getString("not_positive_number"));
               return;
            }

            try {
               Database.database.setCreditsByUUID(player.getUniqueId().toString(), amount);
               var10001 = message.getString("prefix");
               sender.sendMessage(var10001 + TextReplace.replacePlayerAndCredits(player, amount, message.getString("set_credits")));
               var10001 = message.getString("prefix");
               player.sendMessage(var10001 + TextReplace.replaceAmount(amount, message.getString("credits_set")));
            } catch (SQLException var11) {
               var11.printStackTrace();
            }
         } else {
            var10001 = message.getString("prefix");
            sender.sendMessage(var10001 + message.getString("player_not_found"));
         }
      } else {
         var10001 = message.getString("prefix");
         sender.sendMessage(var10001 + message.getString("no_perm"));
      }

   }
}
