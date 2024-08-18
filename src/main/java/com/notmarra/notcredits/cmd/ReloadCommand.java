package com.notmarra.notcredits.cmd;

import com.notmarra.notcredits.Notcredits;
import com.notmarra.notcredits.util.Message;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ReloadCommand {
    public static void execute(CommandSender sender) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (p.hasPermission("notcredits.reload")) {
                Notcredits.getInstance().reload();
                Message.sendMessage(p, "reload", false, null);
            } else {
                Message.sendMessage(p, "no_perm", false, null);
            }
        } else {
            Notcredits.getInstance().reload();
            Message.sendMessage(null, "reload", true, null);
        }
    }
}
