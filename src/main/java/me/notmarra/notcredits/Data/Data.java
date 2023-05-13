package me.notmarra.notcredits.Data;

public class Data {

    private String uuid;
    private String player_name;
    private long balance;

    public Data(String uuid, String player_name, long balance) {
        this.uuid = uuid;
        this.player_name = player_name;
        this.balance = balance;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getPlayer_name() {
        return player_name;
    }

    public void setPlayer_name(String player_name) {
        this.player_name = player_name;
    }

    public long getBalance() {
        return balance;
    }

    public void setBalance(long balance) {
        this.balance = balance;
    }
}