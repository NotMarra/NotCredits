package com.notmarra.notcredits.cmd;

import com.notmarra.notcredits.NotCredits;
import com.notmarra.notcredits.util.DatabaseManager;
import com.notmarra.notcredits.util.Numbers;
import com.notmarra.notcredits.util.Message;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class UniCommand {
    public static void execute(CommandSender sender, String[] args, String operator, String permission, String message_player, String message_admin, String message_invalid) {
        boolean isConsole = !(sender instanceof Player);
        Player player = isConsole ? null : (Player) sender;

        if (!isConsole && !player.hasPermission(permission)) {
            Message.sendMessage(player, "no_perm", isConsole, null);
            return;
        }

        if (args.length != 3) {
            Message.sendMessage(player, message_invalid, isConsole, null);
            return;
        }

        Player targetPlayer = Bukkit.getPlayer(args[1]);
        if (targetPlayer == null) {
            Message.sendMessage(player, "player_not_found", isConsole, null);
            return;
        }

        Bukkit.getScheduler().runTaskAsynchronously(NotCredits.getInstance(), () -> {
            double amount;
            try {
                amount = Double.parseDouble(args[2]);
            } catch (NumberFormatException e) {
                Message.sendMessage(player, "not_number", isConsole, null);
                return;
            }

            if (amount < 0) {
                Message.sendMessage(player, "not_positive_number", isConsole, null);
                return;
            }

            double credits = DatabaseManager.getInstance(NotCredits.getInstance()).getBalance(targetPlayer.getUniqueId().toString());
            double actualAmount = amount;
            switch (operator) {
                case "add":
                    credits += amount;
                    break;
                case "remove":
                    double diff = credits - amount;
                    if (diff < 0) {
                        actualAmount = credits;
                        credits = 0;
                    } else {
                        credits -= amount;
                    }
                    break;
                case "set":
                    credits = amount;
                    break;
                default:
                    Message.sendMessage(player, "invalid_operator", isConsole, null);
                    return;
            }

            DatabaseManager.getInstance(NotCredits.getInstance()).setBalance(targetPlayer.getUniqueId().toString(), credits);

            Map<String, String> replacements = new HashMap<>();
            replacements.put("amount", Numbers.formatBalance(actualAmount));
            replacements.put("player", targetPlayer.getName());
            Message.sendMessage(player, message_admin, isConsole, replacements);
            replacements.remove("player");
            Message.sendMessage(targetPlayer, message_player, false, replacements);
        });
    }
}
