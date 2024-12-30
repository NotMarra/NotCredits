package com.notmarra.notcredits.util;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class TabCompletion implements TabCompleter {
    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String label, String[] args) {
        List<String> completions = new ArrayList<>();
        String currentArg;
        if(args.length == 1) {
            completions.add("add");
            completions.add("remove");
            completions.add("set");
            completions.add("reload");
            completions.add("help");
            for (Player player : Bukkit.getOnlinePlayers()) {
                completions.add(player.getName());
            }
        } else {
            if (args.length == 2) {
                currentArg = args[0].toLowerCase();
                switch (currentArg) {
                    case "add":
                    case "remove":
                    case "set":
                        for (Player player : Bukkit.getOnlinePlayers()) {
                            completions.add(player.getName());
                        }
                }
            } else if (args.length == 3) {
                currentArg = args[0].toLowerCase();
                switch (currentArg) {
                    case "add":
                    case "remove":
                    case "set":
                        completions.add("<amount>");
                }
            }
        }
        currentArg = args[args.length - 1];
        String finalCurrentArg = currentArg;
        completions.removeIf((s) -> !s.startsWith(finalCurrentArg));
        return completions;
    }
}
