package com.notmarra.notcredits.commands;

import com.notmarra.notcredits.Notcredits;
import com.notmarra.notcredits.data.Database;
import com.notmarra.notcredits.utilities.Decimal;
import com.notmarra.notcredits.utilities.GetMessage;
import com.notmarra.notcredits.utilities.Messages;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class UniCommand {
    public static void execute(CommandSender sender, String [] args, String operator, String permission, String message_player, String message_admin, String message_invalid) {
        String playerName;
        Player player;

        if (sender instanceof Player p) {
            if (!p.hasPermission(permission)) {
                p.sendMessage(Messages.mm(Messages.messageGetString("no-perm")));
            } else {
                if (args.length == 3) {
                    playerName = args[1];
                    player = Bukkit.getPlayer(playerName);
                    final String final_playerName = playerName;

                    Bukkit.getScheduler().runTaskAsynchronously(Notcredits.getInstance(), new Runnable() {
                        @Override
                        public void run() {
                            double final_credits;
                            double credits;
                            double amount;

                            if (final_playerName != null) {
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

                                switch(operator) {
                                    case "add":
                                        credits = Database.database.getCreditsByUUID(player.getUniqueId().toString());
                                        final_credits = amount + credits;
                                        Database.getInstance().setCreditsByUUID(player.getUniqueId().toString(), final_credits);
                                        break;
                                    case "remove":
                                        credits = Database.database.getCreditsByUUID(player.getUniqueId().toString());
                                        final_credits = credits - amount;
                                        Database.getInstance().setCreditsByUUID(player.getUniqueId().toString(), final_credits);
                                        break;
                                    case "set":
                                        Database.getInstance().setCreditsByUUID(player.getUniqueId().toString(), amount);
                                        break;
                                    default:
                                        credits = Database.database.getCreditsByUUID(player.getUniqueId().toString());
                                        final_credits = amount + credits;
                                        Database.getInstance().setCreditsByUUID(player.getUniqueId().toString(), final_credits);
                                        break;
                                }

                                p.sendMessage(Messages.mm(Messages.messageReplaceMultiple(Messages.messageGetString(message_admin), new String[]{"%amount%", "%player%"}, new String[]{Decimal.formatBalance(amount), playerName})));
                                player.sendMessage(Messages.mm(Messages.messageReplace(Messages.messageGetString(message_player), "%amount%", Decimal.formatBalance(amount))));
                            } else {
                                p.sendMessage(Messages.mm(Messages.messageGetString("player_not_found")));
                            }
                        }
                    });
                } else {
                    p.sendMessage(Messages.mm(Messages.messageGetString(message_invalid)));
                }
            }
        } else {
            if (args.length == 3) {
                playerName = args[1];
                player = Bukkit.getPlayer(playerName);
                final String final_playerName = playerName;

                Bukkit.getScheduler().runTaskAsynchronously(Notcredits.getInstance(), new Runnable() {
                    @Override
                    public void run() {
                        double final_credits;
                        double credits;
                        double amount;

                        if (final_playerName != null) {
                            try {
                                amount = Double.parseDouble(args[2]);
                            } catch (NumberFormatException e) {
                                Notcredits.getInstance().getLogger().info(GetMessage.getMessageConsole("not_number"));
                                return;
                            }
                            if (amount < 0) {
                                Notcredits.getInstance().getLogger().info(GetMessage.getMessageConsole("not_positive_number"));
                                return;
                            }

                            switch(operator) {
                                case "add":
                                    credits = Database.database.getCreditsByUUID(player.getUniqueId().toString());
                                    final_credits = amount + credits;
                                    Database.getInstance().setCreditsByUUID(player.getUniqueId().toString(), final_credits);
                                    break;
                                case "remove":
                                    credits = Database.database.getCreditsByUUID(player.getUniqueId().toString());
                                    final_credits = credits - amount;
                                    Database.getInstance().setCreditsByUUID(player.getUniqueId().toString(), final_credits);
                                    break;
                                case "set":
                                    Database.getInstance().setCreditsByUUID(player.getUniqueId().toString(), amount);
                                    break;
                                default:
                                    credits = Database.database.getCreditsByUUID(player.getUniqueId().toString());
                                    final_credits = amount + credits;
                                    Database.getInstance().setCreditsByUUID(player.getUniqueId().toString(), final_credits);
                                    break;
                            }

                            Notcredits.getInstance().getLogger().info(Messages.messageReplaceMultiple(GetMessage.getMessageConsole(message_admin), new String[]{"%amount%", "%player%"}, new String[]{Decimal.formatBalance(amount), playerName}));
                            player.sendMessage(Messages.mm(Messages.messageReplace(Messages.messageGetString(message_player), "%amount%", Decimal.formatBalance(amount))));
                        } else {
                            Notcredits.getInstance().getLogger().info(GetMessage.getMessageConsole("player_not_found"));
                        }
                    }
                });
            } else {
                Notcredits.getInstance().getLogger().info(GetMessage.getMessageConsole(message_invalid));
            }
        }

    }
}
