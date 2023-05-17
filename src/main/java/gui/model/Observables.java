package gui.model;

import be.*;
import javafx.collections.*;
import javafx.geometry.Pos;
import logic.businessLogic.*;

public class Observables {



    private ObservableList<User> users = FXCollections.observableArrayList();
    private ObservableList<UserWrapper> techs = FXCollections.observableArrayList();
    private ObservableList<ProjectWrapper> projects = FXCollections.observableArrayList();
    private ObservableList<Task> allTasks = FXCollections.observableArrayList();
    private ObservableList<TaskWrapper> tasksByUser = FXCollections.observableArrayList();
    private ObservableList<TaskWrapper> tasksInfo = FXCollections.observableArrayList();
    private ObservableList<Task> tasksByProject = FXCollections.observableArrayList();
    private ObservableList<CustomerWrapper> customerswithWrapper = FXCollections.observableArrayList();
    private ObservableList<Customer> customers = FXCollections.observableArrayList();

    private ObservableList<PostalCode> postalCodes = FXCollections.observableArrayList();

    private UserLogic userLogic=new UserLogic();
    private CustomerLogic customerLogic=new CustomerLogic();
    private ProjectLogic projectLogic=new ProjectLogic();

    /**
     * Thread safe singleton of Observables.
     */
    private Observables(){}
    private static Observables instance;
    public static synchronized Observables getInstance(){
        if(instance == null){
            instance = new Observables();
        }
        return instance;
    }

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

    public ObservableList<CustomerWrapper> getCustomersWithWrapper() {
        return customerswithWrapper;
    }

    public ObservableList<Customer> getCustomers() {
        return customers;
    }
    public ObservableList<TaskWrapper> getTasksByUserID(){
        return tasksByUser;
    }
    public ObservableList<PostalCode> getPostalCodes() {return postalCodes;};

    public void search(String query) {
        customerswithWrapper.clear();
        customerswithWrapper.addAll(customerLogic.searchCustomer(query));
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
    public void loadCustomersWithWrapper() {
        customerswithWrapper.clear();
        customerswithWrapper.addAll(customerLogic.getCustomersWithProjects());
    }

    public void loadCustomers() {
        customers.clear();
        customers.addAll(customerLogic.getCustomers());
    }

    public void loadProjectsByUser(User user){
        tasksByUser.clear();
        tasksByUser.addAll(projectLogic.tasksByUserID(user));
    }

    public void loadPostalCodes() {
        postalCodes.clear();
        postalCodes.addAll(customerLogic.getAllPostalCodes());
    }
}
