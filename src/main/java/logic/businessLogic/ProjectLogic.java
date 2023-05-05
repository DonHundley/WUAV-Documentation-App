package logic.businessLogic;

import be.*;
import dal.*;

import java.util.*;

public class ProjectLogic {


    private ProjectDAO projectDAO = new ProjectDAO();
    private TaskDAO taskDAO = new TaskDAO();
    private PictureDAO pictureDAO = new PictureDAO();
    private WorksOnDAO worksOnDAO = new WorksOnDAO();

    /**
     * List of projects.
     *
     * @return This returns a list of all projects.
     */
    public List<Project> getProjects() {
        return projectDAO.getAllProjects();
    }
/**
 *This returns a list of all projects with total tasks */

    public List<ProjectWrapper> getProjectsWithTaskCount() {
        return projectDAO.getAllProjectsWithTaskCount();
    }

    /**
     * List of all tasks.
     *
     * @return This returns a list of all tasks.
     */
    public List<Task> getAllTasks() {
        return taskDAO.getAllTasks();
    }


    /**
     * List of all tasks with additional info from TaskWrapper.
     *
     * @return This returns a list of all tasks with info.
     */
    public List<TaskWrapper> getTasksInfo() {
        return taskDAO.getTasksInfo();
    }

    /**
     * Returns a curated list of tasks.
     *
     * @param selectedProject The project we wish to fetch tasks for.
     * @return This returns a list of tasks for the selected project.
     */
    public List<Task> getTasksByProject(Project selectedProject) {
        return taskDAO.getTaskByProject(selectedProject);
    }

    /**
     * Functions model uses this to add new pictures to our database.
     *
     * @param taskPictures is the pictures and descriptions to be added to the database.
     */
    public void addTaskPictures(TaskPictures taskPictures) {
        pictureDAO.createPicture(taskPictures);
    }



    /**
     * Functions model uses this to edit a specific task.
     *
     * @param task is the Task to be edited.
     */
    public void updateTask(Task task) {
        taskDAO.updateTask(task);
    }

    /**
     * Functions model uses this to create a new task.
     * @param task this is the task to be added to the database.
     */
    public void createTask(Task task) {taskDAO.createTask(task);}
    /**
     * Functions model uses this to create a new Project in our database.
     *
     * @param project is the Project to be added to the database.
     */
    public void createProject(Project project) {
        projectDAO.createProject(project);
    }

    /**
     * Functions model uses this to delete a project from the database.
     *
     * @param project is the Project to be deleted.
     */
    public void deleteProject(Project project) {
        projectDAO.deleteProject(project);
    }

    /**
     * Functions model uses this to update an event in the database.
     *
     * @param project is the Project to be updated.
     */
    public void editProject(Project project) {
        projectDAO.updateProject(project);
    }

    /**
     * Functions model uses this to assign a User to a Project.
     *
     * @param selectedUser    The user to be assigned to a project.
     * @param selectedProject The Project that the user will be assigned to.
     */
    public void assignProject(User selectedUser, Project selectedProject) {
        worksOnDAO.createWork(selectedUser, selectedProject);
    }

    /**
     * Functions model uses this to remove an assignment from a User.
     *
     * @param selectedUser    is the User to be removed from the Project.
     * @param selectedProject is the Project the User will be removed from.
     */
    public void removeAssignedProject(User selectedUser, Project selectedProject) {
        worksOnDAO.deleteWork(selectedUser, selectedProject);
    }

    public List<TaskPictures> getTaskPicturesByTask(Task task){return pictureDAO.getPictureByDocumentID(task);}

}
