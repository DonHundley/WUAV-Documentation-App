package main.java.logic.businessLogic;

import be.Customer;
import be.Project;
import be.Task;
import be.TaskWrapper;
import logic.businessLogic.ProjectLogic;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ProjectLogicTest {

    ProjectLogic projectLogic;

    @BeforeEach
    void setUp() {
        projectLogic = new ProjectLogic();

    }

    @Test
    void testTasksByUserID() {
        //Arrange
        List<TaskWrapper> taskInfoList = new ArrayList<>();

        Task task = new Task(1, "task1", "description1", "not started");
        Project project = new Project(1, "Project 1", java.sql.Date.valueOf(java.time.LocalDate.now()), 1);
        Customer customer = new Customer(1, "Customer", "email@email.com", "Denmarksgade 1", "6700","Esbjerg");

        TaskWrapper taskWrapper = new TaskWrapper(project, task, customer);
        taskInfoList.add(taskWrapper);

        List<Integer> projectsIDsByUsersIDs = new ArrayList<>();
        projectsIDsByUsersIDs.add(1);
        projectsIDsByUsersIDs.add(2);

        List<TaskWrapper> expectedResult = new ArrayList<>();
        expectedResult.add(taskWrapper);

        //Act
        List<TaskWrapper> result = projectLogic.tasksByUserID(projectsIDsByUsersIDs, taskInfoList);
        //Assert
        assertEquals(expectedResult, result);
    }
}
