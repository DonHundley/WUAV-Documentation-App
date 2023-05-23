package dal;

import be.*;
import javafx.scene.image.Image;
import org.apache.logging.log4j.*;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TaskDAO {

    private DatabaseConnector databaseConnector;

    public TaskDAO() {
        databaseConnector = new DatabaseConnector();
    }

    private static final Logger logger = LogManager.getLogger("debugLogger");

    /**
     * Method to create task in database.
     */
    public Task createTask(Task task) {
        logger.info("Opening connection in TaskDAO");
        int id = 0;
        try (Connection connection = databaseConnector.getConnection()) {
            String sql = "INSERT INTO task_documentation(projectID, task_name, description, task_state) VALUES (?,?,?,?)";

            PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            statement.setInt(1, task.getProjID());
            statement.setString(2, task.getTaskName());
            //FileInputStream inStreamLayout = new FileInputStream("task.getTaskLayout()");
            //statement.setBinaryStream(3, inStreamLayout);
            statement.setString(3, task.getTaskDesc());
            statement.setString(4, task.getTaskState());
            logger.info("Executing " + sql);
            statement.execute();
           // inStreamLayout.close();

            ResultSet keys = statement.getGeneratedKeys();
            keys.next();
            id = keys.getInt(1);
            if(id == 0){
                logger.warn("The task has an improper ID");
            }

        } catch (SQLException e) {
            logger.error("There has been a problem creating the task. CLASS: TaskDAO CAUSE: " + e);
        }
        logger.info("Returning Task, process complete.");
        return new Task(id, task.getProjID(), task.getTaskName(), task.getTaskLayout(), task.getTaskDesc(), task.getTaskState());
    }

    /**
     * Method to update task in database (except the layout that is updated in updateLayout).
     */
    public void updateTask(Task task) {
        logger.info("Opening connection in TaskDAO");
        try (Connection connection = databaseConnector.getConnection()) {
            String sql = "UPDATE task_documentation SET projectID = ?, task_name = ?, description = ?, task_state = ? " + "WHERE documentationID = ?";

            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, task.getProjID());
            statement.setString(2, task.getTaskName());
            statement.setString(3, task.getTaskDesc());
            statement.setString(4, task.getTaskState());
            statement.setInt(5, task.getDocID());
            logger.info("Executing " + sql);
            statement.executeUpdate();


        } catch (SQLException e) {
            logger.error("There has been a problem updating the task. CLASS: TaskDAO CAUSE: " + e);
        }
        logger.info("Update task complete.");
    }

    /**
     * Method to update the layout in database
     * */

    public void updateLayout(Task task) {
        logger.info("Opening new connection in TaskDAO");
        try(Connection connection = databaseConnector.getConnection()) {
            String sql = "UPDATE task_documentation SET layout = ? WHERE documentationID = ?";
            File layoutAbsolutePath = new File(task.getTaskLayoutAbsolute());

            PreparedStatement statement = connection.prepareStatement(sql);
            FileInputStream inStreamLayout = new FileInputStream(layoutAbsolutePath);
            statement.setBinaryStream(1, inStreamLayout);
            statement.setInt(2, task.getDocID());
            logger.info("Executing " + sql);
            statement.executeUpdate();
        }
        catch (SQLException | FileNotFoundException e) {
            logger.error("There has been a problem updating the layout. CLASS: TaskDAO CAUSE: " + e);
        }
        logger.info("Process complete.");
    }

    /**
     * Method to delete task from database.
     */
    public void deleteTask(Task task) {
        logger.info("Opening new connection in TaskDAO");
        try (Connection conn = databaseConnector.getConnection()) {
            String sql = "DELETE FROM task_documentation WHERE documentationID= ?";

            PreparedStatement pstmt = conn.prepareStatement(sql);

            pstmt.setInt(1, task.getDocID());
            logger.info("Executing " + sql);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            logger.error("There has been a problem deleting the task. CLASS: TaskDAO CAUSE: " + e);
        }
        logger.info("delete task process finished.");
    }

    /**
     * Method to get all tasks from database.
     */
    public List<Task> getAllTasks() {
        ArrayList<Task> tasks = new ArrayList<>();
        logger.info("Opening new connection in TaskDAO");
        try (Connection connection = databaseConnector.getConnection()) {
            String sql = "SELECT * FROM task_documentation";
            Statement statement = connection.createStatement();
            logger.info("Executing " + sql);
            if (statement.execute(sql)) {
                ResultSet resultSet = statement.getResultSet();
                logger.info("Iterating over results of " + sql);
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
                if(tasks.isEmpty()){
                    logger.warn("The list of tasks is empty!");
                }
            }
        } catch (SQLException e) {
            logger.error("There was problem creating a list of tasks. CLASS: TaskDAO CAUSE: " + e);
        }
        logger.info("Returning list of tasks.");
        return tasks;
    }

    /**
     * Method to get task by ID from database.
     */
    public Task getTaskById(Task task) {
        logger.info("Opening connection in TaskDAO");

        try (Connection connection = databaseConnector.getConnection()) {
            String sql = "SELECT * FROM task_documentation WHERE documentationID = ?";

            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setInt(1, task.getDocID());
            logger.info("Executing " + sql);
            if (pstmt.execute(sql)) {
                ResultSet resultSet = pstmt.getResultSet();
                logger.info("Iterating over results");
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
                    logger.info("Returning task");
                    return new Task(id, projectID, taskName, layout, description, taskState);
                }
            }
        } catch (SQLException e) {
            logger.error("There was problem getting task by ID. CLASS: TaskDAO CAUSE: " + e);
        }
        logger.info("get task by ID process finished.");
        return null;
    }

    /**
     * Method to get task by projectID from database.
     */
    public List<Task> getTaskByProject(int selectedProjectID) {
        ArrayList<Task> tasksByProject = new ArrayList<>();
        logger.info("Opening new connection in TaskDAO");
        try (Connection connection = databaseConnector.getConnection()) {
            String sql = "SELECT * FROM task_documentation WHERE projectID = ?";

            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setInt(1, selectedProjectID);
            logger.info("Executing " + sql);
            if (pstmt.execute()) {
                ResultSet resultSet = pstmt.getResultSet();
                logger.info("Iterating over results of " + sql);
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
                if(tasksByProject.isEmpty()){
                    logger.warn("The list tasksByProject is empty!");
                }
            }
        } catch (SQLException e) {
            logger.error("There was a problem creating the list tasksByProject. CLASS: TaskDAO CAUSE: " +e);
        }
        logger.info("Returning list tasksByProject");
        return tasksByProject;
    }

    /**
     * Method to get tasks data together with project and customer (TaskWrapper)
     */
    public List<TaskWrapper> getTasksInfo() {
        List<TaskWrapper> taskInfo = new ArrayList<>();
        String sql = "SELECT t.*, p.*, c.*, pc.* " +
                "FROM project p " +
                "JOIN task_documentation t ON p.projectID=t.projectID " +
                "JOIN customer c ON p.customerID=c.customerID " +
                "LEFT JOIN postal_code pc ON c.postal_code = pc.postal_code";

        logger.info("Opening connection in TaskDAO");
        try (Connection connection = databaseConnector.getConnection()) {
            PreparedStatement prepareStatement = connection.prepareStatement(sql);
            logger.info("Executing " + sql);
            ResultSet resultSet = prepareStatement.executeQuery();
            logger.info("Iterating over results of " + sql);
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
                String postalCode = resultSet.getString("postal_code");
                String city = resultSet.getString("city");

                Customer customer = new Customer(custID, customerName, customerEmail, customerAddress, postalCode, city);
                TaskWrapper taskWrapper = new TaskWrapper(project, task, customer);
                taskInfo.add(taskWrapper);
            }
            if(taskInfo.isEmpty()){
                logger.warn("The list taskInfo is empty!");
            }
        } catch (SQLException e) {
            logger.error("There has been a problem creating the list taskInfo. CLASS: TaskDAO CAUSE: " + e);
        }
        logger.info("Returning taskInfo, process complete.");
        return taskInfo;
    }


}
