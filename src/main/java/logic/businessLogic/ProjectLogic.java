package logic.businessLogic;

import be.*;
import dal.*;
import javafx.scene.image.*;
import org.apache.logging.log4j.*;

import java.util.*;

public class ProjectLogic {


    private final ProjectDAO projectDAO;
    private final TaskDAO taskDAO;
    private final PictureDAO pictureDAO;
    private final WorksOnDAO worksOnDAO;

    private static final Logger logger = LogManager.getLogger("debugLogger");
    private List<Image> imageList;
    private List<String> deviceList;
    private List<String> deviceCredentials;

    public ProjectLogic() {
        projectDAO = new ProjectDAO();
        taskDAO = new TaskDAO();
        pictureDAO = new PictureDAO();
        worksOnDAO = new WorksOnDAO();
        imageList = new ArrayList<>();
        deviceList = new ArrayList<>();
        deviceCredentials = new ArrayList<>();
    }


    /**
     * List of projects.
     *
     * @return This returns a list of all projects.
     */
    public List<Project> getProjects() {
        return projectDAO.getAllProjects();
    }

    /**
     * This returns a list of all projects with total tasks
     */
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
        return taskDAO.getTaskByProject(selectedProject.getProjID());
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
     * Functions model uses this to edit the layout of a specific task.
     * * @param task is the Task to be edited.
     */
    public void updateLayout(Task task) {
        taskDAO.updateLayout(task);
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

    public List<Integer> getProjectIDsByUserID(User user){
        return worksOnDAO.getProjectIDsByUserID(user);
    }

    /**
     * Create a list of tasks by user ID. Used to give Technicians a list of their tasks.
     * @param projectIDsByUserIDs The list of project ids associated with the tech.
     * @param getTasksInfo list of all TaskWrappers
     * @return a list of only the tasks associated with the user.
     */
    public List<TaskWrapper> tasksByUserID(List<Integer> projectIDsByUserIDs, List<TaskWrapper> getTasksInfo){
        logger.info("Creating list in ProjectLogic. tasksByUserID()");
        List<TaskWrapper> tasksByUserID = new ArrayList<>();


        logger.info("Checking lists projectIDsByUserIDs and getTasksInfo");
        if(getTasksInfo.isEmpty()){
            logger.warn("List getTasksInfo is empty!");
        }
        if(projectIDsByUserIDs.isEmpty()){
            logger.warn("List projectIDsByUserIDs is empty!");
        }

        logger.info("Checking lists complete. Iterating over list projectIDsByUserIDs");
        for (int projectID: projectIDsByUserIDs) {
            for (TaskWrapper project: getTasksInfo
                 ) {
                if(project.getProject().getProjID() == projectID){
                    tasksByUserID.add(project);
                }
            }
        }
        logger.info("Returning list of tasksByUserID, process complete.");
        return tasksByUserID;
    }

    /**
     * Creates three lists from every given task pictures. A list of pictures, a list of devices, and a list of credentials.
     * @param taskPictures is the list of TaskPictures which is associated to a given task.
     */
    public void createTaskPictureLists(List<TaskPictures> taskPictures){
        imageList.clear();
        deviceList.clear();
        deviceCredentials.clear();
        int deviceCount = 1;
        for (TaskPictures pics : taskPictures) {
            if(pics.getPicture() != null){
                imageList.add(pics.getPicture());
            }
            if(pics.getDeviceName() != null) {
                deviceList.add("Device " + deviceCount + ": " + pics.getDeviceName() + ", ");
                if(pics.getPassword() != null){
                    deviceCredentials.add("Device " + deviceCount +": " + pics.getPassword() + ", ");
                }
                deviceCount++;
            }
        }

    }

    /**
     * List of images from TaskPictures
     * @return the image list made by createTaskPictureLists
     */
    public List<Image> getImageList() {
        return imageList;
    }

    /**
     * List of devices from TaskPictures
     * @return the image list made by createTaskPictureLists
     */
    public List<String> getDeviceList() {return deviceList;}

    /**
     * List of credentials from TaskPictures
     * @return the image list made by createTaskPictureLists
     */
    public List<String> getDeviceCredentials() {return deviceCredentials;}

    /**
     * Converts the device list to a string
     * @return String of all devices in device list
     */
    public String getDeviceString() {
        StringBuilder names = new StringBuilder();
        if(!deviceList.isEmpty()) {
            for (String device : deviceList
            ) {
                names.append(device);
            }
        }
        return names.toString();
    }

    /**
     * Converts the credentials into a string
     * @return String of all the credentials in deviceCredentials
     */
    public String getDeviceCredentialsString() {
        StringBuilder credentials = new StringBuilder();
        if(!deviceCredentials.isEmpty()){
            for (String credential: deviceCredentials
            ) {
                credentials.append(credential);
            }
        }
        return credentials.toString();
    }

    /**
     * This method will return an int to represent the x coordinate that an image will be placed at on the GUI.
     * @param imgCount The number of a given image to have a location found.
     *                 For example if there are 5 images, but we are finding the location for image 3, this would be 3.
     * @param imgWidth The width of the images to be placed on the GUI.
     * @return an integer representing the X coordinate.
     */
    public int imageLocationX(int imgCount, int imgWidth) {
        logger.trace("Determining image X coordinates.");
        int getX;
        int spacing;
        if (imgCount <= 4) {
            getX = imgCount * imgWidth;
            spacing = imgCount * 5;
            return getX - imgWidth + spacing;
        } else if (imgCount <= 8) {
            imgCount = imgCount - 4;
            getX = imgCount * imgWidth;
            spacing = imgCount * 5;
            return getX - imgWidth + spacing;
        } else if (imgCount <= 12) {
            imgCount = imgCount - 8;
            getX = imgCount * imgWidth;
            spacing = imgCount * 5;
            return getX - imgWidth + spacing;
        } else if (imgCount <= 16) {
            imgCount = imgCount - 12;
            getX = imgCount * imgWidth;
            spacing = imgCount * 5;
            return getX - imgWidth + spacing;
        } else {
            imgCount = imgCount - 16;
            getX = imgCount * imgWidth;
            spacing = imgCount * 5;
            return getX - imgWidth + spacing;
        }
    }

    /**
     * This method will return an int to represent the y coordinate that an image will be placed at on the GUI.
     * @param imgCount The number of a given image to have a location found.
     *                 For example if there are 5 images, but we are finding the location for image 3, this would be 3.
     * @param imgHeight The height of the images to be placed on the GUI.
     * @return an interger representing the y coordinate.
     */
    public int imageLocationY(int imgCount, int imgHeight) {
        logger.trace("Determining image y coordinates");
        int getY;

        if (imgCount <= 4) {
            return 0;
        } else if (imgCount <= 8) {
            return imgHeight + 5;
        } else if (imgCount <= 12) {

            getY = imgHeight * 2;
            return getY + 10;
        } else if (imgCount <= 16) {
            getY = imgHeight * 3;
            return getY + 15;
        } else {
            getY = imgHeight * 4;
            return getY + 20;
        }
    }
}
