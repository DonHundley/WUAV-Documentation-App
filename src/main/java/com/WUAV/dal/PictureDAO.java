package com.WUAV.dal;

import com.WUAV.be.*;
import javafx.scene.image.*;

import java.io.*;
import java.sql.*;
import java.util.*;

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
            String sql = "INSERT INTO task_picture(after_picture, before_picture, after_comment, documentationID, before_comment) VALUES (?,?,?,?,?)";

            PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            File beforeInputFile=new File(taskPictures.getBeforeAbsolutePath());
            File afterInputFile=new File(taskPictures.getAfterAbsolutePath());


            FileInputStream inStreamAfter = new FileInputStream(afterInputFile);
            statement.setBinaryStream(1, inStreamAfter);
            FileInputStream inStreamBefore = new FileInputStream(beforeInputFile);
            statement.setBinaryStream(2, inStreamBefore);
            statement.setString(3, taskPictures.getAfterComment());
            statement.setInt(4, taskPictures.getDocID());
            statement.setString(5, taskPictures.getBeforeComment());
            statement.execute();
            inStreamAfter.close();
            inStreamBefore.close();

            ResultSet keys = statement.getGeneratedKeys();
            keys.next();
            int id = keys.getInt(1);

            return new TaskPictures(id, taskPictures.getAfterPicture(), taskPictures.getBeforePicture(), taskPictures.getAfterComment(), taskPictures.getDocID(), taskPictures.getBeforeComment());
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
            String sql = "UPDATE task_picture SET after_picture = ?, before_picture = ?, after_comment = ?, documentationID = ?, before_comment = ?) " + "WHERE pictureID = ?";

            PreparedStatement statement = connection.prepareStatement(sql);
            FileInputStream inStreamAfter = new FileInputStream(new File("taskPictures.getAfterPicture()"));
            statement.setBinaryStream(1, inStreamAfter);
            FileInputStream inStreamBefore = new FileInputStream(new File("taskPictures.getBeforePicture()"));
            statement.setBinaryStream(2, inStreamBefore);
            statement.setString(3, taskPictures.getAfterComment());
            statement.setInt(4, taskPictures.getDocID());
            statement.setString(5, taskPictures.getBeforeComment());
            statement.setInt(6, taskPictures.getPictureID());
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
                    String afterComment = resultSet.getString("after_comment");
                    int documentationID = resultSet.getInt("documentationID");
                    String beforeComment = resultSet.getString("before_comment");


                    TaskPictures taskPicture = new TaskPictures(id, tryingAfterPicture, tryingBeforePicture, afterComment, documentationID, beforeComment);
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
                    String afterComment = resultSet.getString("after_comment");
                    int documentationID = resultSet.getInt("documentationID");
                    String beforeComment = resultSet.getString("before_comment");

                    return new TaskPictures(id, tryingAfterPicture, tryingBeforePicture, afterComment, documentationID, beforeComment);
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
                    Image tryingAfterPicture = new Image(resultSet.getBinaryStream("after_picture"));
                    Image tryingBeforePicture = new Image(resultSet.getBinaryStream("before_picture"));
                    String afterComment = resultSet.getString("after_comment");
                    int documentationID = resultSet.getInt("documentationID");
                    String beforeComment = resultSet.getString("before_comment");

                    TaskPictures taskPictures = new TaskPictures(id, tryingAfterPicture, tryingBeforePicture, afterComment, documentationID, beforeComment);
                    taskPicturesByDocID.add(taskPictures);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return taskPicturesByDocID;
    }
}
