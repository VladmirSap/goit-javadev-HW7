package org.example;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        DatabaseInitService.main(args);
        DatabasePopulateService.main(args);

        DatabaseQueryService queryService = new DatabaseQueryService();
        try (Connection connection = DataBase.getInstance().getConnection()) {
            List<MaxProjectCountClient> maxProjectsClients = queryService.findMaxProjectsClient(connection);
            List<LongestProject> longestProjects = queryService.findLongestProject(connection);
            List<MaxSalaryWorker> maxSalaryWorkers = queryService.findMaxSalaryWorker(connection);
            List<YoungestEldestWorker> youngestElderWorkers = queryService.findYoungestEldestWorker(connection);

            for (MaxProjectCountClient client : maxProjectsClients) {
                System.out.println("Client: " + client.getName() +
                        ", Project Count: " + client.getProjectCount());
            }

            for (LongestProject project : longestProjects) {
                System.out.println("Project: " + project.getProjectName() +
                        ", Longest duration: " + project.getLongestDuration());
            }

            for (MaxSalaryWorker worker : maxSalaryWorkers) {
                System.out.println("Worker name - " + worker.getWorkerName() +
                        ", Max salary - " + worker.getSalary());
            }

            for (YoungestEldestWorker worker : youngestElderWorkers) {
                System.out.println("Type - " + worker.getType() + ", Worker name - " + worker.getWorkerName()
                        + ", Birthday - " + worker.getBirthday());
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}