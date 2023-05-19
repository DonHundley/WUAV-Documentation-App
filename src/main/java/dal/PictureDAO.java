package dal;

import be.Task;
import be.TaskPictures;
import javafx.scene.image.Image;
import org.apache.logging.log4j.*;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PictureDAO {

    private DatabaseConnector databaseConnector;

    public PictureDAO() {
        databaseConnector = new DatabaseConnector();
    }
    private static final Logger logger = LogManager.getLogger("debugLogger");


    /**
     * Method to create picture in the database.
     */
    public TaskPictures createPicture(TaskPictures taskPictures) {
        int id = 0;
        logger.info("Opening connection in createPicture()");
        try (Connection connection = databaseConnector.getConnection()) {
            String sql = "INSERT INTO task_picture(picture, device_name, documentationID, password) VALUES (?,?,?,?)";

            PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            File InputFile = new File(taskPictures.getPictureAbsolute());


            FileInputStream inStream = new FileInputStream(InputFile);
            statement.setBinaryStream(1, inStream);
            statement.setString(2, taskPictures.getDeviceName());
            statement.setInt(3, taskPictures.getDocID());
            statement.setString(4, taskPictures.getPassword());
            logger.info("Executing " + sql);
            statement.execute();
            inStream.close();

            ResultSet keys = statement.getGeneratedKeys();
            keys.next();
            id = keys.getInt(1);
            if(id == 0){
                logger.warn("The ID for TaskPictures was not set correctly");
            }
        } catch (SQLException | IOException e) {
            logger.error("There has been an issue adding TaskPictures to the database.", e);
        }
        logger.info("Returning new TaskPictures");
        return new TaskPictures(id, taskPictures.getDocID(), taskPictures.getPictureAbsolute(), taskPictures.getDeviceName(), taskPictures.getPassword());
    }

    /**
     * Method to update picture in the database.
     */
    public void updatePicture(TaskPictures taskPictures) {
        logger.info("Opening new connection in PictureDAO");
        try (Connection connection = databaseConnector.getConnection()) {
            String sql = "UPDATE task_picture SET picture = ?, device_name = ?, documentationID = ?, password = ? " + "WHERE pictureID = ?";

            PreparedStatement statement = connection.prepareStatement(sql);
            FileInputStream inStream = new FileInputStream(new File("taskPictures.getAfterPicture()"));
            statement.setBinaryStream(1, inStream);
            statement.setString(2, taskPictures.getDeviceName());
            statement.setInt(3, taskPictures.getDocID());
            statement.setString(4, taskPictures.getPassword());
            statement.setInt(5, taskPictures.getPictureID());
            logger.info("Executing " + sql);
            statement.executeUpdate();
            inStream.close();

        } catch (SQLException | IOException e) {
            logger.error("There was an issue updating TaskPictures.", e);
        }
        logger.info("Pictures update complete.");
    }

    /**
     * Method to delete picture from database.
     */
    public void deletePicture(TaskPictures taskPictures) {
        logger.info("Opening new connection in PictureDAO");
        try (Connection conn = databaseConnector.getConnection()) {
            String sql = "DELETE FROM task_picture WHERE pictureID= ?";

            PreparedStatement pstmt = conn.prepareStatement(sql);

            pstmt.setInt(1, taskPictures.getPictureID());
            logger.info("Executing " + sql);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            logger.error("There has been a problem deleting pictures." , e);
        }
        logger.info("Picture deletion complete.");
    }

    /**
     * Method to get all pictures from database.
     */
    public List<TaskPictures> getAllPictures() {
        ArrayList<TaskPictures> taskPictures = new ArrayList<>();
        logger.info("Opening new connection in PictureDAO.");
        try (Connection connection = databaseConnector.getConnection()) {
            String sql = "SELECT * FROM task_picture";
            Statement statement = connection.createStatement();

            logger.info("Executing " + sql);
            if(statement.execute(sql)) {
                ResultSet resultSet = statement.getResultSet();
                logger.info("Iterating over results of " + sql);
                while (resultSet.next()) {
                    int id = resultSet.getInt("pictureID");
                    Image picture = new Image(resultSet.getBinaryStream("picture"));
                    String deviceName = resultSet.getString("device_name");
                    int documentationID = resultSet.getInt("documentationID");
                    String password = resultSet.getString("password");


                    TaskPictures taskPicture = new TaskPictures(id, documentationID, deviceName, password, picture);
                    taskPictures.add(taskPicture);
                }
            }
            if(taskPictures.isEmpty()){
                logger.warn("The list taskPictures is empty!");
            }
        } catch (SQLException e) {
            logger.error("There has been an error creating the list taskPictures." , e);
        }
        logger.info("Returning list.");
        return taskPictures;
    }

    /**
     * Method to get picture by ID from database.
     */
    public TaskPictures getPictureById(TaskPictures taskPictures) {
        logger.info("Opening new connection in PictureDAO");

        try (Connection connection = databaseConnector.getConnection()) {
            String sql = "SELECT * FROM task_picture WHERE pictureID = ?";

            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setInt(1, taskPictures.getPictureID());
            logger.info("Executing statement " + sql);
            if(pstmt.execute(sql)) {
                ResultSet resultSet = pstmt.getResultSet();
                logger.info("Iterating over results of " + sql);
                while (resultSet.next()) {
                    int id = resultSet.getInt("pictureID");
                    Image picture = new Image(resultSet.getBinaryStream("picture"));
                    String deviceName = resultSet.getString("device_name");
                    int documentationID = resultSet.getInt("documentationID");
                    String password = resultSet.getString("password");

                    return new TaskPictures(id, documentationID, deviceName, password, picture);
                }
            }
        } catch (SQLException e) {
            logger.error("There has been a problem getting picture by ID.", e);
        }
        logger.info("Process complete.");
        return null;
    }

    /**
     * Method to get picture by Document ID from database.
     */
    public List<TaskPictures> getPictureByDocumentID(Task task) {
        ArrayList<TaskPictures> taskPicturesByDocID = new ArrayList<>();
        logger.info("Opening new connection in PictureDAO");
        try (Connection connection = databaseConnector.getConnection()) {
            String sql = "SELECT * FROM task_picture WHERE documentationID = ?";

            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setInt(1, task.getDocID());
            logger.info("Executing " + sql);
            if(pstmt.execute()) {
                ResultSet resultSet = pstmt.getResultSet();
                logger.info("Iterating over results");
                while (resultSet.next()) {
                    int id = resultSet.getInt("pictureID");
                    Image picture = new Image(resultSet.getBinaryStream("picture"));
                    String deviceName = resultSet.getString("device_name");
                    int documentationID = resultSet.getInt("documentationID");
                    String password = resultSet.getString("password");

                    TaskPictures taskPictures = new TaskPictures(id, documentationID, deviceName, password, picture);
                    taskPicturesByDocID.add(taskPictures);
                }
                if(taskPicturesByDocID.isEmpty()){
                    logger.warn("The list is empty!");
                }
            }
        } catch (SQLException e) {
            logger.error("There has been a problem creating a list of pictures by document ID." , e);
        }
        logger.info("Returning list.");
        return taskPicturesByDocID;
    }
}
