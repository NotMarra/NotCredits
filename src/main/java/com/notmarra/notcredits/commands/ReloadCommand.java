package com.notmarra.notcredits.commands;

import com.notmarra.notcredits.Notcredits;
import com.notmarra.notcredits.utilities.Messages;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ReloadCommand {
    public static void execute(CommandSender sender) {
        if (sender instanceof Player) {
            Player p = (Player)sender;
            if (!p.hasPermission("notcredits.reload") || !p.hasPermission("notcredits.*")) {
                p.sendMessage(Messages.mm(Messages.messageGetString("no-perm")));
            } else {
                Notcredits.getInstance().reload();
                p.sendMessage(Messages.mm(Messages.messageGetString("reload")));
            }
        } else {
            Notcredits.getInstance().reload();
            Bukkit.getServer().getLogger().info("[NotCredits] Plugin has been successfully reloaded!");
        }

    }
}
