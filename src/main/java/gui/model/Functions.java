package gui.model;

import be.*;
import logic.businessLogic.*;

public class Functions {

    private UserLogic userLogic = new UserLogic();
    private CustomerLogic customerLogic = new CustomerLogic();
    private ProjectLogic projectLogic = new ProjectLogic();

    /**
     * Customer methods
     */
    public void createCustomer(Customer customer){customerLogic.createCustomer(customer);}
    public void editCustomer(Customer customer){customerLogic.editCustomer(customer);}
    public void deleteCustomer(Customer customer){customerLogic.deleteCustomer(customer);}

    /**
     * Task methods
     */
    public void addTaskPictures(TaskPictures taskPictures){projectLogic.addTaskPictures(taskPictures);}
    public void createTask(Task task){projectLogic.createTask(task);}
    public void updateTask(Task task){projectLogic.updateTask(task);}

    /**
     * Project methods
     */
    public void createProject(Project project){projectLogic.createProject(project);}
    public void deleteProject(Project project){projectLogic.deleteProject(project);}
    public void editProject(Project project){projectLogic.editProject(project);}
    public void assignProject(User selectedUser, Project selectedProject){projectLogic.assignProject(selectedUser, selectedProject);}
    public void removeProjectAssign(User selectedUser, Project selectedProject){projectLogic.removeAssignedProject(selectedUser,selectedProject);}

    /**
     * User methods
     */
    public void createUser(User user){userLogic.createUser(user);}
    public void editUser(User user){userLogic.editUser(user);}
    public void deleteUser(User user){userLogic.deleteUser(user);}


}
