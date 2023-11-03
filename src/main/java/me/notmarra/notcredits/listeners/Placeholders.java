package me.notmarra.notcredits.listeners;

import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.Objects;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.notmarra.notcredits.Notcredits;
import me.notmarra.notcredits.data.Database;
import org.bukkit.entity.Player;

public class Placeholders extends PlaceholderExpansion {
    public boolean canRegister() {
        return true;
    }

    public String getAuthor() {
        return "NotMarra";
    }

    public String getIdentifier() {
        return "NotCredits";
    }

    public String getVersion() {
        return Notcredits.getInstance().getDescription().getVersion();
    }

    public String onPlaceholderRequest(Player player, String identifier) {
        if (player == null) {
            return "";
        } else if (identifier.equals("credits")) {
            String pname = player.getUniqueId().toString();
            double credits = 0.0D;


                credits = Database.getInstance().getCreditsByUUID(pname);

            if (Notcredits.getInstance().getConfig().getBoolean("balance_decimal")) {
                DecimalFormat decimalFormat = new DecimalFormat(Objects.requireNonNull(Notcredits.getInstance().getConfig().getString("balance_format")));
                return String.valueOf(decimalFormat.format(credits));
            } else {
                return String.valueOf(Math.round(credits));
            }
        } else {
            if (identifier.startsWith("top_")) {
                String[] parts = identifier.split("_");
                int position;
                if (parts.length == 2) {
                    position = Integer.parseInt(parts[1]);

                        if (Math.round(Database.getInstance().getCreditsByOrder(position)) == -1L) {
                            return "NaN";
                        }
                        return String.valueOf(Math.round(Database.getInstance().getCreditsByOrder(position)));
                } else if (parts.length == 3 && parts[0].equals("top") && parts[1].equals("name")) {
                    position = Integer.parseInt(parts[2]);
                        return Database.getInstance().getPlayerByOrder(position);
                }
            }

            return null;
        }
    }
}