package com.notmarra.notcredits.commands;

import com.notmarra.notcredits.Notcredits;
import com.notmarra.notcredits.data.Database;
import com.notmarra.notcredits.utilities.Decimal;
import com.notmarra.notcredits.utilities.Messages;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BaseCommand {
    public static void execute(CommandSender sender) {
        if (sender instanceof Player) {
            Player p = (Player)sender;
            Bukkit.getScheduler().runTaskAsynchronously(Notcredits.getInstance(), new Runnable() {
                @Override
                public void run() {
                    double credits = Database.database.getCreditsByUUID(String.valueOf(p.getUniqueId()));
                    p.sendMessage(Messages.mm(Messages.messageReplace(Messages.messageGetString("credits"), "%credits%", Decimal.formatBalance(credits))));
                }
            });
        } else {
            Notcredits.getInstance().getLogger().info("This command can only be run by a player.");
        }
    }
}
