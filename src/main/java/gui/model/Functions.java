package gui.model;

import be.*;
import javafx.scene.image.Image;
import logic.businessLogic.*;
import logic.documentPDF.PDFHandler;

import java.util.*;

public class Functions {

    private UserLogic userLogic = new UserLogic();
    private CustomerLogic customerLogic = new CustomerLogic();
    private ProjectLogic projectLogic = new ProjectLogic();

    private PDFHandler pdfHandler = new PDFHandler();

    /**
     * Customer methods
     */
    public void createCustomer(Customer customer) {
        customerLogic.createCustomer(customer);
    }

    public void editCustomer(Customer customer) {
        customerLogic.editCustomer(customer);
    }

    public void deleteCustomer(Customer customer) {
        customerLogic.deleteCustomer(customer);
    }

    /**
     * Task methods
     */
    public void addTaskPictures(TaskPictures taskPictures) {
        projectLogic.addTaskPictures(taskPictures);
    }

    public void createTask(Task task) {
        projectLogic.createTask(task);
    }

    public void updateTask(Task task) {
        projectLogic.updateTask(task);
    }

    public void updateLayout(Task task) {
        projectLogic.updateLayout(task);
    }

    /**
     * Project methods
     */
    public void createProject(Project project) {
        projectLogic.createProject(project);
    }

    public void deleteProject(Project project) {
        projectLogic.deleteProject(project);
    }

    public void editProject(Project project) {
        projectLogic.editProject(project);
    }

    public void assignProject(User selectedUser, Project selectedProject) {
        projectLogic.assignProject(selectedUser, selectedProject);
    }

    public void removeProjectAssign(User selectedUser, Project selectedProject) {
        projectLogic.removeAssignedProject(selectedUser, selectedProject);
    }

    /**
     * User methods
     */
    public void createUser(User user) {
        userLogic.createUser(user);
    }

    public void editUser(User user) {
        userLogic.editUser(user);
    }

    public void deleteUser(User user) {
        userLogic.deleteUser(user);
    }

    public HashMap<Integer, Integer> userInfo() {
        return userLogic.loginInformation();
    }

    public List<User> users() {
        return userLogic.getUsers();
    }

    public List<TaskPictures> taskPicturesByDocID(Task task) {
        return projectLogic.getTaskPicturesByTask(task);
    }

    /**
     * PDF methods
     **/
    public void exportReport(Customer customer, Project project, Task task, Image image1, Image image2) {
        pdfHandler.exportReport(customer,project,task,image1,image2);
    }
}
