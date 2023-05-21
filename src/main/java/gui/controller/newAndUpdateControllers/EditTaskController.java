package gui.controller.newAndUpdateControllers;

import be.*;
import gui.controller.mainViewControllers.BaseController;
import gui.model.*;
import javafx.event.ActionEvent;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.stage.*;
import org.apache.logging.log4j.*;


import java.util.*;

public class EditTaskController extends BaseController {


    // FXML
    @FXML
    private TextArea taskDescription;


    @FXML
    private ComboBox<String> stateSelection;
    @FXML
    private Label errorLabel;
    @FXML
    private Button cancelButton;


    // Model
    private ProjectModel projectModel = ProjectModel.getInstance();


    // Instance of our task to be edited.
    private Task selectedTask;

    // Variables
    private String[] states = {"Not Started", "In Progress", "Completed"};

    int maxDescription = 255;
    private static final Logger logger = LogManager.getLogger("debugLogger");


    /**
     * This is used to set our fields with relevant information if that information exists.
     * This helps the user see what information already exists in a given task while they are editing it.
     */
    public void setFieldsOnEdit() {
        logger.trace("setting fields for task to be edited.");
        selectedTask = projectModel.getSelectedTask();
        setStateSelection();

        // This will set the text field with the currently input description, if there is one.
        if (selectedTask.getTaskDesc() != null) {
            taskDescription.setText(selectedTask.getTaskDesc());
        }
        logger.trace("fields set.");
    }

    /**
     * We add all of our states into our state selection combobox and then set the selected value to the current value stored in our selectedTask.
     */
    private void setStateSelection() {
        logger.trace("Selecting task state.");
        for (String state : states) {
            stateSelection.getItems().add(state);
        }
        stateSelection.setValue(selectedTask.getTaskState());
        stateSelection.setOnAction(this::changeState);
        logger.trace("State changed.");
    }

    /**
     * Changes the state of the task.
     *
     * @param actionEvent This triggers when the user selects an option in the selected task combobox.
     */
    private void changeState(javafx.event.ActionEvent actionEvent) {
        selectedTask.setTaskState(stateSelection.getValue());
    }


    /**
     * This will update the selected task with any changes the user has done.
     *
     * @param actionEvent This triggers when the user activates the edit task button.
     */
    @FXML
    private void editTask(ActionEvent actionEvent) {
        logger.info("Editing a task");

        if (!taskDescription.getText().isEmpty()) {
            if (validateDescTFLength()) {
                selectedTask.setTaskDesc(taskDescription.getText());

            } else {
                alertDescriptionTFLength();
                logger.warn("Task editing failed:task name exceeds the maximum length");
                return;
            }
            projectModel.updateTask();
            logger.info("Task edited");
            projectModel.loadAllProjLists();

            Node source = (Node) actionEvent.getSource();
            Stage stage = (Stage) source.getScene().getWindow();
            stage.close();
        } else {
            String str = "No name chosen for the task to edit";
            super.createWarning(str);
            logger.warn("Task editing failed: empty field for task name");
        }
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
     * This method checks if the length of the textfield is bigger than the max length for the field
     * it returns true if the length is okay, false if it's too long
     **/

    private boolean validateDescTFLength() {
        if (taskDescription.getText().length() > maxDescription) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * this method shows an alert to the user if the inserted text field length exceeds the max
     **/
    private void alertDescriptionTFLength() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Validate Description");
        alert.setContentText("The description is too long, max is 255 characters.");
        alert.showAndWait();
    }

}
