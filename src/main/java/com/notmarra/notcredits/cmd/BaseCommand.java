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
    public static void execute(CommandSender sender) {
        if (sender instanceof Player p) {
            Bukkit.getScheduler().runTaskAsynchronously(NotCredits.getInstance(), () -> {
                Map<String, String> replacements = new HashMap<>();
                replacements.put("credits", Numbers.formatBalance(DatabaseManager.getInstance(NotCredits.getInstance()).getBalance(p.getUniqueId().toString())));
                Message.sendMessage(p, "credits", false, replacements);
            });
        }
    }
}
