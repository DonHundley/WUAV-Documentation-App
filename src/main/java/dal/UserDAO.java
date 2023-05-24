package dal;

import be.Project;
import be.User;
import be.UserWrapper;
import com.microsoft.sqlserver.jdbc.*;
import org.apache.logging.log4j.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {
    private DatabaseConnector databaseConnector;

    public UserDAO() {
        databaseConnector = new DatabaseConnector();
    }

    private static final Logger logger = LogManager.getLogger("debugLogger");

    /**
     * Method to create user in database.
     */
    public User createUser(User user) {
        logger.info("Opening connection in UserDAO");
        int id = 0;
        try (Connection connection = databaseConnector.getConnection()) {
            String sql = "INSERT INTO users(username, password, access, name, last_name) VALUES (?,?,?,?,?)";

            PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, user.getUserName());
            statement.setString(2, user.getPassword());
            statement.setString(3, user.getAccess());
            statement.setString(4, user.getFirstName());
            statement.setString(5, user.getLastName());
            logger.info("Executing " + sql);
            statement.execute();

            ResultSet keys = statement.getGeneratedKeys();
            keys.next();
            id = keys.getInt(1);
            if (id == 0) {
                logger.warn("There is an issue with the ID of the new user.");
            }

        } catch (SQLException e) {
            logger.error("There has been a problem creating the user." , e);
        }
        logger.info("Returning new user, process complete.");
        return new User(id, user.getUserName(), user.getPassword(), user.getAccess(), user.getFirstName(), user.getLastName());
    }

    /**
     * Method to update user in database.
     */
    public void updateUser(User user) {
        logger.info("Opening connection in UserDAO");
        try (Connection connection = databaseConnector.getConnection()) {
            String sql = "UPDATE users SET username = ?, password = ?, access = ?, name = ?, last_name = ?" + " WHERE userID = ?";

            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setString(1, user.getUserName());
            statement.setString(2, user.getPassword());
            statement.setString(3, user.getAccess());
            statement.setString(4, user.getFirstName());
            statement.setString(5, user.getLastName());
            statement.setInt(6, user.getUserID());
            logger.info("Executing " + sql);
            statement.executeUpdate();

        } catch (SQLException e) {
            logger.error("There has been a problem updating the user." , e);
        }
        logger.info("Update user process finished.");
    }

    /**
     * Method to delete user from database.
     */
    public void deleteUser(User user) {
        logger.info("Opening connection in UserDAO");
        try (Connection conn = databaseConnector.getConnection()) {
            String sql = "DELETE FROM users WHERE userID= ?";

            PreparedStatement pstmt = conn.prepareStatement(sql);

            pstmt.setInt(1, user.getUserID());
            logger.info("Executing " + sql);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            logger.error("There was a problem deleting the user." ,e);
        }
        logger.info("User deletion process finished.");
    }

    /**
     * Method to get all users from database.
     */
    public List<User> getAllUsers() {
        ArrayList<User> users = new ArrayList<>();
        logger.info("Opening new connection in UserDAO");
        try (Connection connection = databaseConnector.getConnection()) {
            String sql = "SELECT * FROM users";
            Statement statement = connection.createStatement();
            logger.info("Executing " + sql);
            if (statement.execute(sql)) {
                ResultSet resultSet = statement.getResultSet();
                logger.info("Iterating over results of " + sql);
                while (resultSet.next()) {
                    int id = resultSet.getInt("userID");
                    String userName = resultSet.getString("username");
                    String password = resultSet.getString("password");
                    String access = resultSet.getString("access");
                    String name = resultSet.getString("name");
                    String lastName = resultSet.getString("last_name");


                    User user = new User(id, userName, password, access, name, lastName);
                    users.add(user);
                }
            }
            if (users.isEmpty()) {
                logger.warn("The list of users is empty!");
            }
        } catch (SQLException e) {
            logger.error("There has beeen a problem creating a list of users." , e);
        }
        logger.info("Returning list of users, process finished.");
        return users;
    }

    /**
     * Method to get user by ID from database.
     */
    public User getUserById(User user) {
        logger.info("Opening new connection in UserDAO");
        try (Connection connection = databaseConnector.getConnection()) {
            String sql = "SELECT * FROM users WHERE userID = ?";

            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setInt(1, user.getUserID());
            logger.info("Executing " + sql);
            if (pstmt.execute(sql)) {
                ResultSet resultSet = pstmt.getResultSet();

                while (resultSet.next()) {
                    int id = resultSet.getInt("userID");
                    String userName = resultSet.getString("username");
                    String password = resultSet.getString("password");
                    String access = resultSet.getString("access");
                    String name = resultSet.getString("name");
                    String lastName = resultSet.getString("last_name");


                    return new User(id, userName, password, access, name, lastName);
                }
            }
        } catch (SQLException e) {
            logger.error("There has been a problem getting user by ID. " ,e);
        }
        logger.info("Get user by ID process complete.");
        return null;
    }

    /**
     * Method to get all technicians from users from database.
     */
    public List<User> getTechnicians() {
        ArrayList<User> technicians = new ArrayList<>();
        logger.info("Opening connection in UserDAO");
        try (Connection connection = databaseConnector.getConnection()) {
            String sql = "SELECT * FROM users WHERE access = 'Technician';";
            Statement statement = connection.createStatement();
            logger.info("Executing " + sql);
            if (statement.execute(sql)) {
                ResultSet resultSet = statement.getResultSet();
                logger.info("Iterating over results of " + sql);
                while (resultSet.next()) {
                    int id = resultSet.getInt("userID");
                    String userName = resultSet.getString("username");
                    String password = resultSet.getString("password");
                    String access = resultSet.getString("access");
                    String name = resultSet.getString("name");
                    String lastName = resultSet.getString("last_name");

                    User tech = new User(id, userName, password, access, name, lastName);
                    technicians.add(tech);
                }
            }
            if (technicians.isEmpty()) {
                logger.warn("The list of technicians is empty!");
            }
        } catch (SQLException e) {
            logger.error("There has been a problem fetching a list of technicians." , e);
        }
        logger.info("Returning list of technicians, process complete.");
        return technicians;
    }

    /**
     * Method to get all technicians with their assigned task (UserWrapper)
     */
    public List<UserWrapper> getTechWithAssignedTasks() {
        List<UserWrapper> users = new ArrayList<>();

        logger.info("Opening new connection in UserDAO.");
        try (Connection conn = databaseConnector.getConnection()) {
            String query = "SELECT u.*, COUNT(t.documentationID) as assigned_tasks " +
                    "FROM users u " +
                    "LEFT JOIN works_on w ON u.userID = w.userID " +
                    "LEFT JOIN task_documentation t ON t.projectID=w.projectID " +
                    "WHERE u.access = 'Technician'" +
                    "GROUP BY u.userID,u.username, u.name, u.last_name, u.access, u.password";


            PreparedStatement statement = conn.prepareStatement(query);
            logger.info("Executing " + query);
            ResultSet resultSet = statement.executeQuery();

            logger.info("Iterating over results of " + query);
            while (resultSet.next()) {
                User user = new User(
                        resultSet.getInt("userID"),
                        resultSet.getString("username"),
                        resultSet.getString("password"),
                        resultSet.getString("access"),
                        resultSet.getString("name"),
                        resultSet.getString("last_Name")
                );
                int assignedTasks = resultSet.getInt("assigned_tasks");
                UserWrapper wrapper = new UserWrapper(user, assignedTasks);
                users.add(wrapper);
            }
            if(users.isEmpty()){
                logger.warn("getTechWithAssignedTasks() in UserDAO has created an empty list!");
            }
        } catch (SQLException e) {
            logger.error("There has been a problem creating the list of techs with tasks. " , e);
        }
        logger.info("returning list of users, process finished.");
        return users;
    }
}
