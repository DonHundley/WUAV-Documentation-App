package gui.controller;

import be.Project;
import be.Task;
import gui.model.Functions;
import gui.model.Observables;
import gui.model.Persistent;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.Optional;


public class EditLayoutController {
    @FXML
    private TextField layoutTF;
    @FXML
    private ImageView layoutPreview;
    @FXML
    private Button editLayoutButton, cancelButton;

    private Image layoutImage;
    private String layoutImageAbsolute;

    private boolean updatedLayout;
    private Task selectedTask;
    private Project selectedProject;

    // Model
    private Functions functionsModel = new Functions();

    private Persistent persistenceModel = Persistent.getInstance();

    private Observables observablesModel = Observables.getInstance();

    /**
     * This method controls what we are doing with the chosen image file by the user.
     * It is triggered when the user activates the button to add a layout image.
     */
    @FXML
    private void imageFileExplorer(javafx.event.ActionEvent actionEvent) {
        try {
            FileChooser fileChooser = new FileChooser();
            setFileChooser(fileChooser);
            File file = fileChooser.showOpenDialog(new Stage());

            try {
                Path imagePath = FileSystems.getDefault().getPath(file.getPath());
                layoutImage = new Image(new FileInputStream(imagePath.toFile()));
                layoutImageAbsolute = file.getAbsolutePath();
                layoutPreview.setImage(layoutImage);
                layoutTF.setText(imagePath.toString());
                setUpdatedLayout(true);
            } catch (NullPointerException n) {
                String str = "There was a problem with selecting an image. Issue: NullPointerException.";
                taskError(str);
            }
        } catch (FileNotFoundException e) {
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
     * Method to update the layout for a task.
     */
    @FXML
    private void editLayout(ActionEvent actionEvent) {
        if (updatedLayout) {
            selectedTask.setTaskLayoutAbsolute(layoutImageAbsolute);
            functionsModel.updateLayout(selectedTask);
            observablesModel.loadTasksByProject(selectedProject);
            Stage stage = (Stage) editLayoutButton.getScene().getWindow();
            stage.close();
        } else {
            String str = "Please select a valid layout";
            taskError(str);
        }
    }

    /**
     * Closes the window with an action event.
     * @param actionEvent triggers when the user activates the cancel button.
     */
    @FXML
    private void cancel(ActionEvent actionEvent) {
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
        if (result.isPresent() && result.get() == ButtonType.OK) {
            alert.close();
        } else {
            alert.close();
        }
    }

    /**
     * sets the boolean updated layout.
     *
     * @param updatedLayout if true we then the user has either added or changed the layout image to the task.
     */
    public void setUpdatedLayout(boolean updatedLayout) {
        this.updatedLayout = updatedLayout;
    }

    /**
     * Method to set selected task and setting the layout preview if available*/
    public void setLayoutOnEdit() {
        selectedTask = persistenceModel.getSelectedTask();
        if (selectedTask.getTaskLayout() != null) {
            layoutPreview.setImage(selectedTask.getTaskLayout());
        }

    }

    /**
     * This controller needs the selected project in order to get a list of tasks for it.
     * We fetch the selected project from persistent in order to do this, if there is a problem with this, we show an alert.
     */
    public void setSelectedProjectForLayout(){
        if(persistenceModel.getSelectedProject() != null){
            selectedProject = persistenceModel.getSelectedProject();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Project does not exist.");
            alert.setHeaderText("An error has occurred, please contact system admin. Project was not selected or does not exist.");

            Optional<ButtonType> result = alert.showAndWait();
            if(result.get() == ButtonType.OK){
                alert.close();
            }
        }
    }
}
