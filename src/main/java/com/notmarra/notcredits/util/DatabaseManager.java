package com.notmarra.notcredits.util;

import com.notmarra.notcredits.Notcredits;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.*;

public class DatabaseManager {

    private final String databaseType;
    private final String host = Notcredits.getInstance().getConfig().getString("data.mysql.host");
    private final String name = Notcredits.getInstance().getConfig().getString("data.mysql.database");
    private final String user = Notcredits.getInstance().getConfig().getString("data.mysql.username");
    private final String pass = Notcredits.getInstance().getConfig().getString("data.mysql.password");
    int port = Notcredits.getInstance().getConfig().getInt("data.mysql.port");
    private final String table = Notcredits.getInstance().getConfig().getString("data.table");
    private final String fileName = Notcredits.getInstance().getConfig().getString("data.file");

    private HikariDataSource dataSource;

    public DatabaseManager() {
        databaseType = Notcredits.getInstance().getConfig().getString("data.type").toLowerCase();
    }

    public void setupDB() {
        switch (databaseType) {
            case "mysql":
                setupMySQL();
                break;
            case "sqlite":
                setupSQLite();
                break;
            case "h2":
            default:
                setupH2();
                break;
        }
    }

    private void setupH2() {
        HikariConfig config;
        config = prepareConfig("jdbc:h2:file:" + Notcredits.getInstance().getDataFolder().getAbsolutePath() + fileName + "h2.db");

        dataSource = new HikariDataSource(config);

        setupTable();
    }

    private void setupSQLite() {
        HikariConfig config;
        config = prepareConfig("jdbc:sqlite:" + Notcredits.getInstance().getDataFolder().getAbsolutePath() + fileName + ".db");

        dataSource = new HikariDataSource(config);

        setupTable();
    }

    private void setupMySQL() {
        HikariConfig config;
        config = prepareConfig("jdbc:mysql://" + host + ":" + port + "/" + name);

        dataSource = new HikariDataSource(config);

        setupTable();
    }

    private HikariConfig prepareConfig(String url) {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(url);
        config.setUsername(user);
        config.setPassword(pass);
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        return config;
    }

    private void setupTable() {
        try (Connection connection = dataSource.getConnection()) {
            Statement statement = connection.createStatement();
            statement.execute("CREATE TABLE IF NOT EXISTS " + table + " (uuid varchar(36) primary key, player_name varchar(36), balance double)");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean isConnected() {
        try {
            return dataSource.getConnection() != null && !dataSource.getConnection().isClosed();
        } catch (Exception e) {
            return false;
        }
    }

    public void close() {
        dataSource.close();
    }

    private void setSTMT(String sql, String... params) {
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement stmt = connection.prepareStatement(sql);
            for (int i = 0; i < params.length; i++) {
                stmt.setString(i + 1, params[i]);
            }
            stmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private String getSTMT(String sql, String... params) {
        if (sql == null || sql.isEmpty() || params == null || params.length == 0) return null;
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement stmt = connection.prepareStatement(sql);
            for (int i = 0; i < params.length; i++) {
                stmt.setString(i + 1, params[i]);
            }
            stmt.execute();
            ResultSet result = stmt.getResultSet();
            if (result != null && result.next()) {
                return result.getString(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void setupPlayer(String uuid, String player_name, double balance) {
        setSTMT("INSERT INTO " + table + " (uuid, player_name, balance) VALUES (?, ?, ?) ON DUPLICATE KEY UPDATE balance = ?", uuid, player_name, String.valueOf(balance), String.valueOf(balance));
    }

    public void setBalance(String uuid, double balance) {
        setSTMT("UPDATE " + table + " SET balance = ? WHERE uuid = ?", String.valueOf(balance), uuid);
    }

    public double getBalance(String uuid) {
        return Double.parseDouble(getSTMT("SELECT balance FROM " + table + " WHERE uuid = ?", uuid));
    }

    public String getPlayerName(String uuid) {
        return getSTMT("SELECT player_name FROM " + table + " WHERE uuid = ?", uuid);
    }

    public boolean hasAccount(String uuid) {
        return getSTMT("SELECT uuid FROM " + table + " WHERE uuid = ?", uuid) != null;
    }
}
