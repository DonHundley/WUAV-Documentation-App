package dal;


import be.Project;
import be.ProjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.apache.logging.log4j.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProjectDAO {
    private DatabaseConnector databaseConnector;

    public ProjectDAO() {
        databaseConnector = new DatabaseConnector();
    }
    private static final Logger logger = LogManager.getLogger("debugLogger");

    /**
     * Method to create project in database.
     */
    public Project createProject(Project project) {
        logger.info("Opening new connection in ProjectDAO");
        int id = 0;
        try (Connection connection = databaseConnector.getConnection()) {
            String sql = "INSERT INTO project(project_name, date_created, customerID) VALUES (?,?,?)";

            PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, project.getProjName());
            statement.setDate(2, new java.sql.Date(project.getProjDate().getTime()));
            statement.setInt(3, project.getCustID());
            logger.info("Executing " + sql);
            statement.execute();

            ResultSet keys = statement.getGeneratedKeys();
            keys.next();
            id = keys.getInt(1);
            if(id == 0){
                logger.warn("There was an issue with the ID of the project.");
            }

        } catch (SQLException e) {
            logger.error("There has been a problem creating the project.",e);
        }
        logger.info("Returning the new Project.");
        return new Project(id, project.getProjName(), project.getProjDate(), project.getCustID());
    }

    /**
     * Method to update project in database.
     */
    public void updateProject(Project project) {
        logger.info("Opening a new connection in ProjectDAO");
        try (Connection connection = databaseConnector.getConnection()) {
            String sql = "UPDATE project SET project_name = ? " + "WHERE projectID = ?";

            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setString(1, project.getProjName());
            statement.setInt(2, project.getProjID());
            logger.info("Executing " + sql);
            statement.executeUpdate();

        } catch (SQLException e) {
            logger.error("There has been a problem updating the project.", e );
        }
        logger.info("Update project process finished.");
    }

    /**
     * Method to delete project from database.
     */
    public void deleteProject(Project project) {
        logger.info("Opening new connection in ProjectDAO");
        try (Connection conn = databaseConnector.getConnection()) {
            String sql = "DELETE FROM project WHERE projectID = ?";

            PreparedStatement pstmt = conn.prepareStatement(sql);

            pstmt.setInt(1, project.getProjID());
            logger.info("Executing " + sql);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            logger.error("There has been a problem deleting the project.", e);
        }
        logger.info("delete project process complete.");
    }

    /**
     * Method to get all projects from database.
     */
    public List<Project> getAllProjects() {
        ArrayList<Project> projects = new ArrayList<>();
        logger.info("Opening new connection in ProjectDAO.");
        try (Connection connection = databaseConnector.getConnection()) {
            String sql = "SELECT * FROM project";
            Statement statement = connection.createStatement();
            logger.info("Executing " + sql);
            if (statement.execute(sql)) {
                ResultSet resultSet = statement.getResultSet();
                logger.info("Iterating over results");
                while (resultSet.next()) {
                    int id = resultSet.getInt("projectID");
                    String projectName = resultSet.getString("project_name");
                    Date dateCreated = resultSet.getDate("date_created");
                    int customerID = resultSet.getInt("customerID");

                    Project project = new Project(id, projectName, dateCreated, customerID);
                    projects.add(project);
                }
                if(projects.isEmpty()){
                    logger.warn("The list of projects is empty!");
                }
            }
        } catch (SQLException e) {
            logger.error("There has been a problem creating a list of projects.",e);
        }
        logger.info("Returning list of projects.");
        return projects;
    }

    /**
     * Method to get project by ID from database.
     */
    public Project getProjectById(Project project) {
        logger.info("Creating new connection in ProjectDAO");
        try (Connection connection = databaseConnector.getConnection()) {
            String sql = "SELECT * FROM project WHERE projectID = ?";

            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setInt(1, project.getProjID());
            logger.info("Executing " + sql);
            if (pstmt.execute(sql)) {
                ResultSet resultSet = pstmt.getResultSet();
                logger.info("Iterating over results");
                while (resultSet.next()) {
                    int id = resultSet.getInt("projectID");
                    String projectName = resultSet.getString("project_name");
                    Date dateCreated = resultSet.getDate("date_created");
                    int customerID = resultSet.getInt("customerID");

                    return new Project(id, projectName, dateCreated, customerID);
                }
            }
        } catch (SQLException e) {
            logger.error("There has been a problem getting project by ID.", e);
        }
        logger.info("Get project by ID process finished.");
        return null;
    }

    /**
     * Method to get all projects from database together with their associated tasks count (ProjectWrapper).
     */
    public ObservableList<ProjectWrapper> getAllProjectsWithTaskCount() {
        ObservableList<ProjectWrapper> list = FXCollections.observableArrayList();

        logger.info("Opening connection in ProjectDAO");
        try (Connection connection = databaseConnector.getConnection()) {
            String sql = "SELECT p.*, COUNT(t.documentationID) AS totalTasks " +
                    "FROM project p " +
                    "LEFT JOIN task_documentation t ON p.projectID = t.projectID " +
                    "GROUP BY p.projectID, p.project_name, p.date_created, p.customerID";

            Statement statement = connection.createStatement();
            logger.info("Executing " + sql);
            if (statement.execute(sql)) {
                ResultSet resultSet = statement.getResultSet();
                logger.info("Iterating over results");
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
                if(list.isEmpty()){
                    logger.warn("The list of projects with task count is empty!");
                }
            }
        } catch (SQLException e) {
            logger.error("There has been a problem creating the list of projects with task count.",e);
        }
        logger.info("Returning list");
        return list;
    }
}
