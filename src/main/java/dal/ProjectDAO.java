package dal;


import be.Project;
import be.ProjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProjectDAO {
    private DatabaseConnector databaseConnector;

    public ProjectDAO() {
        databaseConnector = new DatabaseConnector();
    }

    /**
     * Method to create project in database.
     */
    public Project createProject(Project project) {
        try (Connection connection = databaseConnector.getConnection()) {
            String sql = "INSERT INTO project(project_name, date_created, customerID) VALUES (?,?,?)";

            PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, project.getProjName());
            statement.setDate(2, new java.sql.Date(project.getProjDate().getTime()));
            statement.setInt(3, project.getCustID());
            statement.execute();

            ResultSet keys = statement.getGeneratedKeys();
            keys.next();
            int id = keys.getInt(1);

            return new Project(id, project.getProjName(), project.getProjDate(), project.getCustID());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Method to update project in database.
     */
    public void updateProject(Project project) {
        try (Connection connection = databaseConnector.getConnection()) {
            String sql = "UPDATE project SET project_name = ?, date_created = ?, customerID = ?) " + "WHERE projectID = ?";

            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setString(1, project.getProjName());
            statement.setDate(2, new java.sql.Date(project.getProjDate().getTime()));
            statement.setInt(3, project.getCustID());
            statement.setInt(4, project.getProjID());
            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Method to delete project from database.
     */
    public void deleteProject(Project project) {
        try (Connection conn = databaseConnector.getConnection()) {
            String sql = "DELETE FROM project WHERE projectID = ?";

            PreparedStatement pstmt = conn.prepareStatement(sql);

            pstmt.setInt(1, project.getProjID());
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Method to get all projects from database.
     */
    public List<Project> getAllProjects() {
        ArrayList<Project> projects = new ArrayList<>();

        try (Connection connection = databaseConnector.getConnection()) {
            String sql = "SELECT * FROM project";
            Statement statement = connection.createStatement();

            if (statement.execute(sql)) {
                ResultSet resultSet = statement.getResultSet();

                while (resultSet.next()) {
                    int id = resultSet.getInt("projectID");
                    String projectName = resultSet.getString("project_name");
                    Date dateCreated = resultSet.getDate("date_created");
                    int customerID = resultSet.getInt("customerID");

                    Project project = new Project(id, projectName, dateCreated, customerID);
                    projects.add(project);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return projects;
    }

    /**
     * Method to get project by ID from database.
     */
    public Project getProjectById(Project project) {
        try (Connection connection = databaseConnector.getConnection()) {
            String sql = "SELECT * FROM project WHERE projectID = ?";

            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setInt(1, project.getProjID());

            if (pstmt.execute(sql)) {
                ResultSet resultSet = pstmt.getResultSet();

                while (resultSet.next()) {
                    int id = resultSet.getInt("projectID");
                    String projectName = resultSet.getString("project_name");
                    Date dateCreated = resultSet.getDate("date_created");
                    int customerID = resultSet.getInt("customerID");

                    return new Project(id, projectName, dateCreated, customerID);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    /**
     * Method to get all projects from database together with their associated tasks count.
     */
    public ObservableList<ProjectWrapper> getAllProjectsWithTaskCount() {
        ObservableList<ProjectWrapper> list = FXCollections.observableArrayList();

        String sql = "SELECT p.*, COUNT(t.documentationID) AS totalTasks " +
                "FROM project p " +
                "LEFT JOIN task_documentation t ON p.projectID = t.projectID " +
                "GROUP BY p.projectID, p.project_name, p.date_created, p.customerID";

        try (Connection connection = databaseConnector.getConnection()) {

            Statement statement = connection.createStatement();

            if (statement.execute(sql)) {
                ResultSet resultSet = statement.getResultSet();

                while (resultSet.next()) {
                    int id = resultSet.getInt("projectID");
                    String projectName = resultSet.getString("project_name");
                    Date dateCreated = resultSet.getDate("date_created");
                    int customerID = resultSet.getInt("customerID");
                    int taskNumber = resultSet.getInt("totalTasks");

                    Project project = new Project(id, projectName, dateCreated, customerID);
                    int totalTasks = taskNumber;
                    ProjectWrapper wrapper = new ProjectWrapper(project, totalTasks);
                    list.add(wrapper);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return list;
    }
}
