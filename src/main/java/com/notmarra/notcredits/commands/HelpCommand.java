package com.notmarra.notcredits.commands;

import com.notmarra.notcredits.utilities.Messages;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class HelpCommand {
    public static void execute(CommandSender sender) {
        if (sender instanceof Player) {
            Player p = (Player)sender;
            List<String> list = Messages.messageGetStringList("help");

            for (String line : list) {
                p.sendMessage(Messages.mm(line));
            }
        }

    }
}
