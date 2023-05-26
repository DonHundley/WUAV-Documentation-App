package gui.model;

import be.*;
import javafx.collections.*;
import javafx.scene.image.*;
import logic.businessLogic.*;
import logic.documentPDF.*;

import java.util.*;

public class ProjectModel {
    private final ProjectLogic projectLogic;
    private final PDFHandler pdfHandler;

    private Task selectedTask;
    private Project selectedProject;
    private int imageIndex;

    private final ObservableList<ProjectWrapper> projects;
    private final ObservableList<Task> allTasks;
    private final ObservableList<TaskWrapper> tasksByUser;
    private final ObservableList<TaskWrapper> tasksInfo;
    private final ObservableList<Task> tasksByProject;


    /**
     * Thread safe singleton of ProjectModel.
     */
    private ProjectModel(){
        projectLogic = new ProjectLogic();
        projects = FXCollections.observableArrayList();
        allTasks = FXCollections.observableArrayList();
        tasksByUser = FXCollections.observableArrayList();
        tasksInfo = FXCollections.observableArrayList();
        tasksByProject = FXCollections.observableArrayList();
        pdfHandler = new PDFHandler();
    }
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
    public ObservableList<TaskWrapper> getTasksInfo(){return tasksInfo;}

    public ObservableList<Task> getTasksByProject() {return tasksByProject;}
    public ObservableList<TaskWrapper> getTasksByUserID(){return tasksByUser;}

    /**
     * These methods load our lists.
     */

    public void loadAllProjLists(){
        loadAllTasks();
        loadProjects();
        loadTasksByProject();
        loadTasksInfo();
    }
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
        if(selectedProject != null){
        tasksByProject.clear();
        tasksByProject.addAll(projectLogic.getTasksByProject(selectedProject));}
    }

    public void loadProjectsByUser(User user){
        tasksByUser.clear();
        tasksByUser.addAll(projectLogic.tasksByUserID(projectLogic.getProjectIDsByUserID(user), projectLogic.getTasksInfo()));
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




    /**
     * TaskPicture Methods.
     */
    public int getImageIndex() {return imageIndex;}

    public void setImageIndex(int imageIndex) {this.imageIndex = imageIndex;}
    public List<TaskPictures> taskPicturesByDocID() {
        return projectLogic.getTaskPicturesByTask(selectedTask);
    }
    public void createTaskPicturesList(List<TaskPictures> taskPictures) {
        projectLogic.createTaskPictureLists(taskPictures);
    }
    public List<Image> getTaskPictureImages(){return projectLogic.getImageList();}
    public String getTaskPictureDevices(){return projectLogic.getDeviceString();}
    public String getTaskPictureCredentials(){return projectLogic.getDeviceCredentialsString();}

    public int getLocationX(int imageCount, int imageWidth){ return projectLogic.imageLocationX(imageCount, imageWidth);}
    public int getLocationY(int imageCount, int imageHeight){return projectLogic.imageLocationY(imageCount,imageHeight);}

    /**
     * The method called to export a pdf of a specific project.
     */
    public void exportReport(Customer customer, Project project, Task task, Image image1, Image image2, String deviceNames, String devicePasswords) {
        pdfHandler.exportReport(customer,project,task,image1,image2, deviceNames,devicePasswords);
    }
}
