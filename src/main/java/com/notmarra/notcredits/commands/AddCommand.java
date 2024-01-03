package com.notmarra.notcredits.commands;

import com.notmarra.notcredits.data.Database;
import com.notmarra.notcredits.utilities.Decimal;
import com.notmarra.notcredits.utilities.Messages;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AddCommand {
    public static void execute(CommandSender sender, String [] args) {
        String playerName;
        Player player;
        double amount;
        double credits;
        double final_credits;


        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (!p.hasPermission("notcredits.add") || !p.hasPermission("notcredits.*")) {
                p.sendMessage(Messages.mm(Messages.messageGetString("no-perm")));
            } else {
                if (args.length == 3) {
                    playerName = args[1];
                    player = Bukkit.getPlayer(playerName);
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
                        credits = Database.database.getCreditsByUUID(player.getUniqueId().toString());
                        final_credits = credits + amount;
                        Database.getInstance().setCreditsByUUID(player.getUniqueId().toString(), final_credits);

                        p.sendMessage(Messages.mm(Messages.messageReplaceMultiple(Messages.messageGetString("add_credits"), new String[]{"%amount%", "%player%"}, new String[]{Decimal.formatBalance(final_credits), playerName})));
                        player.sendMessage(Messages.mm(Messages.messageReplace(Messages.messageGetString("credits_add"), "%amount%", Decimal.formatBalance(amount))));
                    } else {
                        p.sendMessage(Messages.mm(Messages.messageGetString("player_not_found")));
                    }
                } else {
                    p.sendMessage(Messages.mm(Messages.messageGetString("invalid_use_add")));
                }
            }
        } else {
            if (args.length == 3) {
                playerName = args[1];
                player = Bukkit.getPlayer(playerName);
                if (player != null) {
                    try {
                        amount = Double.parseDouble(args[2]);
                    } catch (NumberFormatException e) {
                        org.bukkit.Bukkit.getServer().getLogger().info("[NotCredits] Amount must be a number.");
                        return;
                    }
                    if (amount < 0) {
                        Bukkit.getServer().getLogger().info("[NotCredits] Amount must be a positive number.");
                        return;
                    }
                    credits = Database.database.getCreditsByUUID(player.getUniqueId().toString());
                    final_credits = credits + amount;
                    Database.getInstance().setCreditsByUUID(player.getUniqueId().toString(), final_credits);

                    Bukkit.getServer().getLogger().info("[NotCredits] Amount added to player.");
                    player.sendMessage(Messages.mm(Messages.messageReplace(Messages.messageGetString("credits_add"), "%amount%", String.valueOf(final_credits))));
                } else {
                    Bukkit.getServer().getLogger().info("[NotCredits] Invalid use of command, use: /credits add <player> <amount>!");
                }
            } else {
                Bukkit.getServer().getLogger().info("[NotCredits] Invalid use of command, use: /credits add <player> <amount>!");
            }
        }
    }
}
