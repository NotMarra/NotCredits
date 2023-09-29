/* Decompiler 15ms, total 138ms, lines 201 */
package me.notmarra.notcredits.Data;

import java.io.File;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import me.notmarra.notcredits.Notcredits;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

public class Database {
   FileConfiguration config;
   ConfigurationSection data;
   ConfigurationSection mysql;
   String table;
   public static Database database = null;
   private Connection connection;

   private Database() throws SQLException {
      this.config = Notcredits.main.getConfig();
      this.data = this.config.getConfigurationSection("data");
      this.mysql = this.data.getConfigurationSection("mysql");
      this.table = this.data.getString("table");
      this.connection = null;
      String databaseName;
      String databasePath;
      if (this.data.getString("type").equalsIgnoreCase("MySQL")) {
         databaseName = this.mysql.getString("hostname");
         databasePath = this.mysql.getString("port");
         String username = this.mysql.getString("username");
         String database = this.mysql.getString("database");
         String password = this.mysql.getString("password");
         String url = this.mysql.getString("url").replace("%hostname%", databaseName).replace("%port%", databasePath).replace("%database%", database);
         this.connection = DriverManager.getConnection(url, username, password);
         Bukkit.getServer().getLogger().info("[NotCredits] Connected to the MySQL");
      }

      if (this.data.getString("type").equalsIgnoreCase("SQLite")) {
         databaseName = this.data.getString("file");
         String var10000 = Notcredits.main.getDataFolder().getAbsolutePath();
         databasePath = var10000 + File.separator + databaseName;
         this.connection = DriverManager.getConnection("jdbc:sqlite:" + databasePath);
         Bukkit.getServer().getLogger().info("[NotCredits] Connected to the SQLite");
      }

   }

   public static Database getInstance() throws SQLException {
      if (database == null) {
         database = new Database();
      }

      return database;
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
      String table = this.data.getString("table");
      DatabaseMetaData metaData = this.getConnection().getMetaData();
      ResultSet tables = metaData.getTables((String)null, (String)null, table, (String[])null);
      if (!tables.next()) {
         String sql = "CREATE TABLE IF NOT EXISTS " + table + "(uuid varchar(36) primary key, player_name varchar(36), balance double)";
         PreparedStatement stmt = this.getConnection().prepareStatement(sql);
         stmt.execute();
         stmt.close();
         Bukkit.getServer().getLogger().info("[NotCredits] Database created successfully");
      }

   }

   public String findPlayerByUUID(String uuid) throws SQLException {
      String sql = "SELECT * FROM " + this.table + " WHERE uuid = ?";
      PreparedStatement stmt = this.getConnection().prepareStatement(sql);
      stmt.setString(1, uuid);
      ResultSet results = stmt.executeQuery();
      if (results.next()) {
         String dataString = "UUID: " + uuid;
         stmt.close();
         return dataString;
      } else {
         stmt.close();
         return null;
      }
   }

   public String findPlayerByPlayerName(String playerName) throws SQLException {
      String sql = "SELECT * FROM " + this.table + " WHERE player_name = ?";
      PreparedStatement stmt = this.getConnection().prepareStatement(sql);
      stmt.setString(1, playerName);
      ResultSet results = stmt.executeQuery();
      if (results.next()) {
         String dataString = "Player Name: " + playerName;
         stmt.close();
         return dataString;
      } else {
         stmt.close();
         return null;
      }
   }

   public void addPlayerData(String uuid, String player_name, long balance) throws SQLException {
      if (this.findPlayerByUUID(uuid) == null) {
         String sql = "INSERT INTO " + this.table + " (uuid, player_name, balance) VALUES (?, ?, ?)";
         PreparedStatement stmt = this.getConnection().prepareStatement(sql);
         stmt.setString(1, uuid);
         stmt.setString(2, player_name);
         stmt.setDouble(3, (double)balance);
         stmt.executeUpdate();
         stmt.close();
      }
   }

   public double getCreditsByUUID(String uuid) throws SQLException {
      String sql = "SELECT balance FROM " + this.table + " WHERE uuid = ?";
      PreparedStatement stmt = this.getConnection().prepareStatement(sql);
      stmt.setString(1, uuid);
      ResultSet results = stmt.executeQuery();
      double credits = -1.0D;
      if (results.next()) {
         credits = results.getDouble("balance");
      }

      stmt.close();
      return credits;
   }

   public double getCreditsByPlayerName(String playerName) throws SQLException {
      String sql = "SELECT balance FROM " + this.table + " WHERE player_name = ?";
      PreparedStatement stmt = this.getConnection().prepareStatement(sql);
      stmt.setString(1, playerName);
      ResultSet results = stmt.executeQuery();
      double credits = -1.0D;
      if (results.next()) {
         credits = results.getDouble("balance");
      }

      stmt.close();
      return credits;
   }

   public void setCreditsByUUID(String uuid, double credits) throws SQLException {
      String sql = "UPDATE " + this.table + " SET balance = ? WHERE uuid = ?";
      PreparedStatement stmt = this.getConnection().prepareStatement(sql);
      stmt.setDouble(1, credits);
      stmt.setString(2, uuid);
      stmt.executeUpdate();
      stmt.close();
   }

   public void setCreditsByPlayerName(String playerName, double credits) throws SQLException {
      String sql = "UPDATE " + this.table + " SET balance = ? WHERE player_name = ?";
      PreparedStatement stmt = this.getConnection().prepareStatement(sql);
      stmt.setDouble(1, credits);
      stmt.setString(2, playerName);
      stmt.executeUpdate();
      stmt.close();
   }

   public double getCreditsByOrder(int pos) throws SQLException {
      String sql = "SELECT balance FROM " + this.table + " ORDER BY balance DESC LIMIT 1 OFFSET ?";
      PreparedStatement stmt = this.getConnection().prepareStatement(sql);
      stmt.setInt(1, pos - 1);
      ResultSet results = stmt.executeQuery();
      double credits = -1.0D;
      if (results.next()) {
         credits = results.getDouble("balance");
      }

      stmt.close();
      return credits;
   }

   public String getPlayerByOrder(int pos) throws SQLException {
      String sql = "SELECT player_name FROM " + this.table + " ORDER BY balance DESC LIMIT 1 OFFSET ?";
      PreparedStatement stmt = this.getConnection().prepareStatement(sql);
      stmt.setInt(1, pos - 1);
      ResultSet results = stmt.executeQuery();
      String player = "NaN";
      if (results.next()) {
         player = results.getString("player_name");
      }

      stmt.close();
      return player;
   }
}
