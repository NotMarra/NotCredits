package me.notmarra.notcredits.Listeners;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.notmarra.notcredits.Data.Database;
import me.notmarra.notcredits.Notcredits;
import org.bukkit.entity.Player;

import java.sql.SQLException;

public class Placeholders extends PlaceholderExpansion {
    @Override
    public boolean canRegister() {
        return true;
    }

    @Override
    public String getAuthor() {
        return "NotMarra";
    }

    @Override
    public String getIdentifier() {
        return "NotCredits";
    }

    @Override
    public String getVersion() {
        return Notcredits.main.getDescription().getVersion();
    }

    @Override
    public String onPlaceholderRequest(Player player, String identifier) {
        if (player == null) {
            return "";
        }

        if (identifier.equals("amount")) {
            String pname = player.getUniqueId().toString();
            long credits = 0;
            try {
                credits = Database.database.getCreditsByUUID(pname);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return String.valueOf(credits);
        }

        return null;
    }
}
