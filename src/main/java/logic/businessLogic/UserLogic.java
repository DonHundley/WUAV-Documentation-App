package logic.businessLogic;

import be.*;
import dal.*;
import org.apache.logging.log4j.*;

import java.util.*;

public class UserLogic {


    private final UserDAO userDAO;
    private final HashMap<Integer, Integer> loginInfo;
    private final List<User> allUsers;
    private static final Logger logger = LogManager.getLogger("debugLogger");

    public UserLogic() {
        userDAO = new UserDAO();
        loginInfo = new HashMap<>();
        allUsers = new ArrayList<>();
        setUsers();
    }

    /**
     * List of Users.
     * @return returns a list of all users.
     */
    public List<User> getUsers() {
        return allUsers;
    }
    public void setUsers() {
        allUsers.clear();
        allUsers.addAll(userDAO.getAllUsers());
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
        setUsers();
    }

    /**
     * Functions model uses this to update a user in our database.
     * @param user is the User to be updated.
     */
    public void editUser(User user) {
        userDAO.updateUser(user);
        setUsers();
    }

    /**
     * Functions model uses this to delete a user in our database.
     * @param user is the User to be deleted.
     */
    public void deleteUser(User user) {
        userDAO.deleteUser(user);
        setUsers();
    }

    public HashMap<Integer, Integer> getLoginInfo() {
        return loginInfo;
    }

    /**
     * Creates a hashmap of all usernames and passwords
     * @param allUsers list of all users.
     */
    public void loginInformation(List<User> allUsers){
        logger.info("Creating hashmap of login information");
        if(allUsers.isEmpty()){
            logger.warn("The list of users is empty!");
        }

        logger.info("Iterating over users");
        for (User user: allUsers) {
            int k = user.getUserName().hashCode();
            int v = user.getPassword().hashCode();
            loginInfo.put(k, v);
        }
        if(loginInfo.isEmpty()){
            logger.warn("There are no accounts to log into!");
        }
        logger.info("Hashmap complete, process finished.");
    }

    /**
     * Authenticates the given credentials, checking if they are valid login information.
     * @param userID The username to check.
     * @param pass The user pass to check
     * @return if the credentials are valid we return the user associated with that username.
     */
    public User authenticateCredentials(String userID, String pass) {
        loginInformation(allUsers);
        if (loginInfo.containsKey(userID.hashCode())) {
            logger.trace("Checking password");
            if (loginInfo.get(userID.hashCode()).equals(pass.hashCode())) {
                logger.info("Correct user information input. Locating user profile.");
                for (User user : allUsers
                ) {
                    if (Objects.equals(userID, user.getUserName())) {
                        logger.info("Setting logged in user.");
                        return user;
                    }
                }
            }
        }
        return null;
    }
}
