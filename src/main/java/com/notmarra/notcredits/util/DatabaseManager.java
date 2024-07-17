package com.notmarra.notcredits.util;

import com.notmarra.notcredits.Notcredits;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.bukkit.configuration.file.FileConfiguration;
import org.h2.engine.Database;

import java.io.File;
import java.sql.*;

public class DatabaseManager {

    private final String databaseType;
    private final String host;
    private final String name;
    private final String user;
    private final String pass;
    private final int port;
    private final String table;
    private final String fileName;
    private static DatabaseManager instance;
    private static HikariDataSource dataSource;

    private DatabaseManager(Notcredits plugin) {
        FileConfiguration config = plugin.getConfig();
        this.databaseType = config.getString("data.type").toLowerCase();
        this.host = config.getString("data.mysql.host");
        this.name = config.getString("data.mysql.database");
        this.user = config.getString("data.mysql.username");
        this.pass = config.getString("data.mysql.password");
        this.port = config.getInt("data.mysql.port");
        this.table = config.getString("data.table");
        this.fileName = config.getString("data.file");
    }

    public static DatabaseManager getInstance(Notcredits plugin) {
        if (instance == null) {
            instance = new DatabaseManager(plugin);
        }
        return instance;
    }

    public void close() {
        dataSource.close();
    }


    public boolean isConnected() {
        return dataSource != null;
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
        try {
            Class.forName("org.h2.Driver");
            HikariConfig config = prepareConfig("jdbc:h2:file:" + Notcredits.getInstance().getDataFolder().getAbsolutePath() + File.separator + fileName + ".h2");
            dataSource = new HikariDataSource(config);
            setupTable();
        } catch (ClassNotFoundException e) {
            Notcredits.getInstance().getLogger().severe("H2 Driver not found: " + e.getMessage());
        } catch (Exception e) {
            Notcredits.getInstance().getLogger().severe("Error setting up H2 database: " + e.getMessage());
        }
    }

    private void setupSQLite() {
        HikariConfig config;
        config = prepareConfig("jdbc:sqlite:" + Notcredits.getInstance().getDataFolder().getAbsolutePath() + File.separator + fileName + ".db");

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
        config.setPoolName("NotCredits");
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
        setSTMT("INSERT INTO " + table + " (uuid, player_name, balance) VALUES (?, ?, ?)", uuid, player_name, String.valueOf(balance));
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
