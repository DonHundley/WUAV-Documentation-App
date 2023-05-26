package be;

import javafx.scene.image.*;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

class TaskWrapperTest {

    @Test
    void getProject() {
        Image image = null;
        Task task = new Task(1,1,"name", image, "description","state");
        Project project = new Project(1, "Project 1", java.sql.Date.valueOf(java.time.LocalDate.now()), 1);
        Customer customer = new Customer(1,"Customer", "email@email.com", "Denmarksgade 1", "6700","Esbjerg");
        TaskWrapper taskWrapper = new TaskWrapper(project,task,customer);
        Project expected = project;
        Project result = taskWrapper.getProject();
        assertEquals(expected,result);
    }

    @Test
    void getTask() {
        Image image = null;
        Task task = new Task(1,1,"name", image, "description","state");
        Project project = new Project(1, "Project 1", java.sql.Date.valueOf(java.time.LocalDate.now()), 1);
        Customer customer = new Customer(1,"Customer", "email@email.com", "Denmarksgade 1", "6700","Esbjerg");
        TaskWrapper taskWrapper = new TaskWrapper(project,task,customer);
        Task expected = task;
        Task result = taskWrapper.getTask();
        assertEquals(expected,result);
    }

    @Test
    void getCustomer() {
        Image image = null;
        Task task = new Task(1,1,"name", image, "description","state");
        Project project = new Project(1, "Project 1", java.sql.Date.valueOf(java.time.LocalDate.now()), 1);
        Customer customer = new Customer(1,"Customer", "email@email.com", "Denmarksgade 1", "6700","Esbjerg");
        TaskWrapper taskWrapper = new TaskWrapper(project,task,customer);
        Customer expected = customer;
        Customer result = taskWrapper.getCustomer();
        assertEquals(expected,result);
    }
}