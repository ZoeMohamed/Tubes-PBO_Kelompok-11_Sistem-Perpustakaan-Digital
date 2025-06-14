package com.tubes.utilities;

import java.sql.*;

public class MysqlUtilities {
    public static Connection getConnection() throws SQLException {
        Connection koneksi = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = "jdbc:mysql://localhost:3306/perpus"; // Correct database name
            String user = "root";
            String password = "root";
            koneksi = DriverManager.getConnection(url, user, password);
            if (koneksi != null) {
                System.out.println("koneksi berhasil");
            }
            return koneksi;
        } catch (ClassNotFoundException e) {
            System.err.println("Gagal load driver: " + e.getMessage());
            throw new SQLException("Failed to load database driver", e);
        } catch (SQLException e) {
            System.err.println("Gagal Koneksi: " + e.getMessage());
            throw e;
        }
    }
} 