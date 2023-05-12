package me.notmarra.notcredits.utility;

import me.notmarra.notcredits.Data.Database;
import me.notmarra.notcredits.Notcredits;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.SQLException;
import java.util.List;

public class CommandCreator implements CommandExecutor {

    private final GetMessage message = new GetMessage();
    private final Notcredits plugin;
    private static Database database = null;

    public CommandCreator(Notcredits plugin) {
        this.plugin = plugin;
        try {
            database = Database.getInstance();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (sender instanceof Player p) {
            if (args.length == 0) {
                try {
                    p.sendMessage(message.getString("prefix") + TextReplace.replaceCredits(p, message.getString("credits")));
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (args.length >= 1) {
                if (args[0].equalsIgnoreCase("reload")) {
                    if (!p.hasPermission("credits.admin.reload")) {
                        p.sendMessage(message.getString("prefix") + message.getString("no_perm"));
                    } else {
                        plugin.reload();
                        p.sendMessage(message.getString("prefix") + message.getString("reload"));
                    }
                }
                if (args[0].equalsIgnoreCase("set")) {
                    if (!p.hasPermission("credits.admin.set")) {
                        p.sendMessage(message.getString("prefix") + message.getString("no_perm"));
                    }
                    if (args.length == 3 && p.hasPermission("credits.admin.set")) {
                        String playerName = args[1];
                        Player player = Bukkit.getPlayer(playerName);

                        if (player != null) {
                            long amount = 0;

                            try {
                                amount = Long.parseLong(args[2]);
                            } catch (NumberFormatException e) {
                                p.sendMessage(message.getString("prefix") + message.getString("not_number"));
                            }

                            if (amount < 0) {
                                sender.sendMessage(message.getString("prefix") + message.getString("not_positive_number"));
                            }

                            try {
                                database.setCreditsByUUID(player.getUniqueId().toString(), amount);

                                p.sendMessage(message.getString("prefix") + TextReplace.replacePlayerAndCredits(player, amount, message.getString("set_credits")));
                                player.sendMessage(message.getString("prefix") + TextReplace.replaceAmount(amount, message.getString("credits_set")));
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        } else {
                            p.sendMessage(message.getString("prefix") + message.getString("player_not_found"));
                        }
                    }
                    if (args.length != 3 && p.hasPermission("credits.admin.set")) {
                        p.sendMessage(message.getString("prefix") + message.getString("invalid_use_set"));
                    }
                }

                if (args[0].equalsIgnoreCase("add")) {
                    if (!p.hasPermission("credits.admin.add")) {
                        p.sendMessage(message.getString("prefix") + message.getString("no_perm"));
                    }

                    if (args.length == 3 && p.hasPermission("credits.admin.add")) {
                        String playerName = args[1];
                        Player player = Bukkit.getPlayer(playerName);

                        if (player != null) {
                            long amount = 0;

                            try {
                                amount = Long.parseLong(args[2]);
                            } catch (NumberFormatException e) {
                                p.sendMessage(message.getString("prefix") + message.getString("not_number"));
                            }

                            if (amount < 0) {
                                sender.sendMessage(message.getString("prefix") + message.getString("not_positive_number"));
                            }

                            try {
                                long credits = database.getCreditsByUUID(player.getUniqueId().toString());
                                long final_credits = credits + amount;

                                database.setCreditsByUUID(player.getUniqueId().toString(), final_credits);

                                p.sendMessage(message.getString("prefix") + TextReplace.replacePlayerAndCredits(player, amount, message.getString("add_credits")));
                                player.sendMessage(message.getString("prefix") + TextReplace.replaceAmount(amount, message.getString("credits_add")));

                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        } else {
                            p.sendMessage(message.getString("prefix") + message.getString("player_not_found"));
                        }
                    }
                    if (args.length != 3 && p.hasPermission("credits.admin.add")) {
                        p.sendMessage(message.getString("prefix") + message.getString("invalid_use_add"));
                    }
                }


                if (args[0].equalsIgnoreCase("remove")) {
                    if (!p.hasPermission("credits.admin.remove")) {
                        p.sendMessage(message.getString("prefix") + message.getString("no_perm"));
                    }

                    if (args.length == 3 && p.hasPermission("credits.admin.remove")) {
                        String playerName = args[1];
                        Player player = Bukkit.getPlayer(playerName);

                        if (player != null) {
                            long amount = 0;

                            try {
                                amount = Long.parseLong(args[2]);
                            } catch (NumberFormatException e) {
                                p.sendMessage(message.getString("prefix") + message.getString("not_number"));
                            }

                            if (amount < 0) {
                                sender.sendMessage(message.getString("prefix") + message.getString("not_positive_number"));
                            }


                            try {
                                long credits = database.getCreditsByUUID(player.getUniqueId().toString());
                                long final_credits = credits - amount;

                                database.setCreditsByUUID(player.getUniqueId().toString(), final_credits);

                                p.sendMessage(message.getString("prefix") + TextReplace.replacePlayerAndCredits(player, amount, message.getString("remove_credits")));
                                player.sendMessage(message.getString("prefix") + TextReplace.replaceAmount(amount, message.getString("credits_remove")));

                            } catch (SQLException e) {
                                e.printStackTrace();
                            }

                        } else {
                            p.sendMessage(message.getString("prefix") + message.getString("player_not_found"));
                        }
                    }
                    if (args.length != 3 && p.hasPermission("credits.admin.remove")) {
                        p.sendMessage(message.getString("prefix") + message.getString("invalid_use_remove"));
                    }
                } else if (args[0].equalsIgnoreCase("help")) {
                    List<String> list = message.getStringList("help");
                    for (String text : list) {
                        p.sendMessage(text);
                    }
                }
            }
        } else {
            if (args.length >= 1) {
                if (args[0].equalsIgnoreCase("reload")) {
                    plugin.reload();
                    Bukkit.getServer().getLogger().info("[NotCredits] " + message.getString("reload"));
                }
                if (args[0].equalsIgnoreCase("remove")) {
                    if (args.length == 3) {

                        String playerName = args[1];
                        Player player = Bukkit.getPlayer(playerName);

                        if (player != null) {

                            int amount = 0;
                            try {
                                amount = Integer.parseInt(args[2]);
                            } catch (NumberFormatException e) {
                                Bukkit.getServer().getLogger().info("[NotCredits] " + message.getString("not_number"));
                            }

                            if (amount < 0) {
                                Bukkit.getServer().getLogger().info("[NotCredits] " + message.getString("not_positive_number"));
                            }

                            try {
                                database.setCreditsByUUID(player.getUniqueId().toString(), amount);

                                Bukkit.getServer().getLogger().info("[NotCredits] " + TextReplace.replacePlayerAndCredits(player, amount, message.getString("set_credits")));
                                player.sendMessage(message.getString("prefix") + TextReplace.replaceAmount(amount, message.getString("credits_set")));
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        } else {
                            Bukkit.getServer().getLogger().info("[NotCredits] " + message.getString("player_not_found"));
                        }
                    } else {
                        Bukkit.getServer().getLogger().info("[NotCredits] " + message.getString("invalid_use_set"));
                    }
                }
                if (args[0].equalsIgnoreCase("add")) {

                    if (args.length == 3) {
                        String playerName = args[1];
                        Player player = Bukkit.getPlayer(playerName);

                        if (player != null) {
                            int amount = 0;
                            try {
                                amount = Integer.parseInt(args[2]);
                            } catch (NumberFormatException e) {
                                Bukkit.getServer().getLogger().info("[NotCredits] " + message.getString("not_number"));
                            }

                            if (amount < 0) {
                                Bukkit.getServer().getLogger().info("[NotCredits] " + message.getString("not_positive_number"));
                            }

                            try {
                                long credits = database.getCreditsByUUID(player.getUniqueId().toString());
                                long final_credits = credits + amount;

                                database.setCreditsByUUID(player.getUniqueId().toString(), final_credits);

                                Bukkit.getServer().getLogger().info("[NotCredits] " + TextReplace.replacePlayerAndCredits(player, amount, message.getString("add_credits")));
                                player.sendMessage(message.getString("prefix") + TextReplace.replaceAmount(amount, message.getString("credits_add")));

                            } catch (SQLException e) {
                                e.printStackTrace();
                            }

                        } else {
                            Bukkit.getServer().getLogger().info("[NotCredits] " + message.getString("player_not_found"));
                        }
                    } else {
                        Bukkit.getServer().getLogger().info("[NotCredits] " + message.getString("invalid_use_add"));
                    }
                }

                if (args[0].equalsIgnoreCase("remove")) {

                    if (args.length == 3) {
                        String playerName = args[1];
                        Player player = Bukkit.getPlayer(playerName);

                        if (player != null) {
                            int amount = 0;
                            try {
                                amount = Integer.parseInt(args[2]);
                            } catch (NumberFormatException e) {
                                Bukkit.getServer().getLogger().info("[NotCredits] " + message.getString("not_number"));
                            }

                            if (amount < 0) {
                                Bukkit.getServer().getLogger().info("[NotCredits] " + message.getString("not_positive_number"));
                            }

                            try {
                                long credits = database.getCreditsByUUID(player.getUniqueId().toString());
                                long final_credits = credits - amount;

                                database.setCreditsByUUID(player.getUniqueId().toString(), final_credits);

                                Bukkit.getServer().getLogger().info("[NotCredits] " + TextReplace.replacePlayerAndCredits(player, amount, message.getString("remove_credits")));
                                player.sendMessage(message.getString("prefix") + TextReplace.replaceAmount(amount, message.getString("remove_credits")));

                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        } else {
                            Bukkit.getServer().getLogger().info("[NotCredits] " + message.getString("player_not_found"));
                        }
                    } else {
                        Bukkit.getServer().getLogger().info("[NotCredits] " + message.getString("invalid_use_remove"));
                    }
                }
            }
        }
        return true;
    }
}
