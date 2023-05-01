package dal;

import be.Task;
import javafx.scene.image.Image;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TaskDAO {

    private DatabaseConnector databaseConnector;

    public TaskDAO() {
        databaseConnector = new DatabaseConnector();
    }

    /**
     * Method to create task in database.
     */
    public Task createTask(Task task) {
        try (Connection connection = databaseConnector.getConnection()) {
            String sql = "INSERT INTO task_documentation(projectID, task_name, layout, description, task_state) VALUES (?,?,?,?,?)";

            PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            statement.setInt(1, task.getProjID());
            statement.setString(2, task.getTaskName());
            FileInputStream inStreamLayout = new FileInputStream("task.getTaskLayout()");
            statement.setBinaryStream(3, inStreamLayout);
            statement.setString(4, task.getTaskDesc());
            statement.setInt(5, task.getTaskState());
            statement.execute();
            inStreamLayout.close();

            ResultSet keys = statement.getGeneratedKeys();
            keys.next();
            int id = keys.getInt(1);

            return new Task(id, task.getProjID(), task.getTaskName(), task.getTaskLayout(), task.getTaskDesc(), task.getTaskState());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Method to update task in database.
     */
    public void updateTask(Task task) {
        try (Connection connection = databaseConnector.getConnection()) {
            String sql = "UPDATE task_documentation SET projectID = ?, task_name = ?, layout = ?, description = ?, task_state = ?) " + "WHERE documentationID = ?";

            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setInt(1, task.getProjID());
            statement.setString(2, task.getTaskName());
            FileInputStream inStreamLayout = new FileInputStream("task.getTaskLayout()");
            statement.setBinaryStream(3, inStreamLayout);
            statement.setString(4, task.getTaskDesc());
            statement.setInt(5, task.getTaskState());
            statement.setInt(6, task.getDocID());
            statement.executeUpdate();
            inStreamLayout.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Method to delete task from database.
     */
    public void deleteTask(Task task) {
        try (Connection conn = databaseConnector.getConnection()) {
            String sql = "DELETE FROM task_documentation WHERE documentationID= ?";

            PreparedStatement pstmt = conn.prepareStatement(sql);

            pstmt.setInt(1, task.getDocID());
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Method to get all tasks from database.
     */
    public List<Task> getAllTasks() {
        ArrayList<Task> tasks = new ArrayList<>();

        try (Connection connection = databaseConnector.getConnection()) {
            String sql = "SELECT * FROM task_documentation";
            Statement statement = connection.createStatement();

            if(statement.execute(sql)) {
                ResultSet resultSet = statement.getResultSet();

                while (resultSet.next()) {
                    int id = resultSet.getInt("documentationID");
                    int projectID = resultSet.getInt("projectID");
                    String taskName = resultSet.getString("task_name");
                    Image layout = new Image(resultSet.getBinaryStream("layout"));
                    String description = resultSet.getString("description");
                    int taskState = resultSet.getInt("task_state");

                    Task task = new Task(id, projectID, taskName, layout, description, taskState);
                    tasks.add(task);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return tasks;
    }

    /**
     * Method to get task by ID from database.
     */
    public Task getTaskById(Task task) {
        try (Connection connection = databaseConnector.getConnection()) {
            String sql = "SELECT * FROM task_documentation WHERE documentationID = ?";

            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setInt(1, task.getDocID());

            if(pstmt.execute(sql)) {
                ResultSet resultSet = pstmt.getResultSet();

                while (resultSet.next()) {
                    int id = resultSet.getInt("documentationID");
                    int projectID = resultSet.getInt("projectID");
                    String taskName = resultSet.getString("task_name");
                    Image layout = new Image(resultSet.getBinaryStream("layout"));
                    String description = resultSet.getString("description");
                    int taskState = resultSet.getInt("task_state");

                    return new Task(id, projectID, taskName, layout, description, taskState);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
}
