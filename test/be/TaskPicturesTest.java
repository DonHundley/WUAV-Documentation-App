package be;

import javafx.scene.image.*;
import org.apache.commons.io.*;
import org.junit.jupiter.api.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class TaskPicturesTest {

    @Test
    void getPictureID() {
        Image image = null;
        TaskPictures instance = new TaskPictures(1,2,"deviceName","password",image);
        int expected = 1;
        int result = instance.getPictureID();
        assertEquals(expected,result);
    }

    @Test
    void setPictureID() {
        Image image = null;
        TaskPictures instance = new TaskPictures(1,2,"deviceName","password",image);
        int expected = 2;
        instance.setPictureID(2);
        int result = instance.getPictureID();
        assertEquals(expected,result);
    }

    @Test
    void getDocID() {
        Image image = null;
        TaskPictures instance = new TaskPictures(1,2,"deviceName","password",image);
        int expected = 2;
        int result = instance.getDocID();
        assertEquals(expected,result);
    }

    @Test
    void setDocID() {
        Image image = null;
        TaskPictures instance = new TaskPictures(1,2,"deviceName","password",image);
        int expected = 1;
        instance.setDocID(1);
        int result = instance.getDocID();
        assertEquals(expected,result);
    }

    @Test
    void getDeviceName() {
        Image image = null;
        TaskPictures instance = new TaskPictures(1,2,"deviceName","password",image);
        String expected = "deviceName";
        String result = instance.getDeviceName();
        assertEquals(expected,result);
    }

    @Test
    void setDeviceName() {
        Image image = null;
        TaskPictures instance = new TaskPictures(1,2,"deviceName","password",image);
        String expected = "different name";
        instance.setDeviceName("different name");
        String result = instance.getDeviceName();
        assertEquals(expected,result);
    }

    @Test
    void getPassword() {
        Image image = null;
        TaskPictures instance = new TaskPictures(1,2,"deviceName","password",image);
        String expected = "password";
        String result = instance.getPassword();
        assertEquals(expected,result);
    }

    @Test
    void setPassword() {
        Image image = null;
        TaskPictures instance = new TaskPictures(1,2,"deviceName","password",image);
        String expected = "new password";
        instance.setPassword("new password");
        String result = instance.getPassword();
        assertEquals(expected,result);
    }

    @Test
    void getPictureAbsolute() {
        TaskPictures instance = new TaskPictures(1,1,"deviceName","password","path");
        String expected = "path";
        String result = instance.getPictureAbsolute();
        assertEquals(expected,result);
    }

    @Test
    void setPictureAbsolute() {
        TaskPictures instance = new TaskPictures(1,1,"deviceName","password","path");
        String expected = "new absolute path";
        instance.setPictureAbsolute("new absolute path");
        String result = instance.getPictureAbsolute();
        assertEquals(expected,result);
    }

    @Test
    void getPicture() {
        Image image = new Image(Objects.requireNonNull(this.getClass().getResourceAsStream("/layouts/snapshot1685030100780.png")));
        TaskPictures instance = new TaskPictures(1,2,"deviceName","password",image);
        Image expected = image;
        Image result = instance.getPicture();
        assertEquals(expected,result);
    }

    @Test
    void setPicture() {
        Image image = new Image(Objects.requireNonNull(this.getClass().getResourceAsStream("/layouts/snapshot1685030100780.png")));
        Image image2 = null;
        TaskPictures instance = new TaskPictures(1,2,"deviceName","password",image2);
        Image expected = image;
        instance.setPicture(image);
        Image result = instance.getPicture();
        assertEquals(expected,result);
    }
}