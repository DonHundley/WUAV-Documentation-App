package gui.model;

import be.*;

public class Persistent {

    private User loggedInUser;
    private User selectedUser;
    private Task selectedTask;
    private Project selectedProject;
    private Customer selectedCustomer;

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
}
