package me.notmarra.notcredits.commands;

import me.notmarra.notcredits.utilities.Messages;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class HelpCommand {
    public static void execute(CommandSender sender) {
        if (sender instanceof Player) {
            Player p = (Player)sender;
            List<String> list = Collections.singletonList(Messages.getStringList("help"));
            Iterator string = list.iterator();

            while(string.hasNext()) {
                String text = (String)string.next();
                p.sendMessage(Messages.mm(text));
            }
        }

    }
}
