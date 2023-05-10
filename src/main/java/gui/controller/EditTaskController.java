package gui.controller;

import be.*;
import gui.model.*;
import javafx.event.ActionEvent;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.stage.*;


import java.io.*;
import java.nio.file.*;
import java.util.*;

public class EditTaskController {


    // FXML
    @FXML private TextArea taskDescription;


    @FXML private ComboBox<String> stateSelection;
    @FXML private Label errorLabel;
    @FXML private Button cancelButton;

    // Model
    private Functions functionsModel = new Functions();

    private Persistent persistenceModel = Persistent.getInstance();
    private Observables observables = Observables.getInstance();



    // Instance of our task to be edited.
    private Task selectedTask;

    // Variables
    private String[] states = {"Not Started", "In Progress", "Completed"};



    /**
     * This is used to set our fields with relevant information if that information exists.
     * This helps the user see what information already exists in a given task while they are editing it.
     */
    public void setFieldsOnEdit(){
        selectedTask = persistenceModel.getSelectedTask();
        setStateSelection();

        // This will set the text field with the currently input description, if there is one.
        if(selectedTask.getTaskDesc() != null){
        taskDescription.setText(selectedTask.getTaskDesc());
        }

    }

    /**
     * We add all of our states into our state selection combobox and then set the selected value to the current value stored in our selectedTask.
     */
    private void setStateSelection(){
        for (String state : states){
            stateSelection.getItems().add(state);
        }
        stateSelection.setValue(selectedTask.getTaskState());
        stateSelection.setOnAction(this::changeState);
    }

    /**
     * Changes the state of the task.
     * @param actionEvent This triggers when the user selects an option in the selected task combobox.
     */
    private void changeState(javafx.event.ActionEvent actionEvent) {
        selectedTask.setTaskState(stateSelection.getValue());
    }



    /**
     * This will update the selected task with any changes the user has done.
     * @param actionEvent This triggers when the user activates the edit task button.
     */
    @FXML private void editTask(ActionEvent actionEvent){


        if(!taskDescription.getText().isEmpty()){
            selectedTask.setTaskDesc(taskDescription.getText());
        }
        functionsModel.updateTask(selectedTask);
        observables.loadTasksByProject(persistenceModel.getSelectedProject());

        Node source = (Node) actionEvent.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }


    /**
     * Closes the window with an action event.
     * @param actionEvent triggers when the user activates the cancel button.
     */
    @FXML private void cancel(ActionEvent actionEvent){
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    /**
     * We use this to display an error to the user if there is a problem.
     * @param str This is the source of the problem so that the user is informed.
     */
    private void taskError(String str) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Error");
        alert.setHeaderText(str);

        Optional<ButtonType> result = alert.showAndWait();
        if(result.isPresent() && result.get() == ButtonType.OK){
            alert.close();
        } else {
            alert.close();
        }
    }


}
