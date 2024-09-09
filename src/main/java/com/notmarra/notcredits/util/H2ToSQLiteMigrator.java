package com.notmarra.notcredits.util;

import com.notmarra.notcredits.Notcredits;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.util.*;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class H2ToSQLiteMigrator {

    private final Notcredits plugin;
    private final String sqliteFileName;
    private final String tableName;
    private final Logger logger;

    public H2ToSQLiteMigrator(Notcredits plugin) {
        this.plugin = plugin;
        FileConfiguration config = plugin.getConfig();
        this.sqliteFileName = config.getString("data.file") + ".db";
        this.tableName = config.getString("data.table");
        this.logger = plugin.getLogger();
    }

    public boolean migrateIfNeeded() {
        File h2File = new File(plugin.getDataFolder(), "data.h2.mv.db");
        if (h2File.exists()) {
            logger.info("H2 database file found. Starting migration process.");
            try {
                Map<String, PlayerData> playerDataMap = extractDataFromH2File(h2File);
                if (!playerDataMap.isEmpty()) {
                    migratePlayersToSQLite(playerDataMap.values());
                    logger.info("Migration to SQLite completed.");

                    File backupFile = new File(h2File.getAbsolutePath() + ".bak");
                    if (h2File.renameTo(backupFile)) {
                        logger.info("H2 database file renamed to: " + backupFile.getAbsolutePath());
                    } else {
                        logger.warning("Failed to rename H2 database file.");
                    }
                    return true;
                } else {
                    logger.warning("No player data found in H2 database.");
                }
            } catch (IOException | SQLException e) {
                logger.severe("Failed to migrate data to SQLite: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            logger.info("H2 database file not found. No migration necessary.");
        }
        return false;
    }

    private Map<String, PlayerData> extractDataFromH2File(File h2File) throws IOException {
        Map<String, PlayerData> playerDataMap = new HashMap<>();
        Pattern pattern = Pattern.compile("([0-9a-f-]{36}).*?([A-Za-z0-9_]{3,16}).*?(\\d+(?:\\.\\d+)?)");

        try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(h2File));
             ByteArrayOutputStream baos = new ByteArrayOutputStream()) {

            byte[] buffer = new byte[8192];
            int bytesRead;
            while ((bytesRead = bis.read(buffer)) != -1) {
                baos.write(buffer, 0, bytesRead);
            }

            String fileContent = baos.toString(StandardCharsets.UTF_8.name());
            Matcher matcher = pattern.matcher(fileContent);

            while (matcher.find()) {
                String uuid = matcher.group(1);
                String playerName = matcher.group(2);
                String balanceStr = matcher.group(3);

                logger.info("Raw extracted data - UUID: " + uuid + ", Name: " + playerName + ", Balance string: " + balanceStr);

                if (!"chunk".equals(playerName)) {
                    playerName = playerName.replace("LNotMarra", "NotMarra");
                    double balance = parseBalance(balanceStr);
                    PlayerData existingData = playerDataMap.get(uuid);
                    if (existingData == null || balance > existingData.balance) {
                        playerDataMap.put(uuid, new PlayerData(uuid, playerName, balance));
                    }
                }
            }
        }

        for (PlayerData player : playerDataMap.values()) {
            logger.info("Processed player: " + player.playerName + " with UUID: " + player.uuid + " and balance: " + player.balance);
        }
        logger.info("Extracted " + playerDataMap.size() + " unique player records from H2 file.");
        return playerDataMap;
    }

    private double parseBalance(String balanceStr) {
        try {
            // Pokus o parsování jako celé číslo
            long longValue = Long.parseLong(balanceStr);
            logger.info("Parsed balance as long: " + longValue);
            return (double) longValue;
        } catch (NumberFormatException e) {
            try {
                // Pokud se nepodaří jako celé číslo, zkusíme desetinné
                double doubleValue = Double.parseDouble(balanceStr);
                logger.info("Parsed balance as double: " + doubleValue);
                return doubleValue;
            } catch (NumberFormatException ex) {
                logger.warning("Failed to parse balance: " + balanceStr + ". Using 0.0 as default.");
                return 0.0;
            }
        }
    }

    private void migratePlayersToSQLite(Collection<PlayerData> players) throws SQLException {
        String sqliteUrl = "jdbc:sqlite:" + plugin.getDataFolder().getAbsolutePath() + File.separator + sqliteFileName;
        try (Connection conn = DriverManager.getConnection(sqliteUrl)) {
            conn.setAutoCommit(false);
            try (Statement stmt = conn.createStatement()) {
                stmt.execute("CREATE TABLE IF NOT EXISTS " + tableName + " (uuid varchar(36) primary key, player_name varchar(36), balance double)");
                logger.info("Created table " + tableName + " in SQLite database.");
            }

            String insertSql = "INSERT OR REPLACE INTO " + tableName + " (uuid, player_name, balance) VALUES (?, ?, ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(insertSql)) {
                for (PlayerData player : players) {
                    pstmt.setString(1, player.uuid);
                    pstmt.setString(2, player.playerName);
                    pstmt.setDouble(3, player.balance);
                    pstmt.executeUpdate();
                    logger.info("Migrated player: " + player.playerName + " with balance: " + player.balance);
                }
            }
            conn.commit();
        }
        logger.info("Finished migrating " + players.size() + " players to SQLite database.");
    }

    private static class PlayerData {
        String uuid;
        String playerName;
        double balance;

        PlayerData(String uuid, String playerName, double balance) {
            this.uuid = uuid;
            this.playerName = playerName;
            this.balance = balance;
        }
    }
}