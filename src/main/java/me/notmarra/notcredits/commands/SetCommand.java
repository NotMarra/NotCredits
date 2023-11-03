package me.notmarra.notcredits.commands;

import me.notmarra.notcredits.data.Database;
import me.notmarra.notcredits.utilities.Messages;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetCommand {
    public static void execute(CommandSender sender, String[] args) {
        String playerName;
        Player player;
        double amount;
        if (sender instanceof Player) {
            Player p = (Player)sender;
            if (!p.hasPermission("notcredits.set") || !p.hasPermission("notcredits.*")) {
                if (args.length == 3) {
                    playerName = args[1];
                    player = org.bukkit.Bukkit.getPlayer(playerName);
                    if (player != null) {
                        try {
                            amount = Double.parseDouble(args[2]);
                        } catch (NumberFormatException e) {
                            p.sendMessage(Messages.mm(Messages.getString("not_number")));
                            return;
                        }
                        if (amount < 0) {
                            p.sendMessage(Messages.mm(Messages.getString("not_positive_number")));
                            return;
                        }

                        Database.getInstance().setCreditsByUUID(player.getUniqueId().toString(), amount);

                        p.sendMessage(Messages.mm(Messages.replaceMultiple(Messages.getString("set_credits"), new String[]{"%amount%", "%player%"}, new String[]{String.valueOf(amount), String.valueOf(player)})));
                        player.sendMessage(Messages.mm(Messages.replace(Messages.getString("credits_set"), "%amount%", String.valueOf(amount))));
                    } else {
                        p.sendMessage(Messages.mm(Messages.getString("invalid_use_set")));
                    }
                } else {
                    p.sendMessage(Messages.mm(Messages.getString("invalid_use_set")));
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

                    Database.getInstance().setCreditsByUUID(player.getUniqueId().toString(), amount);

                    org.bukkit.Bukkit.getServer().getLogger().info("[NotCredits] Amount set for player.");
                } else {
                    org.bukkit.Bukkit.getServer().getLogger().info("[NotCredits] Invalid use of command.");
                }
            } else {
                org.bukkit.Bukkit.getServer().getLogger().info("[NotCredits] Invalid use of command.");
            }
        }
    }
}
