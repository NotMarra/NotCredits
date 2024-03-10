package com.notmarra.notcredits.util;

import com.notmarra.notcredits.Notcredits;

public class DatabaseManager {

    public void setupDB() {
        switch (Notcredits.getInstance().getConfig().getString("data.type")) {
            case "h2":
                connectDB("H2");
                break;
            case "mysql":
                connectDB("MySQL");
                break;
            case "sqlite":
                connectDB("SQLite");
                break;
            default:
                connectDB("H2");
                break;
        }
    }

    private void connectDB(String type) {
        switch (type) {
            case "H2":
                // connect to H2 database

                break;
            case "MySQL":
                // connect to MySQL database
                break;
            case "SQLite":
                // connect to SQLite database
                break;
            default:
                // connect to H2 database
                break;
        }
    }
}
