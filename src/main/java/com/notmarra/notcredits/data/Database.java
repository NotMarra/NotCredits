package com.notmarra.notcredits.data;

import com.zaxxer.hikari.HikariDataSource;
import com.notmarra.notcredits.Notcredits;

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
            hikari.setJdbcUrl("jdbc:mysql://" + host + ":" + port + "/" + name);
            hikari.setUsername(user);
            hikari.setPassword(pass);
            hikari.setDriverClassName("com.mysql.jdbc.Driver");
            hikari.setMaximumPoolSize(10);
            hikari.setConnectionTimeout(10000);
            hikari.setLeakDetectionThreshold(10000);
            hikari.setPoolName("NotCredits");
        } else if (Notcredits.getInstance().getConfig().getString("data.type").equalsIgnoreCase("sqlite")) {
            String pluginPath = Notcredits.getInstance().getDataFolder().getAbsolutePath();
            String databasePath = pluginPath + File.separator + Notcredits.getInstance().getConfig().getString("data.file");
            hikari.setJdbcUrl("jdbc:sqlite:" + databasePath);
            hikari.setDriverClassName("org.sqlite.JDBC");
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
        String sql = "CREATE TABLE IF NOT EXISTS " + this.table + " (uuid varchar(36) primary key, player_name varchar(36), balance double)";

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


    public String findPlayerByUUID(String uuid) {
        String sql = "SELECT * FROM " + this.table + " WHERE uuid = ?";

        try {
            Connection connection = hikari.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, uuid);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                String player_name = resultSet.getString("player_name");
                statement.close();
                connection.close();
                return player_name;
            }
            statement.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void addPlayerData(String uuid, String player_name, double balance) {
        String sql = "INSERT INTO " + this.table + " (uuid, player_name, balance) VALUES (?, ?, ?)";

        try {
            Connection connection = hikari.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, uuid);
            statement.setString(2, player_name);
            statement.setDouble(3, balance);
            statement.executeUpdate();
            statement.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public double getCreditsByUUID(String uuid) {
        String sql = "SELECT balance FROM " + this.table + " WHERE uuid = ?";

        try {
            Connection connection = hikari.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, uuid);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                double balance = resultSet.getDouble("balance");
                statement.close();
                connection.close();
                return balance;
            }
            statement.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
    public double getCreditsByPlayerName(String player_name) {
        String sql = "SELECT balance FROM " + this.table + " WHERE player_name = ?";

        try {
            Connection connection = hikari.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, player_name);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                double balance = resultSet.getDouble("balance");
                statement.close();
                connection.close();
                return balance;
            }
            statement.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
    public void setCreditsByUUID(String uuid, double balance) {
        String sql = "UPDATE " + this.table + " SET balance = ? WHERE uuid = ?";

        try {
            Connection connection = hikari.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setDouble(1, balance);
            statement.setString(2, uuid);
            statement.executeUpdate();
            statement.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void setCreditsByPlayerName(String player_name, double balance) {
        String sql = "UPDATE " + this.table + " SET balance = ? WHERE player_name = ?";

        try {
            Connection connection = hikari.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setDouble(1, balance);
            statement.setString(2, player_name);
            statement.executeUpdate();
            statement.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public double getCreditsByOrder(int pos) {
        String sql = "SELECT balance FROM " + this.table + " ORDER BY balance DESC LIMIT 1 OFFSET ?";

        try {
            Connection connection = hikari.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, pos);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                double balance = resultSet.getDouble("balance");
                statement.close();
                connection.close();
                return balance;
            }
            statement.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
    public String getPlayerByOrder(int pos) {
        String sql = "SELECT player_name FROM " + this.table + " ORDER BY balance DESC LIMIT 1 OFFSET ?";

        try {
            Connection connection = hikari.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, pos);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                String player_name = resultSet.getString("player_name");
                statement.close();
                connection.close();
                return player_name;
            }
            statement.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
