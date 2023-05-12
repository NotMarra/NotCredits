package me.notmarra.notcredits.Listeners;


import me.notmarra.notcredits.Data.Data;
import me.notmarra.notcredits.Data.Database;
import me.notmarra.notcredits.Notcredits;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.sql.SQLException;

public class playerJoin implements Listener {
    private Database database = null;
    private FileConfiguration config = Notcredits.main.getConfig();

    public playerJoin() {
        try {
            this.database = Database.getInstance();
        } catch (SQLException error) {
            error.printStackTrace();
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        try {
            Data data = database.findPlayerByUUID(p.getUniqueId().toString());

            if (data == null) {
                database.addPlayerData(p.getUniqueId().toString(), p.getName(), config.getLong("default_balance"));
            }
        } catch (SQLException error) {
            error.printStackTrace();
        }
    }
}