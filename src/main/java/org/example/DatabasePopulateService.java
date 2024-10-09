package org.example;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabasePopulateService {
    public static void main(String[] args) {

        DataBase database = DataBase.getInstance();
        Connection connection = database.getConnection();

        String filePath = "sql/populate_db.sql";

        try {
            executeSqlFile(connection, filePath);
            System.out.println("The database is successfully populated.");
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

        try (Statement statement = connection.createStatement()) {
            statement.execute(sql.toString());
        }
    }
}
