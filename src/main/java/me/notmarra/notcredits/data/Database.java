package me.notmarra.notcredits.data;

import com.zaxxer.hikari.HikariDataSource;
import me.notmarra.notcredits.Notcredits;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.sql.*;

public class Database {
    FileConfiguration config;
    ConfigurationSection data = config.getConfigurationSection("data");
    private HikariDataSource hikari;


    private Database() throws SQLException {
        if (data.getString("type").equalsIgnoreCase("MySQL")) {
            connectDB();
            createTable();
            Bukkit.getServer().getLogger().info("[NotCredits] Connected to MySQL database.");
        } else if (!data.getString("type").equalsIgnoreCase("SQLite")) {
            connectSQLite();
            createTable();
            Bukkit.getServer().getLogger().info("[NotCredits] Connected to SQLite database.");
        }
    }

    public void connectDB() {
        hikari = new HikariDataSource();
        hikari.setDataSourceClassName("com.mysql.jdbc.jdbc2.optional.MysqlDataSource");
        hikari.addDataSourceProperty("serverName", config.getString("data.mysql.hostname"));
        hikari.addDataSourceProperty("port", config.getString("data.mysql.port"));
        hikari.addDataSourceProperty("databaseName", config.getString("data.mysql.database"));
        hikari.addDataSourceProperty("user", config.getString("data.mysql.username"));
        hikari.addDataSourceProperty("password", config.getString("data.mysql.password"));
    }

    public void connectSQLite() {
        String pluginFolder = Notcredits.getInstance().getDataFolder().getAbsolutePath();
        String databasePath = pluginFolder + File.separator + config.getString("data.sqlite.file");
        hikari = new HikariDataSource();
        hikari.setDataSourceClassName("org.sqlite.JDBC");
        hikari.addDataSourceProperty("url", "jdbc:sqlite:" + databasePath);
    }

    public void disconnectDB() {
        if (hikari != null) {
            hikari.close();
        }
    }

    public HikariDataSource getHikari() {
        return hikari;
    }

    public void createTable() {
        try(Connection connection = hikari.getConnection();
            Statement statement = connection.createStatement()) {
                statement.executeUpdate("CREATE TABLE IF NOT EXISTS " + config.getString("data.table") + " (`uuid` VARCHAR(36) NOT NULL, `name` VARCHAR(16) NOT NULL, `credits` DOUBLE NOT NULL, PRIMARY KEY (`uuid`))");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String findPlayerByUUID(String uuid) throws SQLException {
        String sql = "SELECT * FROM " + config.getString("data.table") + " WHERE uuid = '" + uuid + "'";
        PreparedStatement statement = hikari.getConnection().prepareStatement(sql);
        ResultSet result = statement.executeQuery();
        if (result.next()) {
            return result.getString("uuid");
        }
        return null;
    }

}
