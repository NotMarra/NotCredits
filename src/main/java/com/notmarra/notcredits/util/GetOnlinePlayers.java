package com.notmarra.notcredits.util;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class GetOnlinePlayers {
    public static List<Player> get() {
        List<Player> players = new ArrayList<>();
        for (Player player : Bukkit.getOnlinePlayers()) {
            players.add(player);
        }
        return players;
    }
}
