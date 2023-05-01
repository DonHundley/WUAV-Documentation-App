package dal;

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
            String sql = "INSERT INTO task_picture(after_picture, before_picture, picture_comment, documentationID) VALUES (?,?,?,?)";

            PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            FileInputStream inStreamAfter = new FileInputStream("taskPictures.getAfterPicture()");
            statement.setBinaryStream(1, inStreamAfter);
            FileInputStream inStreamBefore = new FileInputStream("taskPictures.getBeforePicture()");
            statement.setBinaryStream(2, inStreamBefore);
            statement.setString(3, taskPictures.getPictureComment());
            statement.setInt(4, taskPictures.getDocID());
            statement.execute();
            inStreamAfter.close();
            inStreamBefore.close();

            ResultSet keys = statement.getGeneratedKeys();
            keys.next();
            int id = keys.getInt(1);

            return new TaskPictures(id, taskPictures.getAfterPicture(), taskPictures.getBeforePicture(), taskPictures.getPictureComment(), taskPictures.getDocID());
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
            String sql = "UPDATE task_picture SET after_picture = ?, before_picture = ?, picture_comment = ?, documentationID = ?) " + "WHERE pictureID = ?";

            PreparedStatement statement = connection.prepareStatement(sql);
            FileInputStream inStreamAfter = new FileInputStream(new File("taskPictures.getAfterPicture()"));
            statement.setBinaryStream(1, inStreamAfter);
            FileInputStream inStreamBefore = new FileInputStream(new File("taskPictures.getBeforePicture()"));
            statement.setBinaryStream(2, inStreamBefore);
            statement.setString(3, taskPictures.getPictureComment());
            statement.setInt(4, taskPictures.getDocID());
            statement.setInt(5, taskPictures.getPictureID());
            statement.executeUpdate();
            inStreamAfter.close();
            inStreamBefore.close();

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
                    Image tryingAfterPicture = new Image(resultSet.getBinaryStream("after_picture"));
                    Image tryingBeforePicture = new Image(resultSet.getBinaryStream("before_picture"));
                    String pictureComment = resultSet.getString("picture_comment");
                    int documentationID = resultSet.getInt("documentationID");


                    TaskPictures taskPicture = new TaskPictures(id, tryingAfterPicture, tryingBeforePicture, pictureComment, documentationID);
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
                    Image tryingAfterPicture = new Image(resultSet.getBinaryStream("after_picture"));
                    Image tryingBeforePicture = new Image(resultSet.getBinaryStream("before_picture"));
                    String pictureComment = resultSet.getString("picture_comment");
                    int documentationID = resultSet.getInt("documentationID");

                    return new TaskPictures(id, tryingAfterPicture, tryingBeforePicture, pictureComment, documentationID);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
}
