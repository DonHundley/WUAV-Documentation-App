package be;

import org.junit.jupiter.api.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class ProjectTest {

    @Test
    void getProjID() {
        System.out.println("Get project ID");
        Project project = new Project(1, "Project 1", java.sql.Date.valueOf(java.time.LocalDate.now()), 1);
        int expected = 1;
        int result = project.getProjID();
        assertEquals(expected,result);
    }

    @Test
    void setProjID() {
        System.out.println("Set project ID");
        Project project = new Project(1, "Project 1", java.sql.Date.valueOf(java.time.LocalDate.now()), 1);
        int expected = 500;
        project.setProjID(500);
        int result = project.getProjID();
        assertEquals(expected, result);
    }

    @Test
    void getProjName() {
        System.out.println("Get project name");
        Project project = new Project(1, "Project 1", java.sql.Date.valueOf(java.time.LocalDate.now()), 1);
        String expected = "Project 1";
        String result = project.getProjName();
        assertEquals(result,expected);
    }

    @Test
    void setProjName() {
        System.out.println("Set project name");
        Project project = new Project(1, "Project 1", java.sql.Date.valueOf(java.time.LocalDate.now()), 1);
        String expected = "PROJECT X";
        project.setProjName("PROJECT X");
        String result = project.getProjName();
        assertEquals(expected,result);
    }

    @Test
    void getProjDate() {
        System.out.println("Get project date");
        Project project = new Project(1, "Project 1", java.sql.Date.valueOf(java.time.LocalDate.now()), 1);
        Date expected = java.sql.Date.valueOf(java.time.LocalDate.now());
        Date result = project.getProjDate();
        assertEquals(expected,result);
    }

    @Test
    void setProjDate() {
        System.out.println("Set project date");
        Project project = new Project(1, "Project 1", java.sql.Date.valueOf(java.time.LocalDate.now()), 1);
        Date expected = java.sql.Date.valueOf(java.time.LocalDate.ofYearDay(2000, 1));
        project.setProjDate(java.sql.Date.valueOf(java.time.LocalDate.ofYearDay(2000, 1)));
        Date result = project.getProjDate();
        assertEquals(expected,result);
    }

    @Test
    void getCustID() {
        System.out.println("Get customer ID");
        Project project = new Project(1, "Project 1", java.sql.Date.valueOf(java.time.LocalDate.now()), 1);
        int expected = 1;
        int result = project.getCustID();
        assertEquals(expected,result);
    }

    @Test
    void setCustID() {
        System.out.println("Set customer ID");
        Project project = new Project(1, "Project 1", java.sql.Date.valueOf(java.time.LocalDate.now()), 1);
        int expected = 5;
        project.setCustID(5);
        int result = project.getCustID();
        assertEquals(expected,result);
    }
}