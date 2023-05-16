package gui.controller;

import be.*;
import gui.model.*;
import javafx.event.ActionEvent;
import javafx.fxml.*;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.stage.*;

import javax.swing.*;

import java.net.URL;
import java.util.*;

public class NewTaskController implements Initializable {

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
    private Observables observablesModel = Observables.getInstance();
    private Persistent persistentModel = Persistent.getInstance();
    private Functions functionsModel = new Functions();

    // The project to have a task added.
    private Project selectedProject;

    //variables for input validation
    private int maxTaskName = 25;


    /**
     * This method is used to set our models and which project a task will be added to.
     *
     * @param persistentModel the instance of the persistent model
     * @param functionsModel  the instance of the functions model
     */
    public void setNewTaskController(Persistent persistentModel, Functions functionsModel) {
        this.persistentModel = persistentModel;
        this.functionsModel = functionsModel;

        setSelectedProject();
        windowTitleLabel.setText("New User");
    }

    private void setSelectedProject() {
        if (persistentModel.getSelectedProject() != null) {
            this.selectedProject = persistentModel.getSelectedProject();
        } else {
            String str = "Selected project could not be found, please contact system admin. Error location: NewTaskController.";
            newTaskError(str);
        }
    }

    private void constructTask() {
        if (selectedProject != null && !taskName.getText().isEmpty()) {
            if (validateTaskNameTFLength()) {
                Task task = new Task(selectedProject.getProjID(), taskName.getText(), "No description", "Not Started");
                functionsModel.createTask(task);
            } else {
                alertTaskNameTF();
            }
        } else {
            String str = "Either the selected project does not exist or a name has not been chosen for the task.";
            newTaskError(str);
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
        observablesModel.loadProjects();
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

    /**
     * We use this to display an error to the user if there is a problem.
     *
     * @param str This is the source of the problem so that the user is informed.
     */
    private void newTaskError(String str) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Error");
        alert.setHeaderText(str);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            alert.close();
        } else {
            alert.close();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setSelectedProject();
    }
}
