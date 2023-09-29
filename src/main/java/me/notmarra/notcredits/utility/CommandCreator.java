/* Decompiler 5ms, total 128ms, lines 79 */
package me.notmarra.notcredits.utility;

import me.notmarra.notcredits.Notcredits;
import me.notmarra.notcredits.Commands.AddCommand;
import me.notmarra.notcredits.Commands.BaseCommand;
import me.notmarra.notcredits.Commands.HelpCommand;
import me.notmarra.notcredits.Commands.ReloadCommand;
import me.notmarra.notcredits.Commands.RemoveCommand;
import me.notmarra.notcredits.Commands.SetCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CommandCreator implements CommandExecutor {
   private final GetMessage message = new GetMessage();
   private final Notcredits plugin;

   public CommandCreator(Notcredits plugin) {
      this.plugin = plugin;
   }

   public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
      if (args.length == 0) {
         BaseCommand.execute(sender, this.message);
      }

      if (args.length >= 1) {
         String var5 = args[0].toLowerCase();
         byte var6 = -1;
         switch(var5.hashCode()) {
         case -934641255:
            if (var5.equals("reload")) {
               var6 = 0;
            }
            break;
         case -934610812:
            if (var5.equals("remove")) {
               var6 = 3;
            }
            break;
         case 96417:
            if (var5.equals("add")) {
               var6 = 2;
            }
            break;
         case 113762:
            if (var5.equals("set")) {
               var6 = 1;
            }
            break;
         case 3198785:
            if (var5.equals("help")) {
               var6 = 4;
            }
         }

         switch(var6) {
         case 0:
            ReloadCommand.execute(sender, args, this.message, this.plugin);
            break;
         case 1:
            SetCommand.execute(sender, args, this.message);
            break;
         case 2:
            AddCommand.execute(sender, args, this.message);
            break;
         case 3:
            RemoveCommand.execute(sender, args, this.message);
            break;
         case 4:
         default:
            HelpCommand.execute(sender, this.message);
         }
      }

      return true;
   }
}
