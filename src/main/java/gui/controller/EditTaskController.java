package gui.controller;

import be.*;
import gui.model.*;
import javafx.fxml.*;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.stage.*;

import java.awt.event.*;
import java.io.*;
import java.nio.file.*;
import java.util.*;

public class EditTaskController {
    
    // FXML
    @FXML private TextField taskDescription;
    @FXML private ComboBox<String> stateSelection;
    @FXML private Label windowTitleLabel;
    @FXML private Label errorLabel;
    @FXML private Button cancelButton;

    // Model
    private Functions functionsModel;

    private Persistent persistenceModel;

    // Image
    private Image layoutImage;
    private ImageView layoutPreview;

    // Instance of our task to be edited.
    private Task selectedTask;

    // Variables
    private boolean updatedLayout;
    private String[] states = {"Not Started", "In Progress", "Completed"};


    /**
     * This is used to set our fields with relevant information if that information exists.
     * This helps the user see what information already exists in a given task while they are editing it.
     * @param functionsModel This is used to assure we have the same instance of the functions model.
     * @param persistenceModel This is used to assure we have the same instance of the persistent model.
     */
    public void setFieldsOnEdit(Functions functionsModel, Persistent persistenceModel){
        this.functionsModel = functionsModel;
        this.selectedTask = persistenceModel.getSelectedTask();

        setStateSelection();
        setUpdatedLayout(false);
        // This will set the layout preview if there is an image available.
        if(selectedTask.getTaskLayout() != null){
            layoutPreview.setImage(selectedTask.getTaskLayout());
        }
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
     * This method controls what we are doing with the chosen image file by the user.
     * It is triggered when the user activates the button to add a layout image.
     */
    @FXML private void imageFileExplorer(javafx.event.ActionEvent actionEvent) {
        try {
            FileChooser fileChooser = new FileChooser();
            setFileChooser(fileChooser);
            File file = fileChooser.showOpenDialog(new Stage());

            try{
                Path imagePath = FileSystems.getDefault().getPath(file.getPath());
                layoutImage = new Image(new FileInputStream(imagePath+file.getName()));
                layoutPreview.setImage(layoutImage);
                setUpdatedLayout(true);
            }catch (NullPointerException n){
                String str = "There was a problem with selecting an image. Issue: NullPointerException.";
                taskError(str);
            }
        } catch(IOException e){
            String str = "There was a problem with selecting an image. Issue: IOException.";
            taskError(str);
        }
    }

    /**
     * Method to configure the file chooser and select which file types are accepted
     */
    private static void setFileChooser(FileChooser fileChooser) {
        fileChooser.setTitle("Select layout diagram Image");
        fileChooser.setInitialDirectory(
                new File(System.getProperty("user.home"))
        );
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image files", "*.jpg", "*.bmp", "*.png", "*.jpeg")
        );
    }

    /**
     * This will update the selected task with any changes the user has done.
     * @param actionEvent This triggers when the user activates the edit task button.
     */
    @FXML private void editTask(ActionEvent actionEvent){
        if(updatedLayout){
            selectedTask.setTaskLayout(layoutImage);
        }

        if(taskDescription.getText() != null){
            selectedTask.setTaskDesc(taskDescription.getText());
        }

        functionsModel.updateTask(selectedTask);
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

    /**
     * sets the boolean updated layout.
     * @param updatedLayout if true we then the user has either added or changed the layout image to the task.
     */
    public void setUpdatedLayout(boolean updatedLayout) {
        this.updatedLayout = updatedLayout;
    }
}
