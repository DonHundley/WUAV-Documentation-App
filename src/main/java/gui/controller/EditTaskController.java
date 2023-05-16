package gui.controller;

import be.*;
import gui.model.*;
import javafx.event.ActionEvent;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.stage.*;


import java.util.*;

public class EditTaskController {


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
    private Functions functionsModel = new Functions();

    private Persistent persistenceModel = Persistent.getInstance();
    private Observables observables = Observables.getInstance();


    // Instance of our task to be edited.
    private Task selectedTask;

    // Variables
    private String[] states = {"Not Started", "In Progress", "Completed"};

    int maxDescription = 255;

    /**
     * This is used to set our fields with relevant information if that information exists.
     * This helps the user see what information already exists in a given task while they are editing it.
     */
    public void setFieldsOnEdit() {
        selectedTask = persistenceModel.getSelectedTask();
        setStateSelection();

        // This will set the text field with the currently input description, if there is one.
        if (selectedTask.getTaskDesc() != null) {
            taskDescription.setText(selectedTask.getTaskDesc());
        }

    }

    /**
     * We add all of our states into our state selection combobox and then set the selected value to the current value stored in our selectedTask.
     */
    private void setStateSelection() {
        for (String state : states) {
            stateSelection.getItems().add(state);
        }
        stateSelection.setValue(selectedTask.getTaskState());
        stateSelection.setOnAction(this::changeState);
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


        if (!taskDescription.getText().isEmpty()) {
            if (validateDescTFLength()) {
                selectedTask.setTaskDesc(taskDescription.getText());
            } else {
                alertDescriptionTFLength();
                return;
            }
            functionsModel.updateTask(selectedTask);
            observables.loadTasksByProject(persistenceModel.getSelectedProject());

            Node source = (Node) actionEvent.getSource();
            Stage stage = (Stage) source.getScene().getWindow();
            stage.close();
        } else {

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
     * We use this to display an error to the user if there is a problem.
     *
     * @param str This is the source of the problem so that the user is informed.
     */
    private void taskError(String str) {
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
