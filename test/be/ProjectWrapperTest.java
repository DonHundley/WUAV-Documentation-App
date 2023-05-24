package be;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

class ProjectWrapperTest {

    @Test
    void getProject() {
        System.out.println("Get project");
        Project project = new Project(1, "Project 1", java.sql.Date.valueOf(java.time.LocalDate.now()), 1);
        ProjectWrapper projectWrapper = new ProjectWrapper(project, 5);

        assertEquals(project, projectWrapper.getProject());
    }

    @Test
    void getTotalTasks() {
        System.out.println("Get total tasks");
        Project project = new Project(1, "Project 1", java.sql.Date.valueOf(java.time.LocalDate.now()), 1);
        ProjectWrapper projectWrapper = new ProjectWrapper(project, 5);
        int expected = 5;

        assertEquals(expected, projectWrapper.getTotalTasks());
    }
}