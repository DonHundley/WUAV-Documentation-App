package gui.model;

import be.*;
import logic.businessLogic.*;

import java.util.*;

public class AuthenticationModel {
    private final UserLogic userLogic;
    private User loggedInUser;

    /**
     * Thread safe singleton of AuthenticationModel.
     */
    private AuthenticationModel(){
        userLogic = new UserLogic();
    }
    private static AuthenticationModel instance;
    public static synchronized AuthenticationModel getInstance(){
        if(instance == null){
            instance = new AuthenticationModel();
        }
        return instance;
    }

    public void authenticateCredentials(String userID, String pass) throws NoSuchElementException {
        if(userLogic.authenticateCredentials(userID, pass) != null){
            loggedInUser = userLogic.authenticateCredentials(userID, pass);
        } else {throw new NoSuchElementException();}
    }

    public User getLoggedInUser() {
        return loggedInUser;
    }
    public void setLoggedInUser(User loggedInUser) {
        this.loggedInUser = loggedInUser;
    }
}
