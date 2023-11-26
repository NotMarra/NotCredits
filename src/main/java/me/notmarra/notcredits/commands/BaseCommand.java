package me.notmarra.notcredits.commands;

import me.notmarra.notcredits.data.Database;
import me.notmarra.notcredits.utilities.Decimal;
import me.notmarra.notcredits.utilities.Messages;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BaseCommand {
    public static void execute(CommandSender sender) {
        if (sender instanceof Player) {
            Player p = (Player)sender;

            double credits = Database.database.getCreditsByUUID(String.valueOf(p.getUniqueId()));
            p.sendMessage(Messages.mm(Messages.messageReplace(Messages.messageGetString("credits"), "%credits%", Decimal.formatBalance(credits))));
        }
    }
}
