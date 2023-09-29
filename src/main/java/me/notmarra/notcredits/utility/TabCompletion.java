/* Decompiler 11ms, total 131ms, lines 91 */
package me.notmarra.notcredits.utility;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

public class TabCompletion implements TabCompleter {
   public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
      List<String> completions = new ArrayList();
      String currentArg;
      if (args.length == 1) {
         completions.add("add");
         completions.add("remove");
         completions.add("set");
         completions.add("help");
         completions.add("reload");
      } else {
         byte var7;
         if (args.length == 2) {
            currentArg = args[0].toLowerCase();
            var7 = -1;
            switch(currentArg.hashCode()) {
            case -934610812:
               if (currentArg.equals("remove")) {
                  var7 = 1;
               }
               break;
            case 96417:
               if (currentArg.equals("add")) {
                  var7 = 0;
               }
               break;
            case 113762:
               if (currentArg.equals("set")) {
                  var7 = 2;
               }
            }

            switch(var7) {
            case 0:
            case 1:
            case 2:
               Iterator var8 = Bukkit.getOnlinePlayers().iterator();

               while(var8.hasNext()) {
                  Player player = (Player)var8.next();
                  completions.add(player.getName());
               }
            }
         } else if (args.length == 3) {
            currentArg = args[0].toLowerCase();
            var7 = -1;
            switch(currentArg.hashCode()) {
            case -934610812:
               if (currentArg.equals("remove")) {
                  var7 = 2;
               }
               break;
            case 96417:
               if (currentArg.equals("add")) {
                  var7 = 1;
               }
               break;
            case 113762:
               if (currentArg.equals("set")) {
                  var7 = 0;
               }
            }

            switch(var7) {
            case 0:
            case 1:
            case 2:
               completions.add("<amount>");
            }
         }
      }

      currentArg = args[args.length - 1];
      completions.removeIf((s) -> {
         return !s.startsWith(currentArg);
      });
      return completions;
   }
}
