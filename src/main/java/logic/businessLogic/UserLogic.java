package logic.businessLogic;

import be.*;
import dal.*;
import org.apache.logging.log4j.*;

import java.util.*;

public class UserLogic {


    private final UserDAO userDAO;
    private static final Logger logger = LogManager.getLogger("debugLogger");

    public UserLogic() {
        userDAO = new UserDAO();
    }

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



    public HashMap<Integer, Integer> loginInformation(List<User> allUsers){
        logger.info("Creating hashmap of login information");
        HashMap<Integer, Integer> loginInfo = new HashMap<>();

        if(allUsers.isEmpty()){
            logger.warn("The list of users is empty!");
        }

        logger.info("Iterating over users");
        for (User user: allUsers) {
            //System.out.println(user.getUserName() + " "+ user.getPassword());
            int k = user.getUserName().hashCode();
            int v = user.getPassword().hashCode();

            loginInfo.put(k, v);
        }
        if(loginInfo.isEmpty()){
            logger.warn("There are no accounts to log into!");
        }
        logger.info("Hashmap complete, process finished.");
        return loginInfo;
    }
}
