package com.notmarra.notcredits.cmd;

import com.notmarra.notcredits.util.Message;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class HelpCommand {
    public static void execute(CommandSender sender) {
        List<String> help = Message.getMessageList("help");

        if (sender instanceof Player) {
            Player p = (Player) sender;

            for (String line : help) {
                Message.sendRawMessage(p, line, false, null);
            }
        } else {
            for (String line : help) {
                Message.sendRawMessage(null, line, true, null);
            }
        }
    }
}
