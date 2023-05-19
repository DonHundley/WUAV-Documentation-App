package gui.model;

import be.*;
import logic.businessLogic.*;

import java.util.*;

public class AuthenticationModel {

    /**
     * Thread safe singleton of AuthenticationModel.
     */
    private AuthenticationModel(){}
    private static AuthenticationModel instance;
    public static synchronized AuthenticationModel getInstance(){
        if(instance == null){
            instance = new AuthenticationModel();
        }
        return instance;
    }

    private UserLogic userLogic = new UserLogic();
    private User loggedInUser;
    public User getLoggedInUser() {
        return loggedInUser;
    }
    public void setLoggedInUser(User loggedInUser) {
        this.loggedInUser = loggedInUser;
    }
    public HashMap<Integer, Integer> userInfo() {
        return userLogic.loginInformation(userLogic.getUsers());
    }
}
