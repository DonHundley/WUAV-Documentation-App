package bll;

import be.*;
import dal.*;

import java.util.*;

public class ProjectLogic {

    private ProjectDAO projectDAO = new ProjectDAO();
    private TaskDAO taskDAO = new TaskDAO();
    private PictureDAO pictureDAO = new PictureDAO();
    private WorksOnDAO worksOnDAO = new WorksOnDAO();

    public List<Project> getProjects() {
        return projectDAO.getAllProjects();
    }
    public List<Task> getTasksByProject(Project selectedProject) {return taskDAO.getTaskByProject(selectedProject.getProjID());}

    public void addTaskPictures(TaskPictures taskPictures) {
        pictureDAO.createPicture(taskPictures);
    }

    public void updateTask(Task task) {
        taskDAO.updateTask(task);
    }

    public void createProject(Project project) {
        projectDAO.createProject(project);
    }

    public void deleteProject(Project project) {
        projectDAO.deleteProject(project);
    }

    public void editProject(Project project) {
        projectDAO.updateProject(project);
    }

    public void assignProject(User selectedUser, Project selectedProject) {
        worksOnDAO.createWork(selectedUser, selectedProject);
    }

    public void removeAssignedProject(User selectedUser, Project selectedProject) {
        worksOnDAO.deleteWork(selectedUser, selectedProject);
    }
}
