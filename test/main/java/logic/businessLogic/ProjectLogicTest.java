package main.java.logic.businessLogic;

import be.*;
import javafx.scene.image.*;
import logic.businessLogic.ProjectLogic;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

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

    @Test
    void createTaskPictureLists(){
        List<TaskPictures> taskPictures = new ArrayList<>();
        Image image = new Image(Objects.requireNonNull(this.getClass().getResourceAsStream("/layouts/snapshot1685030100780.png")));
        TaskPictures instance = new TaskPictures(1,1,"deviceName1","password1",image);
        TaskPictures instance2 = new TaskPictures(2,2,"deviceName2","password2",image);
        TaskPictures instance3 = new TaskPictures(3,3,"deviceName3","password3",image);
        taskPictures.add(instance);
        taskPictures.add(instance2);
        taskPictures.add(instance3);

        projectLogic.createTaskPictureLists(taskPictures);

        int expected = 3;

        int result1 = projectLogic.getImageList().size();
        int result2= projectLogic.getDeviceList().size();
        int result3 = projectLogic.getDeviceCredentials().size();

        assertEquals(expected,result1);
        assertEquals(expected,result2);
        assertEquals(expected,result3);
    }

    @Test
    void getImageList(){
        List<TaskPictures> taskPictures = new ArrayList<>();
        Image image = new Image(Objects.requireNonNull(this.getClass().getResourceAsStream("/layouts/snapshot1685030100780.png")));
        TaskPictures instance = new TaskPictures(1,1,"deviceName1","password1",image);
        TaskPictures instance2 = new TaskPictures(2,2,"deviceName2","password2",image);
        TaskPictures instance3 = new TaskPictures(3,3,"deviceName3","password3",image);
        taskPictures.add(instance);
        taskPictures.add(instance2);
        taskPictures.add(instance3);

        projectLogic.createTaskPictureLists(taskPictures);

        int expected = 3;

        int result = projectLogic.getImageList().size();

        assertEquals(expected,result);
        assertNotNull(projectLogic.getImageList());
        assertEquals(image,projectLogic.getImageList().get(1));
    }

    @Test
    void getDeviceString(){
        List<TaskPictures> taskPictures = new ArrayList<>();
        Image image = new Image(Objects.requireNonNull(this.getClass().getResourceAsStream("/layouts/snapshot1685030100780.png")));
        TaskPictures instance = new TaskPictures(1,1,"deviceName1","password1",image);
        TaskPictures instance2 = new TaskPictures(2,2,"deviceName2","password2",image);
        TaskPictures instance3 = new TaskPictures(3,3,"deviceName3","password3",image);
        taskPictures.add(instance);
        taskPictures.add(instance2);
        taskPictures.add(instance3);

        projectLogic.createTaskPictureLists(taskPictures);

        String expected = "Device 1: deviceName1, Device 2: deviceName2, Device 3: deviceName3, ";
        String result = projectLogic.getDeviceString();

        assertEquals(expected,result);
    }
    @Test
    void getDeviceList(){
        List<TaskPictures> taskPictures = new ArrayList<>();
        Image image = new Image(Objects.requireNonNull(this.getClass().getResourceAsStream("/layouts/snapshot1685030100780.png")));
        TaskPictures instance = new TaskPictures(1,1,"deviceName1","password1",image);
        TaskPictures instance2 = new TaskPictures(2,2,"deviceName2","password2",image);
        TaskPictures instance3 = new TaskPictures(3,3,"deviceName3","password3",image);
        taskPictures.add(instance);
        taskPictures.add(instance2);
        taskPictures.add(instance3);

        projectLogic.createTaskPictureLists(taskPictures);

        int expected = 3;

        int result = projectLogic.getDeviceList().size();

        assertEquals(expected,result);
        assertNotNull(projectLogic.getDeviceList());
        assertEquals("Device 3: deviceName3, ",projectLogic.getDeviceList().get(2));
    }
    @Test
    void getDeviceCredentialsString(){
        List<TaskPictures> taskPictures = new ArrayList<>();
        Image image = new Image(Objects.requireNonNull(this.getClass().getResourceAsStream("/layouts/snapshot1685030100780.png")));
        TaskPictures instance = new TaskPictures(1,1,"deviceName1","password1",image);
        TaskPictures instance2 = new TaskPictures(2,2,"deviceName2","password2",image);
        TaskPictures instance3 = new TaskPictures(3,3,"deviceName3","password3",image);
        taskPictures.add(instance);
        taskPictures.add(instance2);
        taskPictures.add(instance3);

        projectLogic.createTaskPictureLists(taskPictures);

        String expected = "Device 1: password1, Device 2: password2, Device 3: password3, ";
        String result = projectLogic.getDeviceCredentialsString();

        assertEquals(expected,result);
    }
    @Test
    void getDeviceCredentials(){
        List<TaskPictures> taskPictures = new ArrayList<>();
        Image image = new Image(Objects.requireNonNull(this.getClass().getResourceAsStream("/layouts/snapshot1685030100780.png")));
        TaskPictures instance = new TaskPictures(1,1,"deviceName1","password1",image);
        TaskPictures instance2 = new TaskPictures(2,2,"deviceName2","password2",image);
        TaskPictures instance3 = new TaskPictures(3,3,"deviceName3","password3",image);
        taskPictures.add(instance);
        taskPictures.add(instance2);
        taskPictures.add(instance3);

        projectLogic.createTaskPictureLists(taskPictures);

        int expected = 3;

        int result = projectLogic.getDeviceCredentials().size();

        assertEquals(expected,result);
        assertNotNull(projectLogic.getDeviceCredentials());
        assertEquals("Device 1: password1, ",projectLogic.getDeviceCredentials().get(0));
    }
    @Test
    void imageLocationX(){
        int image1 = 1;
        int image5 = 5;
        int image9 = 9;
        int image13 = 13;
        int image17 = 17;

        int imgWidth = 150;

        int expected = 5; // each row starts at pixel 5

        int result1 = projectLogic.imageLocationX(image1, imgWidth);
        int result2 = projectLogic.imageLocationX(image5, imgWidth);
        int result3 = projectLogic.imageLocationX(image9, imgWidth);
        int result4 = projectLogic.imageLocationX(image13, imgWidth);
        int result5 = projectLogic.imageLocationX(image17, imgWidth);

        assertEquals(expected,result1);
        assertEquals(expected,result2);
        assertEquals(expected,result3);
        assertEquals(expected,result4);
        assertEquals(expected,result5);
    }

    @Test
    void imageLocationY(){
        int image1 = 1;
        int image5 = 5;
        int image9 = 9;
        int image13 = 13;
        int image17 = 17;

        int imgHeight = 100;

        int expected1 = 0; // row 1 has a y of 0
        int expected2 = 105; // row 2 has a y of height + 5
        int expected3 = 210; // y = height x 2 + 10
        int expected4 = 315; // y = height x 3 + 15
        int expected5 = 420; // y = height x 4 + 20


        int result1 = projectLogic.imageLocationY(image1, imgHeight);
        int result2 = projectLogic.imageLocationY(image5, imgHeight);
        int result3 = projectLogic.imageLocationY(image9, imgHeight);
        int result4 = projectLogic.imageLocationY(image13, imgHeight);
        int result5 = projectLogic.imageLocationY(image17, imgHeight);

        assertEquals(expected1,result1);
        assertEquals(expected2,result2);
        assertEquals(expected3,result3);
        assertEquals(expected4,result4);
        assertEquals(expected5,result5);
    }

}
