package com.notmarra.notcredits.util;

import com.notmarra.notcredits.Notcredits;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    private void setSTMT(String sql, Object... params) {
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement stmt = connection.prepareStatement(sql);
            for (int i = 0; i < params.length; i++) {
                stmt.setObject(i + 1, params[i]);
            }
            stmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private List<Map<String, String>> getSTMT(String sql, Object... params) {
        if (sql == null || sql.isEmpty() || params == null) return null;
        List<Map<String, String>> resultList = new ArrayList<>();

        try (Connection connection = dataSource.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            for (int i = 0; i < params.length; i++) {
                stmt.setObject(i + 1, params[i]);
            }

            try (ResultSet result = stmt.executeQuery()) {
                ResultSetMetaData metaData = result.getMetaData();
                int columnCount = metaData.getColumnCount();

                while (result.next()) {
                    Map<String, String> row = new HashMap<>();
                    for (int i = 1; i <= columnCount; i++) {
                        String columnName = metaData.getColumnName(i);
                        String value = result.getString(i);
                        row.put(columnName, value);
                    }
                    resultList.add(row);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return resultList.isEmpty() ? null : resultList;
    }

    private String getSTMTSingle(String sql, Object... params) {
        List<Map<String, String>> result = getSTMT(sql, params);
        if (result != null && !result.isEmpty()) {
            Map<String, String> firstRow = result.get(0);
            if (!firstRow.isEmpty()) {
                return firstRow.values().iterator().next();
            }
        }
        return null;
    }
    public void setupPlayer(String uuid, String player_name, double balance) {
        setSTMT("INSERT INTO " + table + " (uuid, player_name, balance) VALUES (?, ?, ?)", uuid, player_name, balance);
    }

    public void setBalance(String uuid, double balance) {
        setSTMT("UPDATE " + table + " SET balance = ? WHERE uuid = ?", balance, uuid);
    }

    public double getBalance(String uuid) {
        String balance = getSTMTSingle("SELECT balance FROM " + table + " WHERE uuid = ?", uuid);
        return balance != null ? Double.parseDouble(balance) : 0.0;
    }

    public boolean hasAccount(String uuid) {
        return getSTMT("SELECT uuid FROM " + table + " WHERE uuid = ?", uuid) != null;
    }

    public void setBalanceByPlayerName(String name, double balance) {
        setSTMT("UPDATE " + table + " SET balance = ? WHERE player_name = ?", balance, name);
    }

    public double getBalanceByPlayerName(String name) {
        String balance = getSTMTSingle("SELECT balance FROM " + table + " WHERE player_name = ?", name);
        return balance != null ? Double.parseDouble(balance) : 0.0;
    }

    public double getBalanceByOrder(int position) {
        String balance = getSTMTSingle("SELECT balance FROM " + table + " ORDER BY balance DESC LIMIT 1 OFFSET ?", position);
        return balance != null ? Double.parseDouble(balance) : -1.0;
    }

    public String getPlayerByBalance(int position) {
        return getSTMTSingle("SELECT player_name FROM " + table + " ORDER BY balance DESC LIMIT 1 OFFSET ?", position);
    }

    public List<Map<String, Double>> getPlayersByBalance(int position, int amount) {
        List<Map<String, String>> result = getSTMT("SELECT player_name, balance FROM " + table + " ORDER BY balance DESC LIMIT ? OFFSET ?", amount, position);
        if (result == null) return null;

        List<Map<String, Double>> resultList = new ArrayList<>();
        for (Map<String, String> row : result) {
            Map<String, Double> newRow = new HashMap<>();
            for (Map.Entry<String, String> entry : row.entrySet()) {
                newRow.put(entry.getKey(), Double.parseDouble(entry.getValue()));
            }
            resultList.add(newRow);
        }
        return resultList;
    }
}

