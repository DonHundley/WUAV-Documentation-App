package dal;

import be.*;
import org.apache.logging.log4j.*;

import java.sql.*;
import java.util.*;

public class WorksOnDAO {

    private DatabaseConnector databaseConnector;

    public WorksOnDAO() {
        databaseConnector = new DatabaseConnector();
    }
    private static final Logger logger = LogManager.getLogger("debugLogger");

    /**
     * Method to create connection between user and project in database.
     */
    public void createWork(User user, Project project) {
        logger.info("Creating new connection in WorksOnDAO");
        try (Connection connection = databaseConnector.getConnection()) {
            String sql = "INSERT INTO works_on(userID, projectID) VALUES (?,?)";

            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, user.getUserID());
            statement.setInt(2, project.getProjID());
            logger.info("Executing " + sql);
            statement.execute();

        } catch (SQLException e) {
            logger.error("There has been a problem creating work for the user. CLASS: WorksOnDAO CAUSE: " + e);
        }
        logger.info("createWork process complete.");
    }

    /**
     * Method to delete connection between user and project in database.
     */
    public void deleteWork(User user, Project project) {
        logger.info("Opening connection in WorksOnDAO");
        try (Connection conn = databaseConnector.getConnection()) {
            String sql = "DELETE FROM works_on WHERE userID= ? AND projectID = ? ";

            PreparedStatement pstmt = conn.prepareStatement(sql);

            pstmt.setInt(1, user.getUserID());
            pstmt.setInt(2, project.getProjID());
            logger.info("Executing " + sql);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            logger.error("There has been a problem deleting work from the user.");
        }
        logger.info("The delete work process is complete.");
    }

    /**
     * Method to get all project IDs by userID.
     * Used to make a list of projects for a given technician.
     */
    public List<Integer> getProjectIDsByUserID(User user) {
        List<Integer> projectIDsByUserID = new ArrayList<>();
        logger.info("Opening connection in WorksOnDAO");
        try (Connection connection = databaseConnector.getConnection()) {
            String sql = "SELECT * FROM works_on WHERE userID = ?";

            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setInt(1, user.getUserID());

            logger.info("Executing " + sql);
            if (pstmt.execute()) {
                ResultSet resultSet = pstmt.executeQuery();
                {
                    logger.info("Iterating over results of " + sql);
                    while (resultSet.next()) {
                        int projectID = resultSet.getInt("projectID");
                        if (!projectIDsByUserID.contains(projectID)) {
                            projectIDsByUserID.add(projectID);
                        }
                    }

                }
            }
            if(projectIDsByUserID.isEmpty()){
                logger.warn("The method getProjectIDsByUserID in WorksOnDAO has returned an empty list!");
            }
        } catch (SQLException ex) {
            logger.error("There has been a problem creating a list of projects by user ID. CLASS: WorksOnDAO CAUSE: " + e);
        }
        logger.info("Returning list, process complete.");
        return projectIDsByUserID;
    }
}
