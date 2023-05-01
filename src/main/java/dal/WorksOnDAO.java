package dal;

import be.Project;
import be.TaskPictures;
import be.User;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.*;

public class WorksOnDAO {

    private DatabaseConnector databaseConnector;

    public WorksOnDAO() {
        databaseConnector = new DatabaseConnector();
    }

    /**
     * Method to create connection between user and project in database.
     */
    public void createWork(User user, Project project) {
        try (Connection connection = databaseConnector.getConnection()) {
            String sql = "INSERT INTO works_on(userID, projectID) VALUES (?,?)";

            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, user.getUserID());
            statement.setInt(2, project.getProjID());
            statement.execute();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Method to delete connection between user and project in database.
     */
    public void deleteWork(User user, Project project) {
        try (Connection conn = databaseConnector.getConnection()) {
            String sql = "DELETE FROM works_on WHERE userID= ? AND projectID = ? ";

            PreparedStatement pstmt = conn.prepareStatement(sql);

            pstmt.setInt(1, user.getUserID());
            pstmt.setInt(2, project.getProjID());
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
