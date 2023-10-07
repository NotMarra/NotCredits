/* Decompiler 3ms, total 122ms, lines 34 */
package me.notmarra.notcredits.listeners;

import java.sql.SQLException;
import me.notmarra.notcredits.Notcredits;
import me.notmarra.notcredits.data.Database;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoin implements Listener {
   private FileConfiguration config;

   public PlayerJoin() {
      this.config = Notcredits.main.getConfig();
   }

   @EventHandler
   public void onPlayerJoin(PlayerJoinEvent e) {
      Player p = e.getPlayer();

      try {
         String data = Database.database.findPlayerByUUID(p.getUniqueId().toString());
         if (data == null) {
            Database.database.addPlayerData(p.getUniqueId().toString(), p.getName(), this.config.getLong("default_balance"));
         }
      } catch (SQLException var4) {
         var4.printStackTrace();
      }

   }
}
