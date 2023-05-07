package dal;

import be.*;
import javafx.scene.image.Image;

import java.io.*;
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
            String sql = "INSERT INTO task_documentation(projectID, task_name, description, task_state) VALUES (?,?,?,?)";

            PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            statement.setInt(1, task.getProjID());
            statement.setString(2, task.getTaskName());
            //FileInputStream inStreamLayout = new FileInputStream("task.getTaskLayout()");
            //statement.setBinaryStream(3, inStreamLayout);
            statement.setString(3, task.getTaskDesc());
            statement.setString(4, task.getTaskState());
            statement.execute();
           // inStreamLayout.close();

            ResultSet keys = statement.getGeneratedKeys();
            keys.next();
            int id = keys.getInt(1);

            return new Task(id, task.getProjID(), task.getTaskName(), task.getTaskLayout(), task.getTaskDesc(), task.getTaskState());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Method to update task in database (except the layout that is updated in updateLayout).
     */
    public void updateTask(Task task) {
        try (Connection connection = databaseConnector.getConnection()) {
            String sql = "UPDATE task_documentation SET projectID = ?, task_name = ?, description = ?, task_state = ? " + "WHERE documentationID = ?";

            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, task.getProjID());
            statement.setString(2, task.getTaskName());
            statement.setString(3, task.getTaskDesc());
            statement.setString(4, task.getTaskState());
            statement.setInt(5, task.getDocID());
            statement.executeUpdate();


        } catch (SQLException e) {
        throw new RuntimeException(e);
    }
    }

    /**
     * Method to update the layout in database
     * */

    public void updateLayout(Task task) {
        try(Connection connection = databaseConnector.getConnection()) {
            String sql = "UPDATE task_documentation SET layout = ? WHERE documentationID = ?";
            File layoutAbsolutePath = new File(task.getTaskLayoutAbsolute());

            PreparedStatement statement = connection.prepareStatement(sql);
            FileInputStream inStreamLayout = new FileInputStream(layoutAbsolutePath);
            statement.setBinaryStream(1, inStreamLayout);
            statement.setInt(2, task.getDocID());
            statement.executeUpdate();
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (FileNotFoundException e) {
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

            if (statement.execute(sql)) {
                ResultSet resultSet = statement.getResultSet();

                while (resultSet.next()) {
                    int id = resultSet.getInt("documentationID");
                    int projectID = resultSet.getInt("projectID");
                    String taskName = resultSet.getString("task_name");

                    byte[] layoutData = resultSet.getBytes("layout");
                    Image layout = null;
                    if (layoutData != null) {
                        layout = new Image(new ByteArrayInputStream(layoutData));
                    }

                    String description = resultSet.getString("description");
                    String taskState = resultSet.getString("task_state");

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

            if (pstmt.execute(sql)) {
                ResultSet resultSet = pstmt.getResultSet();

                while (resultSet.next()) {
                    int id = resultSet.getInt("documentationID");
                    int projectID = resultSet.getInt("projectID");
                    String taskName = resultSet.getString("task_name");

                    byte[] layoutData = resultSet.getBytes("layout");
                    Image layout = null;
                    if (layoutData != null) {
                        layout = new Image(new ByteArrayInputStream(layoutData));
                    }

                    String description = resultSet.getString("description");
                    String taskState = resultSet.getString("task_state");

                    return new Task(id, projectID, taskName, layout, description, taskState);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    /**
     * Method to get task by projectID from database.
     */
    public List<Task> getTaskByProject(int selectedProjectID) {
        ArrayList<Task> tasksByProject = new ArrayList<>();
        try (Connection connection = databaseConnector.getConnection()) {
            String sql = "SELECT * FROM task_documentation WHERE projectID = ?";

            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setInt(1, selectedProjectID);

            if (pstmt.execute()) {
                ResultSet resultSet = pstmt.getResultSet();

                while (resultSet.next()) {
                    int id = resultSet.getInt("documentationID");
                    int projectID = resultSet.getInt("projectID");
                    String taskName = resultSet.getString("task_name");

                    byte[] layoutData = resultSet.getBytes("layout");
                    Image layout = null;
                    if (layoutData != null) {
                        layout = new Image(new ByteArrayInputStream(layoutData));
                    }

                    String description = resultSet.getString("description");
                    String taskState = resultSet.getString("task_state");

                    Task task = new Task(id, projectID, taskName, layout, description, taskState);
                    tasksByProject.add(task);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return tasksByProject;
    }

    /**
     * Method to get tasks data together with project and customer (TaskWrapper)
     */

    public List<TaskWrapper> getTasksInfo() {
        List<TaskWrapper> taskInfo = new ArrayList<>();
        String sql = "SELECT t.*, p.*, c.* " +
                "FROM project p " +
                "JOIN task_documentation t ON p.projectID=t.projectID " +
                "JOIN customer c ON p.customerID=c.customerID ";

        try (Connection connection = databaseConnector.getConnection()) {
            PreparedStatement prepareStatement = connection.prepareStatement(sql);
            ResultSet resultSet = prepareStatement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("projectID");
                String projectName = resultSet.getString("project_name");
                Date dateCreated = resultSet.getDate("date_created");
                int customerID = resultSet.getInt("customerID");
                Project project = new Project(id, projectName, dateCreated, customerID);
                int documentationID = resultSet.getInt("documentationID");
                int projectID = resultSet.getInt("projectID");
                String taskName = resultSet.getString("task_name");

                byte[] layoutData = resultSet.getBytes("layout");
                Image layout = null;
                if (layoutData != null) {
                    layout = new Image(new ByteArrayInputStream(layoutData));
                }
                String description = resultSet.getString("description");
                String taskState = resultSet.getString("task_state");

                Task task = new Task(documentationID, projectID, taskName, layout, description, taskState);
                int custID = resultSet.getInt("customerID");
                String customerName = resultSet.getString("customer_name");
                String customerEmail = resultSet.getString("customer_email");
                String customerAddress = resultSet.getString("customer_address");

                Customer customer = new Customer(custID, customerName, customerEmail, customerAddress);
                TaskWrapper taskWrapper = new TaskWrapper(project, task, customer);
                taskInfo.add(taskWrapper);
            }

        return taskInfo;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }


}
