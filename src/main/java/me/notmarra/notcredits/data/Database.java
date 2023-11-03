package me.notmarra.notcredits.data;

import com.zaxxer.hikari.HikariDataSource;
import me.notmarra.notcredits.Notcredits;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Database {
    String table;
    private HikariDataSource hikari;

    public static Database database = null;

    public void Database() {
        String host = Notcredits.getInstance().getConfig().getString("data.mysql.host");
        String name = Notcredits.getInstance().getConfig().getString("data.mysql.database");
        String user = Notcredits.getInstance().getConfig().getString("data.mysql.username");
        String pass = Notcredits.getInstance().getConfig().getString("data.mysql.password");
        int port = Notcredits.getInstance().getConfig().getInt("data.mysql.port");
        this.table = Notcredits.getInstance().getConfig().getString("data.table");

        hikari = new HikariDataSource();

        if (Notcredits.getInstance().getConfig().getString("data.type").equalsIgnoreCase("mysql")) {
            hikari.setDriverClassName("com.mysql.jdbc.Driver");
            hikari.setJdbcUrl("jdbc:mysql://" + host + ":" + port + "/" + name);
            hikari.setUsername(user);
            hikari.setPassword(pass);
            hikari.setMaximumPoolSize(10);
            hikari.setConnectionTimeout(10000);
            hikari.setLeakDetectionThreshold(10000);
            hikari.setPoolName("NotCredits");
        } else {
            File dbFile = new File(Notcredits.getInstance().getDataFolder(), Notcredits.getInstance().getConfig().getString("data.file"));
            hikari.setDriverClassName("org.sqlite.JDBC");
            hikari.setJdbcUrl("jdbc:sqlite:" + dbFile.getAbsolutePath());
            hikari.setMaximumPoolSize(10);
            hikari.setConnectionTimeout(10000);
            hikari.setLeakDetectionThreshold(10000);
            hikari.setPoolName("NotCredits");
        }

    }

    public void disconnectFromDB() {
        hikari.close();
    }

    public static Database getInstance() {
        if (database == null) {
            database = new Database();
        }
        return database;
    }

    public HikariDataSource getHikari() {
        return hikari;
    }


    public void initializeTables() {
        Connection connection = null;
        String sql = "CREATE TABLE IF NOT EXISTS \" + this.table + \"(uuid varchar(36) primary key, player_name varchar(36), balance double)";

        try {
            connection = hikari.getConnection();
            connection.prepareStatement(sql).execute();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (connection != null) connection.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public String stmtDB(String sql, String... params) {
        Connection connection = null;
        PreparedStatement stmt = null;
        ResultSet result = null;

        try {
            connection = hikari.getConnection();
            stmt = connection.prepareStatement(sql);
            for (int i = 0; i < params.length; i++) {
                stmt.setString(i + 1, params[i]);
            }
            stmt.execute();
            result = stmt.getResultSet();
            if (result != null && result.next()) {
                return result.getString(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (connection != null) connection.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public String findPlayerByUUID(String uuid) {
        String sql = "SELECT * FROM " + this.table + " WHERE uuid = ?";
        return stmtDB(sql, uuid);
    }
    public String findPlayerByPN(String player_name) {
        String sql = "SELECT * FROM " + this.table + " WHERE player_name = ?";
        return stmtDB(sql, player_name);
    }
    public void addPlayerData(String uuid, String player_name, double balance) {
        String sql = "INSERT INTO " + this.table + " (uuid, player_name, balance) VALUES (?, ?, ?)";
        stmtDB(sql, uuid, player_name, String.valueOf(balance));
    }
    public double getCreditsByUUID(String uuid) {
        String sql = "SELECT balance FROM " + this.table + " WHERE uuid = ?";
        return Double.parseDouble(stmtDB(sql, uuid));
    }
    public double getCreditsByPlayerName(String player_name) {
        String sql = "SELECT balance FROM " + this.table + " WHERE player_name = ?";
        return Double.parseDouble(stmtDB(sql, player_name));
    }
    public void setCreditsByUUID(String uuid, double balance) {
        String sql = "UPDATE " + this.table + " SET balance = ? WHERE uuid = ?";
        stmtDB(sql, String.valueOf(balance), uuid);
    }
    public void setCreditsByPlayerName(String player_name, double balance) {
        String sql = "UPDATE " + this.table + " SET balance = ? WHERE player_name = ?";
        stmtDB(sql, String.valueOf(balance), player_name);
    }
    public double getCreditsByOrder(int pos) {
        String sql = "SELECT balance FROM " + this.table + " ORDER BY balance DESC LIMIT 1 OFFSET ?";
        return Double.parseDouble(stmtDB(sql, String.valueOf(pos)));
    }
    public String getPlayerByOrder(int pos) {
        String sql = "SELECT player_name FROM " + this.table + " ORDER BY balance DESC LIMIT 1 OFFSET ?";
        return stmtDB(sql, String.valueOf(pos));
    }
}
