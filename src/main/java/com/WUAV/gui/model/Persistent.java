package com.WUAV.gui.model;

import com.WUAV.be.*;
import javafx.scene.layout.*;

public class Persistent {

    private User loggedInUser;
    private User selectedUser;
    private Task selectedTask;
    private Project selectedProject;
    private Customer selectedCustomer;
    private AnchorPane viewAnchor;

    /**
     * Thread safe singleton of Persistent.
     */
    private Persistent(){}
    private static Persistent instance;
    public static synchronized Persistent getInstance(){
        if(instance == null){
            instance = new Persistent();
        }
        return instance;
    }

    /**
     * These methods are the getters for the objects we keep a persistent instance of.
     */
    public User getLoggedInUser() {
        return loggedInUser;
    }
    public Task getSelectedTask() {
        return selectedTask;
    }
    public User getSelectedUser() {
        return selectedUser;
    }
    public Project getSelectedProject() {
        return selectedProject;
    }

    public Customer getSelectedCustomer() {return selectedCustomer;}

    /**
     * These methods are the setters for the objects we keep a persistent instance of.
     */
    public void setSelectedUser(User selectedUser) {
        this.selectedUser = selectedUser;
    }
    public void setLoggedInUser(User loggedInUser) {
        this.loggedInUser = loggedInUser;
    }
    public void setSelectedTask(Task selectedTask) {
        this.selectedTask = selectedTask;
    }
    public void setSelectedProject(Project selectedProject) {
        this.selectedProject = selectedProject;
    }

    public void setSelectedCustomer(Customer selectedCustomer) {this.selectedCustomer = selectedCustomer;}


    public AnchorPane getViewAnchor() {
        return viewAnchor;
    }

    public void setViewAnchor(AnchorPane viewAnchor) {
        this.viewAnchor = viewAnchor;
    }
}
