package org.example;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class DatabaseQueryService {

    private String readSqlFile(String filePath) throws IOException {
        StringBuilder sql = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                sql.append(line).append("\n");
            }
        }
        return sql.toString();
    }

    public List<MaxProjectCountClient> findMaxProjectsClient(Connection connection) {
        String sqlFilePath = "sql/find_max_projects_client.sql";
        return executeQuery(connection, sqlFilePath, resultSet -> {
            try {
                String name = resultSet.getString("name");
                int projectCount = resultSet.getInt("project_count");
                return new MaxProjectCountClient(name, projectCount);
            } catch (SQLException e) {
                e.printStackTrace();
                return null;
            }
        });
    }

    public List<LongestProject> findLongestProject(Connection connection) {
        String sqlFilePath = "sql/find_longest_project.sql";
        return executeQuery(connection, sqlFilePath, resultSet -> {
            try {
                String projectName = resultSet.getString("id");
                int longestDuration = resultSet.getInt("month_count");
                return new LongestProject(projectName, longestDuration);
            } catch (SQLException e) {
                e.printStackTrace();
                return null;
            }
        });
    }

    public List<MaxSalaryWorker> findMaxSalaryWorker(Connection connection) {
        String sqlFilePath = "sql/find_max_salary_worker.sql";
        return executeQuery(connection, sqlFilePath, resultSet -> {
            try {
                String workerName = resultSet.getString("name");
                int salary = resultSet.getInt("salary");
                return new MaxSalaryWorker(workerName, salary);
            } catch (SQLException e) {
                e.printStackTrace();
                return null;
            }
        });
    }

    public List<YoungestEldestWorker> findYoungestEldestWorker(Connection connection) {
        String sqlFilePath = "sql/find_youngest_eldest_workers.sql";
        return executeQuery(connection, sqlFilePath, resultSet -> {
            try {
                String type = resultSet.getString("type");
                String workerName = resultSet.getString("name");
                LocalDate birthday = LocalDate.parse(resultSet.getString("birthday"));
                return new YoungestEldestWorker(type, workerName, birthday);
            } catch (SQLException e) {
                e.printStackTrace();
                return null;
            }
        });
    }



    private <T> List<T> executeQuery(Connection connection, String sqlFilePath, Function<ResultSet, T> mapper) {
        List<T> result = new ArrayList<>();
        try (Statement statement = connection.createStatement()) {

            String sql = readSqlFile(sqlFilePath);
            try (ResultSet resultSet = statement.executeQuery(sql)) {
                while (resultSet.next()) {
                    T mappedResult = mapper.apply(resultSet);
                    if (mappedResult != null) {
                        result.add(mappedResult);
                    }
                }
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}
