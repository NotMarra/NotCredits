package com.notmarra.notcredits.util;

import com.notmarra.notcredits.cmd.BaseCommand;
import com.notmarra.notcredits.cmd.HelpCommand;
import com.notmarra.notcredits.cmd.ReloadCommand;
import com.notmarra.notcredits.cmd.UniCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CommandCreator implements CommandExecutor {
    @Override

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            BaseCommand.execute(sender, null);
        }

        if (args.length >= 1) {
            String subCommand = args[0].toLowerCase();
            switch (subCommand) {
                case "reload":
                    ReloadCommand.execute(sender);
                    break;
                case "add":
                    UniCommand.execute(sender, args, "add", "notcredits.add", "credits_add", "add_credits", "invalid_use_add");
                    break;
                case "remove":
                    UniCommand.execute(sender, args, "remove", "notcredits.remove", "credits_remove", "remove_credits", "invalid_use_remove");
                    break;
                case "set":
                    UniCommand.execute(sender, args, "set", "notcredits.set", "credits_set", "set_credits", "invalid_use_set");
                    break;
                case "help":
                    HelpCommand.execute(sender);
                    break;
                default:
                    BaseCommand.execute(sender, args[0]);
                    break;
            }
        }
        return true;
    }

}
