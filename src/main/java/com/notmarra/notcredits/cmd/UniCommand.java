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

        if (sender instanceof Player ) {
            Player p = (Player) sender;
            if (!p.hasPermission(permission)) {
                Message.sendMessage(p, "no_perm", false, null);
            } else {
                if (args.length == 3) {
                    Player player = Bukkit.getPlayer(args[1]);

                    Bukkit.getScheduler().runTaskAsynchronously(Notcredits.getInstance(), () -> {
                        double credits;
                        double amount;
                        if (player != null) {
                            try {
                                 amount = Double.parseDouble(args[2]);
                            } catch (NumberFormatException e) {
                                Message.sendMessage(p, "not_number", false, null);
                                return;
                            }
                            if (amount < 0) {
                                Message.sendMessage(p, "not_positive_number", false, null);
                                return;
                            }

                            switch (operator) {
                                case "add":
                                    credits = DatabaseManager.getInstance(Notcredits.getInstance()).getBalance(player.getUniqueId().toString());
                                    credits += amount;
                                    DatabaseManager.getInstance(Notcredits.getInstance()).setBalance(player.getUniqueId().toString(), credits);
                                    break;
                                case "remove":
                                    credits = DatabaseManager.getInstance(Notcredits.getInstance()).getBalance(player.getUniqueId().toString());
                                    credits -= amount;
                                    DatabaseManager.getInstance(Notcredits.getInstance()).setBalance(player.getUniqueId().toString(), credits);
                                    break;
                                case "set":
                                    DatabaseManager.getInstance(Notcredits.getInstance()).setBalance(player.getUniqueId().toString(), amount);
                                    break;
                                default:
                                    Message.sendMessage(p, "invalid_operator", false, null);
                                    break;
                            }
                            Map<String, String> replacements = new HashMap<>();
                            replacements.put("amount", Decimal.formatBalance(amount));
                            replacements.put("player", player.getName());
                            Message.sendMessage(p, message_admin, false, replacements);
                            replacements.remove("player");
                            Message.sendMessage(player, message_player, false, replacements);
                        } else {
                            Message.sendMessage(p, "player_not_found", false, null);
                        }
                    });
                } else {
                    Message.sendMessage(p, message_invalid, false, null);
                }
            }
        } else {
            if (args.length == 3) {
                Player player = Bukkit.getPlayer(args[1]);

                Bukkit.getScheduler().runTaskAsynchronously(Notcredits.getInstance(), () -> {
                    double credits;
                    double amount;
                    if (player != null) {
                        try {
                            amount = Double.parseDouble(args[2]);
                        } catch (NumberFormatException e) {
                            Message.sendMessage(null, "not_number", true, null);
                            return;
                        }
                        if (amount < 0) {
                            Message.sendMessage(null, "not_positive_number", true, null);
                            return;
                        }

                        switch (operator) {
                            case "add":
                                credits = DatabaseManager.getInstance(Notcredits.getInstance()).getBalance(player.getUniqueId().toString());
                                credits += amount;
                                DatabaseManager.getInstance(Notcredits.getInstance()).setBalance(player.getUniqueId().toString(), credits);
                                break;
                            case "remove":
                                credits = DatabaseManager.getInstance(Notcredits.getInstance()).getBalance(player.getUniqueId().toString());
                                credits -= amount;
                                DatabaseManager.getInstance(Notcredits.getInstance()).setBalance(player.getUniqueId().toString(), credits);
                                break;
                            case "set":
                                DatabaseManager.getInstance(Notcredits.getInstance()).setBalance(player.getUniqueId().toString(), amount);
                                break;
                            default:
                                Message.sendMessage(null, "invalid_operator", true, null);
                                break;
                        }
                        Map<String, String> replacements = new HashMap<>();
                        replacements.put("amount", Decimal.formatBalance(amount));
                        replacements.put("player", player.getName());
                        Message.sendMessage(null, message_admin, true, replacements);
                        replacements.remove("player");
                        Message.sendMessage(player, message_player, false, replacements);
                    } else {
                        Message.sendMessage(null, "player_not_found", true, null);
                    }
                });
            } else {
                Message.sendMessage(null, message_invalid, true, null);
            }
        }
    }
}
