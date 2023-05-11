package gui.controller;

import be.*;
import gui.model.*;
import javafx.event.ActionEvent;
import javafx.fxml.*;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.stage.*;


import java.io.*;
import java.nio.file.*;
import java.util.*;

public class AddTaskPicturesController {
    @FXML private TextArea deviceNameTA;
    @FXML private TextArea deviceCredTA;
    @FXML private ImageView pictureImageView;

    @FXML private TextField  pictureTF;

    @FXML private Button cancelButton;

    @FXML private Button createTaskPictures;

    // Models
    private Persistent persistenceModel = Persistent.getInstance();
    private Observables observablesModel = Observables.getInstance();
    private Functions functionsModel = new Functions();

    // Task to have pictures added to
    private Task task = persistenceModel.getSelectedTask();

    private Image picture;

    private String pictureAbsolute;


    /**
     * Opens the filechooser for pictures.
     * @param actionEvent triggers when the user activates the after picture button.
     */
    @FXML private void openFileChooser(ActionEvent actionEvent){
        imageFileExplorer();
    }


    /**
     * Creates a new TaskPictures from the filled fields.
     * @param actionEvent triggered when the user activates the create button.
     */
    @FXML private void createTaskPictures(ActionEvent actionEvent){
        TaskPictures taskPictures = new TaskPictures(task.getDocID(), deviceNameTA.getText(), deviceCredTA.getText(), pictureAbsolute);
        functionsModel.addTaskPictures(taskPictures);
        Stage stage = (Stage) createTaskPictures.getScene().getWindow();
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
     * This method controls what we are doing with the chosen image file by the user.
     */
    @FXML private void imageFileExplorer() {

            FileChooser fileChooser = new FileChooser();
            setFileChooser(fileChooser);
            File file = fileChooser.showOpenDialog(new Stage());

            try{
                Path imagePath = FileSystems.getDefault().getPath(file.getPath());

                picture = new Image(new FileInputStream(imagePath.toFile()));
                pictureAbsolute = file.getAbsolutePath();
                pictureImageView.setImage(picture);
                pictureTF.setText(imagePath.toString());

            }catch (NullPointerException n){
                String str = "There was a problem with selecting an image. Issue: NullPointerException.";
                pictureError(str);
            } catch (FileNotFoundException e) {
                String str = "There was a problem with finding the image. Issue : FileNotFound.";
                pictureError(str);
            }

    }

    /**
     * Method to configure the file chooser and select which file types are accepted
     */
    private static void setFileChooser(FileChooser fileChooser) {
        fileChooser.setTitle("Select Image");
        fileChooser.setInitialDirectory(
                new File(System.getProperty("user.home"))
        );
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image files", "*.jpg", "*.bmp", "*.png", "*.jpeg")
        );
    }


    /**
     * We use this to display an error to the user if there is a problem.
     * @param str This is the source of the problem so that the user is informed.
     */
    private void pictureError(String str) {
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
