package com.tubes.utilities;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.Statement;

public class DatabaseInitializer {
    public static void initializeDatabase() {
        try {
            Connection connection = MysqlUtilities.getConnection();
            Statement statement = connection.createStatement();

            // Read and execute the SQL script
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(DatabaseInitializer.class.getResourceAsStream("/com/tubes/database.sql")));

            StringBuilder sql = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                // Skip comments and empty lines
                if (line.trim().startsWith("--") || line.trim().isEmpty()) {
                    continue;
                }
                sql.append(line);
                if (line.trim().endsWith(";")) {
                    statement.execute(sql.toString());
                    sql.setLength(0);
                }
            }

            statement.close();
            connection.close();
            System.out.println("Database initialized successfully");
        } catch (Exception e) {
            System.err.println("Error initializing database: " + e.getMessage());
            e.printStackTrace();
        }
    }
}