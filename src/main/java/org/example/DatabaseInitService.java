package org.example;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DatabaseInitService {

    public static void main(String[] args) {
        DataBase database = DataBase.getInstance();
        Connection connection = database.getConnection();

        String filePath = "sql/init_db.sql";

        try {
            executeSqlFile(connection, filePath);
            System.out.println("Database successfully initialized.");
        } catch (SQLException e) {
            System.err.println("Error executing SQL queries: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("Error reading SQL file: " + e.getMessage());
        }
    }

    private static void executeSqlFile(Connection connection, String filePath) throws IOException, SQLException {
        StringBuilder sql = new StringBuilder();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                sql.append(line).append("\n");
            }
        }

        // Розділімо SQL запити, якщо їх декілька
        String[] sqlStatements = sql.toString().split(";"); // Розділяємо за крапкою з комою

        for (String statement : sqlStatements) {
            if (!statement.trim().isEmpty()) {
                try (PreparedStatement preparedStatement = connection.prepareStatement(statement.trim())) {
                    preparedStatement.execute();
                }
            }
        }
    }

}
