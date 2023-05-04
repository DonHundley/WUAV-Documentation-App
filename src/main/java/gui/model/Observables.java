package gui.model;

import be.*;
import javafx.collections.*;
import logic.businessLogic.*;

public class Observables {

    private ObservableList<User> users = FXCollections.observableArrayList();
    private ObservableList<UserWrapper> techs = FXCollections.observableArrayList();
    private ObservableList<ProjectWrapper> projects = FXCollections.observableArrayList();
    private ObservableList<Task> allTasks = FXCollections.observableArrayList();
    private ObservableList<TaskWrapper> tasksInfo = FXCollections.observableArrayList();
    private ObservableList<Task> tasksByProject = FXCollections.observableArrayList();
    private ObservableList<CustomerWrapper> customers = FXCollections.observableArrayList();

    private UserLogic userLogic=new UserLogic();
    private CustomerLogic customerLogic=new CustomerLogic();
    private ProjectLogic projectLogic=new ProjectLogic();



    /**
     * Getters for our observable lists.
     */
    public ObservableList<User> getUsers() {
        return users;
    }

    public ObservableList<UserWrapper> getTechs() {
        return techs;
    }

    public ObservableList<ProjectWrapper> getProjects() {
        return projects;
    }

    public ObservableList<Task> getAllTasks(){return allTasks;}
    public ObservableList<TaskWrapper> getTasksInfo(){return tasksInfo;}

    public ObservableList<Task> getTasksByProject() {
        return tasksByProject;
    }

    public ObservableList<CustomerWrapper> getCustomers() {
        return customers;
    }

    /**
     * These commands are used to load and refresh our lists.
     */
    public void loadUsers() {
        users.clear();
        users.addAll(userLogic.getUsers());
    }
    public void loadTechs() {
        techs.clear();
        techs.addAll(userLogic.getTechsWithAssignedTasks());
    }
    public void loadProjects() {
        projects.clear();
        projects.addAll(projectLogic.getProjectsWithTaskCount());
    }
    public void loadAllTasks(){
        allTasks.clear();
        allTasks.addAll(projectLogic.getAllTasks());
    }
    public void loadTasksInfo(){
        tasksInfo.clear();
        tasksInfo.addAll(projectLogic.getTasksInfo());
    }
    public void loadTasksByProject(Project selectedProject){
        tasksByProject.clear();
        tasksByProject.addAll(projectLogic.getTasksByProject(selectedProject));
    }
    public void loadCustomers() {
        customers.clear();
        customers.addAll(customerLogic.getCustomersWithProjects());
    }
}
