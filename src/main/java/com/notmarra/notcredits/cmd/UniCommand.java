package com.notmarra.notcredits.cmd;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class UniCommand {
    public static void execute(CommandSender sender, String [] args, String operator, String permission, String message_player, String message_admin, String message_invalid) {

        if (sender instanceof Player p) {
            if (!p.hasPermission(permission)) {
                //edit message
                p.sendMessage(message_invalid);
            } else {
                if (args.length == 3) {
                    Player player = Bukkit.getPlayer(args[1]);



                    //to do
                    //:)
                    

                }
            }
        } else {

        }
    }
}
