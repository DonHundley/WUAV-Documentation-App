package gui.controller.newAndUpdateControllers;

import be.*;
import gui.controller.mainViewControllers.*;
import gui.model.*;
import javafx.event.ActionEvent;
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
    private Label windowTitleLabel;
    @FXML
    private Label errorLabel;
    @FXML
    private Button cancelButton;
    @FXML
    private Button createTask;

    // Model instances
    private ProjectModel projectModel = ProjectModel.getInstance();

    // The project to have a task added.
    private Project selectedProject;

    //variables for input validation
    private int maxTaskName = 25;


    private static final Logger logger = LogManager.getLogger("debugLogger");

    /**
     * This method is used to set our models and which project a task will be added to.
     */
    public void setNewTaskController() {
        setSelectedProject();
        windowTitleLabel.setText("New User");
    }

    private void setSelectedProject() {
        if (projectModel.getSelectedProject() != null) {
            this.selectedProject = projectModel.getSelectedProject();
        } else {
            String str = "Selected project could not be found, please contact system admin. Error location: NewTaskController.";
            super.createWarning(str);
        }
    }

    private void constructTask() {
        logger.info("Creating a new Task");
        if (selectedProject != null && !taskName.getText().isEmpty()) {
            if (validateTaskNameTFLength()) {
                Task task = new Task(selectedProject.getProjID(), taskName.getText(), "No description", "Not Started");
                functionsModel.createTask(task);
                logger.info("Task created");
            } else {
                alertTaskNameTF();
                logger.warn("Task creation failed: Task name exceeds the maximum length");
            }
        } else {
            String str = "Either the selected project does not exist or a name has not been chosen for the task.";
            newTaskError(str);
            logger.warn("Task creation failed: empty field for task name");
        }
    }

/** This method checks if the length of the textfield is bigger than the max length for the field
 * it returns true if the length is okay, false if it's too long**/
    private boolean validateTaskNameTFLength() {
        if (taskName.getText().length() > maxTaskName) {
            return false;
        } else {
            return true;
        }
    }

    /**this method shows an alert to the user if the inserted text field length exceeds the max **/
    private void alertTaskNameTF() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Validate Task Name");
        alert.setContentText("Task name is too long, max is 25 characters.");
        alert.showAndWait();
    }

    @FXML
    private void createTask(ActionEvent actionEvent) {
        constructTask();
        projectModel.loadProjects();
        Stage stage = (Stage) createTask.getScene().getWindow();
        stage.close();
    }

    /**
     * Closes the window with an action event.
     *
     * @param actionEvent triggers when the user activates the cancel button.
     */
    @FXML
    private void cancel(ActionEvent actionEvent) {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setSelectedProject();
    }
}
