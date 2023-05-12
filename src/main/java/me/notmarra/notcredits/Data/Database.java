package me.notmarra.notcredits.Data;

import me.notmarra.notcredits.Notcredits;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.sql.*;
import java.util.Objects;

public class Database {
    FileConfiguration config = Notcredits.main.getConfig();

    private static Database instance = null;
    private Connection connection = null;

    private Database() throws SQLException {

        if (Objects.equals(config.getString("data"), "MySQL")) {
            // load data from config
            String hostname = config.getString("mysql_hostname");
            String port = config.getString("mysql_port");
            String username = config.getString("mysql_username");
            String database = config.getString("mysql_database");
            String password = config.getString("mysql_password");

            // set information
            String url = config.getString("mysql_url")
                    .replace("%hostname%", hostname)
                    .replace("%port%", port)
                    .replace("%database%", database);

            this.connection = DriverManager.getConnection(url, username, password);

            Bukkit.getServer().getLogger().info("[NotCredits] Connected to the MySQL");
        }

        if (Objects.equals(config.getString("data"), "SQLite")) {
            // db file name and path
            String databaseName = config.getString("data_file");
            String databasePath = Notcredits.main.getDataFolder().getAbsolutePath() + File.separator + databaseName;

            // create connection
            this.connection = DriverManager.getConnection("jdbc:sqlite:" + databasePath);

            Bukkit.getServer().getLogger().info("[NotCredits] Connected to the SQLite");
        }
    }

    public static Database getInstance() throws SQLException {
        if (instance == null) {
            instance = new Database();
        }

        return instance;
    }

    public Connection getConnection() {
        return this.connection;
    }

    public void closeConnection() throws SQLException {
        if (this.connection != null) {
            this.connection.close();
        }
    }


    public void initializeDatabase() throws SQLException {
        String table = config.getString("table");

        DatabaseMetaData metaData = getConnection().getMetaData();
        ResultSet tables = metaData.getTables(null, null, table, null);

        if (!tables.next()) {
            String sql = "CREATE TABLE IF NOT EXISTS " + table + "(uuid varchar(36) primary key, player_name varchar(36), balance long)";
            PreparedStatement stmt = getConnection().prepareStatement(sql);
            stmt.execute();

            stmt.close();

            Bukkit.getServer().getLogger().info("[NotCredits] Database created successfully");
        }
    }

    public Data findPlayerByUUID(String uuid) throws SQLException {
        String table = config.getString("table");

        String sql = "SELECT * FROM " + table + " WHERE uuid = ?";
        PreparedStatement stmt = getConnection().prepareStatement(sql);
        stmt.setString(1, uuid);
        ResultSet results = stmt.executeQuery();

        if (results.next()) {
            String player_name = results.getString("player_name");
            long balance = results.getLong("balance");
            Data data = new Data(uuid, player_name, balance);
            stmt.close();
            return data;
        }

        stmt.close();
        return null;
    }

    public void addPlayerData(String uuid, String player_name, long balance) throws SQLException {
        String table = config.getString("table");

        if (findPlayerByUUID(uuid) != null) {
            return;
        }

        String sql = "INSERT INTO " + table + " (uuid, player_name, balance) VALUES (?, ?, ?)";
        PreparedStatement stmt = getConnection().prepareStatement(sql);
        stmt.setString(1, uuid);
        stmt.setString(2, player_name);
        stmt.setLong(3, balance);
        stmt.executeUpdate();

        stmt.close();
    }

    public long getCreditsByUUID(String uuid) throws SQLException {
        String table = config.getString("table");

        String sql = "SELECT balance FROM " + table + " WHERE uuid = ?";
        PreparedStatement stmt = getConnection().prepareStatement(sql);
        stmt.setString(1, uuid);
        ResultSet results = stmt.executeQuery();

        long credits = -1; // výchozí hodnota v případě, že neexistuje záznam pro dané UUID

        if (results.next()) {
            credits = results.getLong("balance");
        }

        stmt.close();
        return credits;
    }

    public void setCreditsByUUID(String uuid, long credits) throws SQLException {
        String table = config.getString("table");

        String sql = "UPDATE " + table + " SET balance = ? WHERE uuid = ?";
        PreparedStatement stmt = getConnection().prepareStatement(sql);
        stmt.setLong(1, credits);
        stmt.setString(2, uuid);
        stmt.executeUpdate();

        stmt.close();
    }

}
