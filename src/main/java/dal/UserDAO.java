package dal;

import be.Project;
import be.User;
import be.UserWrapper;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {
    private DatabaseConnector databaseConnector;

    public UserDAO() {
        databaseConnector = new DatabaseConnector();
    }

    /**
     * Method to create user in database.
     */
    public User createUser(User user) {
        try (Connection connection = databaseConnector.getConnection()) {
            String sql = "INSERT INTO users(username, password, access, name, last_name) VALUES (?,?,?,?,?)";

            PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, user.getUserName());
            statement.setString(2, user.getPassword());
            statement.setString(3, user.getAccess());
            statement.setString(4, user.getFirstName());
            statement.setString(5, user.getLastName());
            statement.execute();

            ResultSet keys = statement.getGeneratedKeys();
            keys.next();
            int id = keys.getInt(1);

            return new User(id, user.getUserName(), user.getPassword(), user.getAccess(), user.getFirstName(), user.getLastName());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Method to update user in database.
     */
    public void updateUser(User user) {
        try (Connection connection = databaseConnector.getConnection()) {
            String sql = "UPDATE users SET username = ?, password = ?, name = ?, last_name = ?) " + "WHERE userID = ?";

            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setString(1, user.getUserName());
            statement.setString(2, user.getPassword());
            statement.setString(3, user.getFirstName());
            statement.setString(4, user.getLastName());
            statement.setInt(5, user.getUserID());
            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Method to delete user from database.
     */
    public void deleteUser(User user) {
        try (Connection conn = databaseConnector.getConnection()) {
            String sql = "DELETE FROM users WHERE userID= ?";

            PreparedStatement pstmt = conn.prepareStatement(sql);

            pstmt.setInt(1, user.getUserID());
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Method to get all users from database.
     */
    public List<User> getAllUsers() {
        ArrayList<User> users = new ArrayList<>();
        ArrayList<Project> currentProjects = new ArrayList<>();

        try (Connection connection = databaseConnector.getConnection()) {
            String sql = "SELECT * FROM users JOIN works_on ON works_on.userID = users.userID JOIN project ON project.projectID = works_on.projectID;";
            Statement statement = connection.createStatement();

            if(statement.execute(sql)) {
                ResultSet resultSet = statement.getResultSet();

                while (resultSet.next()) {
                    int id = resultSet.getInt("userID");
                    String userName = resultSet.getString("username");
                    String password = resultSet.getString("password");
                    String access = resultSet.getString("access");
                    String name = resultSet.getString("name");
                    String lastName = resultSet.getString("last_name");
                    int projectID = resultSet.getInt("projectID");
                    String projectName = resultSet.getString("project_name");
                    Date dateCreated = resultSet.getDate("date_created");
                    int customerID = resultSet.getInt("customerID");

                    Project project = new Project(projectID, projectName, dateCreated, customerID);
                    currentProjects.add(project);
                    User user = new User(id, userName, password, access, name, lastName, currentProjects);
                    users.add(user);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return users;
    }

    /**
     * Method to get user by ID from database.
     */
    public User getUserById(User user) {
        ArrayList<Project> currentProjects = new ArrayList<>();

        try (Connection connection = databaseConnector.getConnection()) {
            String sql = "SELECT * FROM users WHERE userID = ? JOIN works_on ON works_on.userID = users.userID JOIN project ON project.projectID = works_on.projectID";

            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setInt(1, user.getUserID());

            if(pstmt.execute(sql)) {
                ResultSet resultSet = pstmt.getResultSet();

                while (resultSet.next()) {
                    int id = resultSet.getInt("userID");
                    String userName = resultSet.getString("username");
                    String password = resultSet.getString("password");
                    String access = resultSet.getString("access");
                    String name = resultSet.getString("name");
                    String lastName = resultSet.getString("last_name");
                    int projectID = resultSet.getInt("projectID");
                    String projectName = resultSet.getString("project_name");
                    Date dateCreated = resultSet.getDate("date_created");
                    int customerID = resultSet.getInt("customerID");


                    Project project = new Project(projectID, projectName, dateCreated, customerID);
                    currentProjects.add(project);
                    return new User(id, userName, password, access, name, lastName, currentProjects);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    /**
     * Method to get all technicians from users from database.
     */
    public List<User> getTechnicians() {
        ArrayList<User> technicians = new ArrayList<>();

        try (Connection connection = databaseConnector.getConnection()) {
            String sql = "SELECT * FROM users WHERE access = 'Technician';";
            Statement statement = connection.createStatement();

            if(statement.execute(sql)) {
                ResultSet resultSet = statement.getResultSet();

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
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return technicians;
    }

    /**
     * Method to get all technicians with their assigned task (UserWrapper)
     */
    public List<UserWrapper> getTechWithAssignedTasks() {
        String query = "SELECT u.*, COUNT(t.documentationID) as assigned_tasks " +
                "FROM users u "+
                "LEFT JOIN works_on w ON u.userID = w.userID " +
                "LEFT JOIN task_documentation t ON t.projectID=w.projectID " +
                "WHERE u.access = 'Technician'"+
                "GROUP BY u.userID,u.username, u.name, u.last_name, u.access, u.password";

        try (Connection conn = databaseConnector.getConnection();
             PreparedStatement statement = conn.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            List<UserWrapper> users = new ArrayList<>();
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
            return users;

        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
            return null;
        }
    }


}
