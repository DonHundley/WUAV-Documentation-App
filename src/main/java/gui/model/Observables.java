package gui.model;

import be.*;
import bll.*;
import javafx.collections.*;

public class Observables {

    private ObservableList<User> users = FXCollections.observableArrayList();
    private ObservableList<User> techs = FXCollections.observableArrayList();
    private ObservableList<Project> projects = FXCollections.observableArrayList();
    private ObservableList<Customer> customers = FXCollections.observableArrayList();

    private UserLogic userLogic;
    private CustomerLogic customerLogic;
    private ProjectLogic projectLogic;

    /**
     * Getters for our observable lists.
     */
    public ObservableList<User> getUsers() {
        return users;
    }

    public ObservableList<User> getTechs() {
        return techs;
    }

    public ObservableList<Project> getProjects() {
        return projects;
    }

    public ObservableList<Customer> getCustomers() {
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
        techs.addAll(userLogic.getTechs());
    }
    public void loadProjects() {
        projects.clear();
        projects.addAll(projectLogic.getProjects());
    }
    public void loadCustomers() {
        customers.clear();
        customers.addAll(customerLogic.getCustomers());
    }
}
