package gui.controller.newAndUpdateControllers;

import be.*;
import gui.controller.mainViewControllers.*;
import gui.model.*;
import javafx.fxml.*;
import javafx.scene.control.*;

import javafx.stage.*;
import org.apache.logging.log4j.*;

import java.net.URL;
import java.util.*;

public class NewTaskController extends BaseController implements Initializable {

    // FXML
    @FXML
    private TextField taskName;
    @FXML
    private Button cancelButton;
    @FXML
    private Button createTask;

    // Model instances
    private final ProjectModel projectModel = ProjectModel.getInstance();

    // The project to have a task added.
    private Project selectedProject;


    private static final Logger logger = LogManager.getLogger("debugLogger");


    private void setSelectedProject() {
        logger.trace("Setting selected project for new Task.");
        if (projectModel.getSelectedProject() != null) {
            this.selectedProject = projectModel.getSelectedProject();
        } else {
            logger.warn("There was a problem finding the project for setSelectedProject() in NewTaskController.");
            String str = "Selected project could not be found, please contact system admin. Error location: NewTaskController.";
            super.createWarning(str);
        }
    }

    private void constructTask() {
        logger.info("Creating a new Task");
        if (selectedProject != null && !taskName.getText().isEmpty()) {
            if (validateTaskNameTFLength()) {
                Task task = new Task(selectedProject.getProjID(), taskName.getText(), "No description", "Not Started");
                projectModel.createTask(task);
                projectModel.loadAllProjLists();
                logger.info("Task created");
            } else {
                alertTaskNameTF();
                logger.warn("Task creation failed: Task name exceeds the maximum length");
            }
        } else {
            String str = "Either the selected project does not exist or a name has not been chosen for the task.";
            super.createWarning(str);
            logger.warn("Task creation failed: empty field for task name");
        }
    }

/** This method checks if the length of the textfield is bigger than the max length for the field
 * it returns true if the length is okay, false if it's too long**/
    private boolean validateTaskNameTFLength() {
        //variables for input validation
        int maxTaskName = 25;
        return taskName.getText().length() <= maxTaskName;
    }

    /**this method shows an alert to the user if the inserted text field length exceeds the max **/
    private void alertTaskNameTF() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Validate Task Name");
        alert.setContentText("Task name is too long, max is 25 characters.");
        alert.showAndWait();
    }

    @FXML
    private void createTask() {
        logger.trace("Creating new task.");
        constructTask();
        projectModel.loadProjects();
        Stage stage = (Stage) createTask.getScene().getWindow();
        stage.close();
    }

    /**
     * Closes the window with an action event.
     *
     */
    @FXML
    private void cancel() {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setSelectedProject();
    }
}
