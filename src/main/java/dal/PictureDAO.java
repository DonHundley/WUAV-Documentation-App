package dal;

import be.Task;
import be.TaskPictures;
import javafx.scene.image.Image;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PictureDAO {

    private DatabaseConnector databaseConnector;

    public PictureDAO() {
        databaseConnector = new DatabaseConnector();
    }

    /**
     * Method to create picture in the database.
     */
    public TaskPictures createPicture(TaskPictures taskPictures) {
        try (Connection connection = databaseConnector.getConnection()) {
            String sql = "INSERT INTO task_picture(picture, device_name, documentationID, password) VALUES (?,?,?,?)";

            PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            File InputFile = new File(taskPictures.getPictureAbsolute());


            FileInputStream inStream = new FileInputStream(InputFile);
            statement.setBinaryStream(1, inStream);
            statement.setString(2, taskPictures.getDeviceName());
            statement.setInt(3, taskPictures.getDocID());
            statement.setString(4, taskPictures.getPassword());
            statement.execute();
            inStream.close();

            ResultSet keys = statement.getGeneratedKeys();
            keys.next();
            int id = keys.getInt(1);

            return new TaskPictures(id, taskPictures.getDocID(), taskPictures.getPictureAbsolute(), taskPictures.getDeviceName(), taskPictures.getPassword());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Method to update picture in the database.
     */
    public void updatePicture(TaskPictures taskPictures) {
        try (Connection connection = databaseConnector.getConnection()) {
            String sql = "UPDATE task_picture SET picture = ?, device_name = ?, documentationID = ?, password = ? " + "WHERE pictureID = ?";

            PreparedStatement statement = connection.prepareStatement(sql);
            FileInputStream inStream = new FileInputStream(new File("taskPictures.getAfterPicture()"));
            statement.setBinaryStream(1, inStream);
            statement.setString(2, taskPictures.getDeviceName());
            statement.setInt(3, taskPictures.getDocID());
            statement.setString(4, taskPictures.getPassword());
            statement.setInt(5, taskPictures.getPictureID());
            statement.executeUpdate();
            inStream.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Method to delete picture from database.
     */
    public void deletePicture(TaskPictures taskPictures) {
        try (Connection conn = databaseConnector.getConnection()) {
            String sql = "DELETE FROM task_picture WHERE pictureID= ?";

            PreparedStatement pstmt = conn.prepareStatement(sql);

            pstmt.setInt(1, taskPictures.getPictureID());
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Method to get all pictures from database.
     */
    public List<TaskPictures> getAllPictures() {
        ArrayList<TaskPictures> taskPictures = new ArrayList<>();

        try (Connection connection = databaseConnector.getConnection()) {
            String sql = "SELECT * FROM task_picture";
            Statement statement = connection.createStatement();

            if(statement.execute(sql)) {
                ResultSet resultSet = statement.getResultSet();

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
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return taskPictures;
    }

    /**
     * Method to get picture by ID from database.
     */
    public TaskPictures getPictureById(TaskPictures taskPictures) {
        try (Connection connection = databaseConnector.getConnection()) {
            String sql = "SELECT * FROM task_picture WHERE pictureID = ?";

            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setInt(1, taskPictures.getPictureID());

            if(pstmt.execute(sql)) {
                ResultSet resultSet = pstmt.getResultSet();

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
            throw new RuntimeException(e);
        }
        return null;
    }

    /**
     * Method to get picture by Document ID from database.
     */
    public List<TaskPictures> getPictureByDocumentID(Task task) {
        ArrayList<TaskPictures> taskPicturesByDocID = new ArrayList<>();
        try (Connection connection = databaseConnector.getConnection()) {
            String sql = "SELECT * FROM task_picture WHERE documentationID = ?";

            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setInt(1, task.getDocID());

            if(pstmt.execute()) {
                ResultSet resultSet = pstmt.getResultSet();

                while (resultSet.next()) {
                    int id = resultSet.getInt("pictureID");
                    Image picture = new Image(resultSet.getBinaryStream("picture"));
                    String deviceName = resultSet.getString("device_name");
                    int documentationID = resultSet.getInt("documentationID");
                    String password = resultSet.getString("password");

                    TaskPictures taskPictures = new TaskPictures(id, documentationID, deviceName, password, picture);
                    taskPicturesByDocID.add(taskPictures);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return taskPicturesByDocID;
    }
}
