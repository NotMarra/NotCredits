package com.notmarra.notcredits.events;

import com.notmarra.notcredits.NotCredits;
import com.notmarra.notcredits.util.DatabaseManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoin implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Bukkit.getScheduler().runTaskAsynchronously(NotCredits.getInstance(), new Runnable() {
            @Override
            public void run() {
                Player player = event.getPlayer();
                if (!DatabaseManager.getInstance(NotCredits.getInstance()).hasAccount(player.getUniqueId().toString())){
                    DatabaseManager.getInstance(NotCredits.getInstance()).setupPlayer(player.getUniqueId().toString(), player.getName(), NotCredits.getInstance().getConfig().getDouble("default_balance"));
                }
            }
        });
    }
}
