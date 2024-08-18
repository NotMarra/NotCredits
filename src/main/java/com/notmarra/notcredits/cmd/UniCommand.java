package com.notmarra.notcredits.cmd;

import com.notmarra.notcredits.Notcredits;
import com.notmarra.notcredits.util.DatabaseManager;
import com.notmarra.notcredits.util.Decimal;
import com.notmarra.notcredits.util.Message;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class UniCommand {
    public static void execute(CommandSender sender, String [] args, String operator, String permission, String message_player, String message_admin, String message_invalid) {
        Boolean isConsole;
        Player p;
        if (sender instanceof Player) {
            p = (Player) sender;
            isConsole = false;
        } else {
            p = null;
            isConsole = true;
        }

        if (!p.hasPermission(permission) && !isConsole) {
            Message.sendMessage(p, "no_perm", false, null);
        } else {
            if (args.length == 3) {
                Player player = Bukkit.getPlayer(args[1]);

                Bukkit.getScheduler().runTaskAsynchronously(Notcredits.getInstance(), () -> {
                    double amount;
                    if (player != null) {
                        try {
                            amount = Double.parseDouble(args[2]);
                        } catch (NumberFormatException e) {
                            Message.sendMessage(p, "not_number", isConsole, null);
                            return;
                        }
                        if (amount < 0) {
                            Message.sendMessage(p, "not_positive_number", isConsole, null);
                            return;
                        }
                        double credits = DatabaseManager.getInstance(Notcredits.getInstance()).getBalance(player.getUniqueId().toString());

                        switch (operator) {
                            case "add":
                                credits += amount;
                                break;
                            case "remove":
                                credits -= amount;
                                break;
                            case "set":
                                credits = amount;
                                break;
                            default:
                                Message.sendMessage(p, "invalid_operator", isConsole, null);
                                break;
                        }

                        DatabaseManager.getInstance(Notcredits.getInstance()).setBalance(player.getUniqueId().toString(), credits);

                        Map<String, String> replacements = new HashMap<>();
                        replacements.put("amount", Decimal.formatBalance(amount));
                        replacements.put("player", player.getName());
                        Message.sendMessage(p, message_admin, isConsole, replacements);
                        replacements.remove("player");
                        Message.sendMessage(player, message_player, isConsole, replacements);
                    } else {
                        Message.sendMessage(p, "player_not_found", isConsole, null);
                    }
                });
            } else {
                Message.sendMessage(p, message_invalid, isConsole, null);
            }
        }
    }
}
