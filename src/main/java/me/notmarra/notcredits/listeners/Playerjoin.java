package me.notmarra.notcredits.listeners;

import me.notmarra.notcredits.Notcredits;
import me.notmarra.notcredits.data.Database;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class Playerjoin implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (Database.getInstance().findPlayerByUUID(String.valueOf(player.getUniqueId())) != null){
            Database.getInstance().addPlayerData(player.getUniqueId().toString(), player.getName(), Notcredits.getInstance().getConfig().getDouble("default-balance"));
        }
    }
}