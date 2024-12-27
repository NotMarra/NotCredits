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

public class BaseCommand {
    public static void execute(CommandSender sender, String target) {
        boolean isConsole = !(sender instanceof Player);
        Player player = isConsole ? null : (Player) sender;

        if (target != null) {
            Player targetPlayer = Bukkit.getPlayer(target);
            Bukkit.getScheduler().runTaskAsynchronously(NotCredits.getInstance(), () -> {
                Map<String, String> replacements = new HashMap<>();
                replacements.put("player", targetPlayer.getName());
                replacements.put("credits", Numbers.formatBalance(DatabaseManager.getInstance(NotCredits.getInstance()).getBalance(targetPlayer.getUniqueId().toString())));
                Message.sendMessage(player, "credits_other", isConsole, replacements);
            });
        } else {
            if (sender instanceof Player p) {
                Bukkit.getScheduler().runTaskAsynchronously(NotCredits.getInstance(), () -> {
                    Map<String, String> replacements = new HashMap<>();
                    replacements.put("credits", Numbers.formatBalance(DatabaseManager.getInstance(NotCredits.getInstance()).getBalance(p.getUniqueId().toString())));
                    Message.sendMessage(p, "credits", false, replacements);
                });
            }
        }
    }
}
