package me.notmarra.notcredits.commands;

import me.notmarra.notcredits.Notcredits;
import me.notmarra.notcredits.utilities.Messages;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ReloadCommand {
    public static void execute(CommandSender sender) {
        if (sender instanceof Player) {
            Player p = (Player)sender;
            if (!p.hasPermission("notcredits.reload") || !p.hasPermission("notcredits.*")) {
                p.sendMessage(Messages.mm(Messages.getString("no-perm")));
            } else {
                Notcredits.getInstance().reload();
                p.sendMessage(Messages.mm(Messages.getString("reload")));
            }
        } else {
            Notcredits.getInstance().reload();
            Bukkit.getServer().getLogger().info("[NotCredits] Plugin has been successfully reloaded!");
        }

    }
}