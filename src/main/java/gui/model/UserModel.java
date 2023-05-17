package gui.model;

import be.*;
import javafx.collections.*;
import logic.businessLogic.*;

import java.util.*;

public class UserModel {

    private ObservableList<User> users = FXCollections.observableArrayList();
    private ObservableList<UserWrapper> techs = FXCollections.observableArrayList();
    private User selectedUser;
    private UserLogic userLogic = new UserLogic();

    /**
     * Thread safe singleton of UserModel.
     */
    private UserModel(){}
    private static UserModel instance;
    public static synchronized UserModel getInstance(){
        if(instance == null){
            instance = new UserModel();
        }
        return instance;
    }

    /**
     * user lists.
     */
    public ObservableList<User> getUsers() {
        return users;
    }
    public ObservableList<UserWrapper> getTechs() {
        return techs;
    }
    public List<User> users() {
        return userLogic.getUsers();
    }

    /**
     * Loaders for our lists.
     */
    public void loadUsers() {
        users.clear();
        users.addAll(userLogic.getUsers());
    }
    public void loadTechs() {
        techs.clear();
        techs.addAll(userLogic.getTechsWithAssignedTasks());
    }

    /**
     * User crud functions
     */
    public void createUser(User user) {
        userLogic.createUser(user);
    }

    public void editUser(User user) {
        userLogic.editUser(user);
    }

    public void deleteUser() {
        userLogic.deleteUser(selectedUser);
    }

    /**
     * getter and setter for selected user.
     */
    public User getSelectedUser() {
        return selectedUser;
    }
    public void setSelectedUser(User selectedUser) {
        this.selectedUser = selectedUser;
    }
}
