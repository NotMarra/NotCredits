package me.notmarra.notcredits.utility;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class TabCompletion implements TabCompleter {

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        List<String> completions = new ArrayList<>();
        if (args.length == 1) {
            completions.add("add");
            completions.add("remove");
            completions.add("set");
            completions.add("help");
            completions.add("reload");
        } else if (args.length == 2) {
            switch (args[0].toLowerCase()) {
                case "add":
                case "remove":
                case "set":
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        completions.add(player.getName());
                    }
                    break;
            }
        } else if (args.length == 3) {
            switch (args[0].toLowerCase()) {
                case "set":
                case "add":
                case "remove":
                    completions.add("<amount>");
                    break;
            }
        }
        String currentArg = args[args.length - 1];
        completions.removeIf(s -> !s.startsWith(currentArg));

        return completions;
    }
}
