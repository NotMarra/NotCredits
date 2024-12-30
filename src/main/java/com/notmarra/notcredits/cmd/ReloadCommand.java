package com.notmarra.notcredits.cmd;

import com.notmarra.notcredits.NotCredits;
import com.notmarra.notcredits.util.Message;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ReloadCommand {
    public static void execute(CommandSender sender) {
        if (sender instanceof Player p) {
            if (p.hasPermission("notcredits.reload")) {
                NotCredits.getInstance().reload();
                Message.sendMessage(p, "reload", false, null);
            } else {
                Message.sendMessage(p, "no_perm", false, null);
            }
        } else {
            NotCredits.getInstance().reload();
            Message.sendMessage(null, "reload", true, null);
        }
    }
}
