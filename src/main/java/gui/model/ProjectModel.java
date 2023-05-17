package gui.model;

import be.*;
import javafx.collections.*;
import javafx.scene.image.*;
import logic.businessLogic.*;
import logic.documentPDF.*;

import java.util.*;

public class ProjectModel {

    private ProjectLogic projectLogic = new ProjectLogic();
    private PDFHandler pdfHandler = new PDFHandler();

    private Task selectedTask;
    private Project selectedProject;

    private ObservableList<ProjectWrapper> projects = FXCollections.observableArrayList();
    private ObservableList<Task> allTasks = FXCollections.observableArrayList();
    private ObservableList<TaskWrapper> tasksByUser = FXCollections.observableArrayList();
    private ObservableList<TaskWrapper> tasksInfo = FXCollections.observableArrayList();
    private ObservableList<Task> tasksByProject = FXCollections.observableArrayList();


    /**
     * Thread safe singleton of ProjectModel.
     */
    private ProjectModel(){}
    private static ProjectModel instance;
    public static synchronized ProjectModel getInstance(){
        if(instance == null){
            instance = new ProjectModel();
        }
        return instance;
    }

    /**
     * These methods get our observable lists.
     */
    public ObservableList<ProjectWrapper> getProjects() {return projects;}

    public ObservableList<Task> getAllTasks(){return allTasks;}
    public ObservableList<TaskWrapper> getTasksInfo(){return tasksInfo;}

    public ObservableList<Task> getTasksByProject() {return tasksByProject;}
    public ObservableList<TaskWrapper> getTasksByUserID(){return tasksByUser;}

    /**
     * These methods load our lists.
     */
    public void loadProjects() {
        projects.clear();
        projects.addAll(projectLogic.getProjectsWithTaskCount());
    }
    public void loadAllTasks(){
        allTasks.clear();
        allTasks.addAll(projectLogic.getAllTasks());
    }
    public void loadTasksInfo(){
        tasksInfo.clear();
        tasksInfo.addAll(projectLogic.getTasksInfo());
    }
    public void loadTasksByProject(){
        tasksByProject.clear();
        tasksByProject.addAll(projectLogic.getTasksByProject(selectedProject));
    }

    public void loadProjectsByUser(User user){
        tasksByUser.clear();
        tasksByUser.addAll(projectLogic.tasksByUserID(user));
    }

    /**
     * Task methods
     */
    public Task getSelectedTask() {
        return selectedTask;
    }
    public void setSelectedTask(Task selectedTask) {
        this.selectedTask = selectedTask;
    }
    public void addTaskPictures(TaskPictures taskPictures) {
        projectLogic.addTaskPictures(taskPictures);
    }
    public void createTask(Task task) {
        projectLogic.createTask(task);
    }
    public void updateTask() {
        projectLogic.updateTask(selectedTask);
    }
    public void updateLayout() {
        projectLogic.updateLayout(selectedTask);
    }

    /**
     * Project methods
     */
    public Project getSelectedProject() {
        return selectedProject;
    }
    public void setSelectedProject(Project selectedProject) {
        this.selectedProject = selectedProject;
    }
    public void createProject(Project project) {
        projectLogic.createProject(project);
    }
    public void deleteProject() {
        projectLogic.deleteProject(selectedProject);
    }
    public void editProject(Project project) {
        projectLogic.editProject(project);
    }
    public void assignProject(User selectedUser, Project selectedProject) {
        projectLogic.assignProject(selectedUser, selectedProject);
    }
    public void removeProjectAssign(User selectedUser, Project selectedProject) {
        projectLogic.removeAssignedProject(selectedUser, selectedProject);
    }


    /**
     * TaskPicture Methods.
     */
    public List<TaskPictures> taskPicturesByDocID(Task task) {
        return projectLogic.getTaskPicturesByTask(task);
    }

    /**
     * The method called to export a pdf of a specific project.
     */
    public void exportReport(Customer customer, Project project, Task task, Image image1, Image image2, String deviceNames, String devicePasswords) {
        pdfHandler.exportReport(customer,project,task,image1,image2, deviceNames,devicePasswords);
    }
}
