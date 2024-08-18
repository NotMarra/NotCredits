package com.notmarra.notcredits.events;

import com.notmarra.notcredits.Notcredits;
import com.notmarra.notcredits.util.DatabaseManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoin implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Bukkit.getScheduler().runTaskAsynchronously(Notcredits.getInstance(), new Runnable() {
            @Override
            public void run() {
                Player player = event.getPlayer();
                if (!DatabaseManager.getInstance(Notcredits.getInstance()).hasAccount(player.getUniqueId().toString())){
                    DatabaseManager.getInstance(Notcredits.getInstance()).setupPlayer(player.getUniqueId().toString(), player.getName(), Notcredits.getInstance().getConfig().getDouble("default_balance"));
                }
            }
        });
    }
}
