package me.notmarra.notcredits.utility;

import me.notmarra.notcredits.Data.Database;
import org.bukkit.entity.Player;

import java.sql.SQLException;

public class TextReplace {

    public static String replaceCredits(Player player, String text) throws SQLException {
        long credits = Database.database.getCreditsByUUID(player.getUniqueId().toString());

        String result = text.replace("%credits%", String.valueOf(credits));
        return result;
    }

    public static String replacePlayerAndCredits(Player player, long amount, String message){
        String text = message.replace("%player%", player.getName()).replace("%amount%", String.valueOf(amount));
        return text;
    }

    public static String replaceAmount(long amount, String message){
        String text = message.replace("%amount%", String.valueOf(amount));
        return text;
    }
}
