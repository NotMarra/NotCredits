package com.notmarra.notcredits.utilities;

import com.notmarra.notcredits.commands.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CommandCreator implements CommandExecutor {
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            BaseCommand.execute(sender);
        }

        if (args.length >= 1) {
            String subCommand = args[0].toLowerCase();
            switch (subCommand) {
                case "reload":
                    ReloadCommand.execute(sender);
                    break;
                case "add":
                    AddCommand.execute(sender, args);
                    break;
                case "remove":
                    RemoveCommand.execute(sender, args);
                    break;
                case "set":
                    SetCommand.execute(sender, args);
                    break;
                case "help":
                    HelpCommand.execute(sender);
                    break;
                default:
                    BaseCommand.execute(sender);
                    break;
            }
        }
        return true;
    }
}
