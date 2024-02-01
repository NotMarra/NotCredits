package com.notmarra.notcredits.listeners;

import com.notmarra.notcredits.data.Database;
import com.notmarra.notcredits.Notcredits;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class Playerjoin implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Bukkit.getScheduler().runTaskAsynchronously(Notcredits.getInstance(), new Runnable() {
            @Override
            public void run() {
                Player player = event.getPlayer();
                if (Database.getInstance().findPlayerByUUID(String.valueOf(player.getUniqueId())) == null){
                    Database.getInstance().addPlayerData(player.getUniqueId().toString(), player.getName(), Notcredits.getInstance().getConfig().getDouble("default-balance"));
                }
            }
        });
    }
}
