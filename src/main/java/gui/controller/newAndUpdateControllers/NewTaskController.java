package gui.controller.newAndUpdateControllers;

import be.*;
import gui.controller.mainViewControllers.*;
import gui.model.*;
import javafx.event.ActionEvent;
import javafx.fxml.*;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.stage.*;

import javax.swing.*;

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
        if (selectedProject != null && !taskName.getText().isEmpty()) {
            Task task = new Task(selectedProject.getProjID(), taskName.getText(), "No description", "Not Started");
            projectModel.createTask(task);
        } else {
            String str = "Either the selected project does not exist or a name has not been chosen for the task.";
            super.createWarning(str);
        }
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
