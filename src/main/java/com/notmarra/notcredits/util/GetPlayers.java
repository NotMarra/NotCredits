package com.notmarra.notcredits.util;

public class GetPlayers {
    public static List<Player> get() {
        List<Player> players = new ArrayList<>(Bukkit.getOnlinePlayers());
        return players;
    }
}