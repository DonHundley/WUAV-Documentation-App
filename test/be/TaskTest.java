package be;

import javafx.scene.image.*;
import org.junit.jupiter.api.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class TaskTest {

    @Test
    void getDocID() {
        Image image = null;
        Task instance = new Task(1,1,"name", image, "description","state");
        int expected = 1;
        int result = instance.getDocID();
        assertEquals(expected,result);
    }

    @Test
    void setDocID() {
        Image image = null;
        Task instance = new Task(1,1,"name", image, "description","state");
        int expected = 2;
        instance.setDocID(2);
        int result = instance.getDocID();
        assertEquals(expected,result);
    }

    @Test
    void getProjID() {
        Image image = null;
        Task instance = new Task(1,1,"name", image, "description","state");
        int expected = 1;
        int result = instance.getProjID();
        assertEquals(expected,result);
    }

    @Test
    void setProjID() {
        Image image = null;
        Task instance = new Task(1,1,"name", image, "description","state");
        int expected = 2;
        instance.setProjID(2);
        int result = instance.getProjID();
        assertEquals(expected,result);
    }

    @Test
    void getTaskName() {
        Image image = null;
        Task instance = new Task(1,1,"name", image, "description","state");
        String expected = "name";
        String result = instance.getTaskName();
        assertEquals(expected,result);
    }

    @Test
    void setTaskName() {
        Image image = null;
        Task instance = new Task(1,1,"name", image, "description","state");
        String expected = "new name";
        instance.setTaskName("new name");
        String result = instance.getTaskName();
        assertEquals(expected,result);
    }

    @Test
    void getTaskLayout() {
        Image image = new Image(Objects.requireNonNull(this.getClass().getResourceAsStream("/layouts/snapshot1685030100780.png")));
        Task instance = new Task(1,1,"name", image, "description","state");
        Image expected = image;
        Image result = instance.getTaskLayout();
        assertEquals(expected,result);
    }

    @Test
    void setTaskLayout() {
        Image image = null;
        Image image2 = new Image(Objects.requireNonNull(this.getClass().getResourceAsStream("/layouts/snapshot1685030100780.png")));
        Task instance = new Task(1,1,"name", image, "description","state");
        Image expected = image2;
        instance.setTaskLayout(image2);
        Image result = instance.getTaskLayout();
        assertEquals(expected,result);
    }

    @Test
    void getTaskDesc() {
        Image image = null;
        Task instance = new Task(1,1,"name", image, "description","state");
        String expected = "description";
        String result = instance.getTaskDesc();
        assertEquals(expected,result);
    }

    @Test
    void setTaskDesc() {
        Image image = null;
        Task instance = new Task(1,1,"name", image, "description","state");
        String expected = "new description";
        instance.setTaskDesc("new description");
        String result = instance.getTaskDesc();
        assertEquals(expected,result);
    }

    @Test
    void getTaskState() {
        Image image = null;
        Task instance = new Task(1,1,"name", image, "description","state");
        String expected = "state";
        String result = instance.getTaskState();
        assertEquals(expected,result);
    }

    @Test
    void setTaskState() {
        Image image = null;
        Task instance = new Task(1,1,"name", image, "description","state");
        String expected = "new state";
        instance.setTaskState("new state");
        String result = instance.getTaskState();
        assertEquals(expected,result);
    }

    @Test
    void getTaskLayoutAbsolute() {
        Task instance = new Task();
        String expected = "path";
        instance.setTaskLayoutAbsolute("path");
        String result = instance.getTaskLayoutAbsolute();
        assertEquals(expected,result);
    }

    @Test
    void setTaskLayoutAbsolute() {
        Task instance = new Task();
        String expected = "path";
        instance.setTaskLayoutAbsolute("path");
        String result = instance.getTaskLayoutAbsolute();
        assertEquals(expected,result);
    }
}