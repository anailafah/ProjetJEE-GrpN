package com.assurance.util;
import com.mysql.cj.jdbc.AbandonedConnectionCleanupThread;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private static final String URL =
        "jdbc:mysql://localhost:3306/gestion_assurance";

    private static final String USER = "assurance_app";

    private static final String PASSWORD = "JEEgrp_N!1";

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("Pilote MySQL introuvable", e);
        }

        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
    public static void fermerPiloteMySQL() {
        try {
            AbandonedConnectionCleanupThread.checkedShutdown();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}