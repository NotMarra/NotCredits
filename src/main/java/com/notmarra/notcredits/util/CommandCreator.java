package com.notmarra.notcredits.util;

import com.mojang.brigadier.context.CommandContext;
import com.notmarra.notcredits.cmd.BaseCommand;
import com.notmarra.notcredits.cmd.HelpCommand;
import com.notmarra.notcredits.cmd.ReloadCommand;
import com.notmarra.notcredits.cmd.UniCommand;
import io.papermc.paper.command.brigadier.BasicCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class CommandCreator implements BasicCommand {
    @Override

    public void execute(CommandSourceStack stack, String[] args) {
        if (args.length == 0) {
            BaseCommand.execute(stack.getSender());
        }

        if (args.length >= 1) {
            String subCommand = args[0].toLowerCase();
            switch (subCommand) {
                case "reload":
                    ReloadCommand.execute(stack.getSender());
                    break;
                case "add":
                    UniCommand.execute(stack.getSender(), args, "add", "notcredits.add", "credits_add", "add_credits", "invalid_use_add");
                    break;
                case "remove":
                    UniCommand.execute(stack.getSender(), args, "remove", "notcredits.remove", "credits_remove", "remove_credits", "invalid_use_remove");
                    break;
                case "set":
                    UniCommand.execute(stack.getSender(), args, "set", "notcredits.set", "credits_set", "set_credits", "invalid_use_set");
                    break;
                case "help":
                    HelpCommand.execute(stack.getSender());
                    break;
                default:
                    BaseCommand.execute(stack.getSender());
                    break;
            }
        }
    }
}
