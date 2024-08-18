package com.notmarra.notcredits.util;

public class Vault_Account {
    private String playerName;
    private double balance;

    public Vault_Account(String playerName, double balance) {
        this.playerName = playerName;
        this.balance = balance;
    }

    public String getPlayerName() {
        return this.playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public double getBalance() {
        return this.balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }
}
