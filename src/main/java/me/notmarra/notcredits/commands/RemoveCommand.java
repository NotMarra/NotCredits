package me.notmarra.notcredits.commands;

import me.notmarra.notcredits.data.Database;
import me.notmarra.notcredits.utilities.Decimal;
import me.notmarra.notcredits.utilities.Messages;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RemoveCommand {
    public static void execute(CommandSender sender, String[] args) {
        String playerName;
        Player player;
        double amount;
        double credits;
        double final_credits;
        if (sender instanceof Player) {
            Player p = (Player)sender;
            if (!p.hasPermission("notcredits.remove") || !p.hasPermission("notcredits.*")) {
                p.sendMessage(Messages.mm(Messages.messageGetString("no-perm")));
            } else {
                if (args.length == 3) {
                    playerName = args[1];
                    player = org.bukkit.Bukkit.getPlayer(playerName);
                    if (player != null) {
                        try {
                            amount = Double.parseDouble(args[2]);
                        } catch (NumberFormatException e) {
                            p.sendMessage(Messages.mm(Messages.messageGetString("not_number")));
                            return;
                        }
                        if (amount < 0) {
                            p.sendMessage(Messages.mm(Messages.messageGetString("not_positive_number")));
                            return;
                        }

                        credits = Database.getInstance().getCreditsByUUID(player.getUniqueId().toString());
                        final_credits = credits - amount;
                        Database.getInstance().setCreditsByUUID(player.getUniqueId().toString(), final_credits);

                        p.sendMessage(Messages.mm(Messages.messageReplaceMultiple(Messages.messageGetString("remove_credits"), new String[]{"%amount%", "%player%"}, new String[]{Decimal.formatBalance(final_credits), playerName})));
                        player.sendMessage(Messages.mm(Messages.messageReplace(Messages.messageGetString("credits_remove"), "%amount%", Decimal.formatBalance(final_credits))));
                    } else {
                        p.sendMessage(Messages.mm(Messages.messageGetString("invalid_use_remove")));
                    }
                } else {
                    p.sendMessage(Messages.mm(Messages.messageGetString("invalid_use_remove")));
                }
            }
        } else {
            if (args.length == 3) {
                playerName = args[1];
                player = org.bukkit.Bukkit.getPlayer(playerName);
                if (player != null) {
                    try {
                        amount = Double.parseDouble(args[2]);
                    } catch (NumberFormatException e) {
                        org.bukkit.Bukkit.getServer().getLogger().info("[NotCredits] Amount must be a number.");
                        return;
                    }
                    if (amount < 0) {
                        org.bukkit.Bukkit.getServer().getLogger().info("[NotCredits] Amount must be a positive number.");
                        return;
                    }

                    credits = Database.getInstance().getCreditsByUUID(player.getUniqueId().toString());
                    final_credits = credits - amount;
                    Database.getInstance().setCreditsByUUID(player.getUniqueId().toString(), final_credits);

                    org.bukkit.Bukkit.getServer().getLogger().info("[NotCredits] Amount removed from player.");
                } else {
                    org.bukkit.Bukkit.getServer().getLogger().info("[NotCredits] Invalid use of command.");
                }
            } else {
                org.bukkit.Bukkit.getServer().getLogger().info("[NotCredits] Invalid use of command.");
            }
        }
    }
}
