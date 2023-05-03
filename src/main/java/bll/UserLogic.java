package bll;

import be.*;
import dal.*;

import java.util.*;

public class UserLogic {

    private UserDAO userDAO = new UserDAO();

    /**
     * List of Users.
     * @return returns a list of all users.
     */
    public List<User> getUsers() {
        return userDAO.getAllUsers();
    }

    /**
     * List of technicians.
     * @return This will return the list from UserDAO that retrieves all Users who have the technician access level.
     */
    public List<User> getTechs() {
        return userDAO.getTechnicians();
    }

    /**
     * List of technicians.
     * @return This will return the list from UserDAO that retrieves all Users who have the technician access level.
     */
    public List<UserWrapper> getTechsWithAssignedTasks() {
        return userDAO.getTechWithAssignedTasks();
    }

    /**
     * Functions model uses this to create a new user in our database.
     * @param user is the user to be added to the database.
     */
    public void createUser(User user) {
        userDAO.createUser(user);
    }

    /**
     * Functions model uses this to update a user in our database.
     * @param user is the User to be updated.
     */
    public void editUser(User user) {
        userDAO.updateUser(user);
    }

    /**
     * Functions model uses this to delete a user in our database.
     * @param user is the User to be deleted.
     */
    public void deleteUser(User user) {
        userDAO.deleteUser(user);
    }
}
