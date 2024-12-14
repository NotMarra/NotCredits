package com.notmarra.notcredits.events;

import com.notmarra.notcredits.NotCredits;
import com.notmarra.notcredits.util.DatabaseManager;
import com.notmarra.notcredits.util.Numbers;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;

public class Placeholders extends PlaceholderExpansion {
    private final NotCredits plugin;

    public Placeholders(NotCredits plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean persist() {
        return true;
    }

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
        return NotCredits.getInstance().getDescription().getVersion();
    }


    public String onPlaceholderRequest(Player player, String identifier) {
        if (player == null) {
            return "";
        } else if (identifier.equals("credits")) {
            String pname = player.getUniqueId().toString();
            double credits = 0.0D;


            credits = DatabaseManager.getInstance(NotCredits.getInstance()).getBalance(pname);

            return Numbers.formatBalance(credits);
        } else {
            if (identifier.startsWith("top_")) {
                String[] parts = identifier.split("_");
                int position;
                if (parts.length == 2) {
                    position = Integer.parseInt(parts[1])-1;

                    if (DatabaseManager.getInstance(NotCredits.getInstance()).getPlayerByBalance(position) == null) {
                        return NotCredits.getInstance().getConfig().getString("placeholder_no_data");
                    } else {
                        return Numbers.formatBalance(Double.parseDouble(DatabaseManager.getInstance(NotCredits.getInstance()).getPlayerByBalance(position)));
                    }
                } else if (parts.length == 3 && parts[0].equals("top") && parts[1].equals("name")) {
                    position = Integer.parseInt(parts[2])-1;
                    if (DatabaseManager.getInstance(NotCredits.getInstance()).getPlayerByBalance(position) == null) {
                        return NotCredits.getInstance().getConfig().getString("placeholder_no_data");
                    } else {
                        return DatabaseManager.getInstance(NotCredits.getInstance()).getPlayerByBalance(position);
                    }
                }
            }

            return null;
        }
    }
}
