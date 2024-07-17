package com.notmarra.notcredits.cmd;

import com.notmarra.notcredits.util.Message;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class HelpCommand {
    public static void execute(CommandSender sender) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            List<String> help = Message.getMessageList("help");

            for (String line : help) {
                Message.sendMessage(p, line, false, null);
            }
        } else {
            List<String> help = Message.getMessageList("help");

            for (String line : help) {
                Message.sendMessage(null, line, true, null);
            }
        }
    }
}
