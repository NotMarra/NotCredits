package com.notmarra.notcredits.events;

import com.notmarra.notcredits.Notcredits;
import com.notmarra.notcredits.util.DatabaseManager;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import net.milkbowl.vault.economy.EconomyResponse.ResponseType;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.util.List;

public class Economy_NotCredits implements Economy {
    public boolean isEnabled() {
        return true;
    }

    public String getName() {
        return "NotCredits";
    }

    public int fractionalDigits() {
        return -1;
    }

    public String format(double v) {
        return v + " " + (v > 1.0D ? this.currencyNamePlural() : this.currencyNameSingular());
    }

    public String currencyNamePlural() {
        return "credits";
    }

    public String currencyNameSingular() {
        return "credit";
    }

    public double getBalance(String name) {
        return this.getBalance((OfflinePlayer)Bukkit.getPlayerExact(name));
    }

    public double getBalance(OfflinePlayer player) {
        return player != null && player.getPlayer() != null ? DatabaseManager.getInstance(Notcredits.getInstance()).getBalanceByPlayerName(player.getName()) : 0.0D;
    }

    public double getBalance(String name, String world) {
        return this.getBalance(name);
    }

    public double getBalance(OfflinePlayer player, String world) {
        return this.getBalance(player);
    }

    public boolean has(String name, double amount) {
        return this.getBalance(name) >= (double)((long)amount);
    }

    public boolean has(OfflinePlayer player, double amount) {
        return this.getBalance(player) >= amount;
    }

    public boolean has(String name, String world, double amount) {
        return this.has(name, amount);
    }

    public boolean has(OfflinePlayer player, String world, double amount) {
        return this.has(player, amount);
    }

    public EconomyResponse withdrawPlayer(String name, double amount) {
        return this.withdrawPlayer((OfflinePlayer)Bukkit.getPlayerExact(name), amount);
    }

    public EconomyResponse withdrawPlayer(OfflinePlayer player, double amount) {
        if (player == null) {
            return new EconomyResponse(0.0D, 0.0D, ResponseType.FAILURE, "Player is not online");
        } else {
            double balance = this.getBalance(player);
            if (amount == 0.0D) {
                return new EconomyResponse(amount, balance, ResponseType.SUCCESS, "");
            } else if (balance < amount) {
                return new EconomyResponse(amount, balance, ResponseType.FAILURE, "Not enough credits");
            } else {
                balance -= amount;

                DatabaseManager.getInstance(Notcredits.getInstance()).setBalanceByPlayerName(player.getName(), balance);

                return new EconomyResponse(amount, balance, ResponseType.SUCCESS, "");
            }
        }
    }

    public EconomyResponse withdrawPlayer(String name, String world, double amount) {
        return this.withdrawPlayer(name, amount);
    }

    public EconomyResponse withdrawPlayer(OfflinePlayer player, String name, double amount) {
        return this.withdrawPlayer(player, amount);
    }

    public EconomyResponse depositPlayer(String name, double amount) {
        return this.depositPlayer((OfflinePlayer)Bukkit.getPlayer(name), amount);
    }

    public EconomyResponse depositPlayer(OfflinePlayer player, double amount) {
        if (player == null) {
            return new EconomyResponse(0.0D, 0.0D, ResponseType.FAILURE, "Player is not online");
        } else {
            double balance = this.getBalance(player);
            if (amount == 0.0D) {
                return new EconomyResponse(amount, balance, ResponseType.SUCCESS, "");
            } else {
                balance += amount;

                DatabaseManager.getInstance(Notcredits.getInstance()).setBalanceByPlayerName(player.getName(), balance);

                return new EconomyResponse(amount, balance, ResponseType.SUCCESS, "");
            }
        }
    }

    public EconomyResponse depositPlayer(String name, String world, double amount) {
        return this.depositPlayer(name, amount);
    }

    public EconomyResponse depositPlayer(OfflinePlayer player, String world, double amount) {
        return this.depositPlayer(player, amount);
    }

    public boolean hasBankSupport() {
        return false;
    }

    public boolean createPlayerAccount(String name) {
        return true;
    }

    public boolean createPlayerAccount(OfflinePlayer player) {
        return true;
    }

    public boolean createPlayerAccount(String name, String world) {
        return true;
    }

    public boolean createPlayerAccount(OfflinePlayer player, String world) {
        return true;
    }

    public boolean hasAccount(String name) {
        return true;
    }

    public boolean hasAccount(OfflinePlayer player) {
        return true;
    }

    public boolean hasAccount(String name, String world) {
        return true;
    }

    public boolean hasAccount(OfflinePlayer player, String world) {
        return true;
    }

    public EconomyResponse createBank(String s, String s1) {
        return null;
    }

    public EconomyResponse createBank(String s, OfflinePlayer offlinePlayer) {
        return null;
    }

    public EconomyResponse deleteBank(String s) {
        return null;
    }

    public EconomyResponse bankBalance(String s) {
        return null;
    }

    public EconomyResponse bankHas(String s, double v) {
        return null;
    }

    public EconomyResponse bankWithdraw(String s, double v) {
        return null;
    }

    public EconomyResponse bankDeposit(String s, double v) {
        return null;
    }

    public EconomyResponse isBankOwner(String s, String s1) {
        return null;
    }

    public EconomyResponse isBankOwner(String s, OfflinePlayer offlinePlayer) {
        return null;
    }

    public EconomyResponse isBankMember(String s, String s1) {
        return null;
    }

    public EconomyResponse isBankMember(String s, OfflinePlayer offlinePlayer) {
        return null;
    }

    public List<String> getBanks() {
        return null;
    }
}