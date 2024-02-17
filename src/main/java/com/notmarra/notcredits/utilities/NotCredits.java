package com.notmarra.notcredits.utilities;

import com.notmarra.notcredits.data.Database;

import java.util.UUID;

public class NotCredits {
    /*
        * Get the balance of a player
        * @param uuid The UUID of the player
        * @return The balance of the player
     */
    public double getBalance(UUID uuid) {
        return Database.database.getCreditsByUUID(String.valueOf(uuid));
    }

    /*
        * Set the balance of a player
        * @param uuid The UUID of the player
        * @param amount The amount to set the balance to
     */
    public void setBalance(UUID uuid, double amount) {
        Database.database.setCreditsByUUID(String.valueOf(uuid), amount);
    }
}
