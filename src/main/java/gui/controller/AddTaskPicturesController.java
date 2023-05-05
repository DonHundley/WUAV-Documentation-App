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
    @FXML private TextArea beforeComment;
    @FXML private ImageView beforeImageView;
    @FXML private ImageView afterImageView;
    @FXML private TextArea afterComment;
    @FXML private TextField beforeTF;
    @FXML private TextField afterTF;
    @FXML private Button cancelButton;

    // Models
    private Persistent persistenceModel = Persistent.getInstance();
    private Observables observablesModel = Observables.getInstance();
    private Functions functionsModel = new Functions();

    // Task to have pictures added to
    private Task task = persistenceModel.getSelectedTask();

    private Image beforePicture;
    private Image afterPicture;


    /**
     * We call this when this controller is called from navigation to set our models, tableview, and labels.
     * @param persistenceModel this is our instance of Persistent from navigation
     * @param observablesModel this is our instance of Observables from navigation
     * @param functionsModel this is our instance of Functions from navigation
     */
    public void addTaskPicturesController(Persistent persistenceModel, Observables observablesModel, Functions functionsModel){
        this.persistenceModel = persistenceModel;
        this.functionsModel = functionsModel;
        this.observablesModel = observablesModel;

        task = persistenceModel.getSelectedTask();
        beforeComment.setText("No comment.");
        afterComment.setText("No comment. ");
    }

    /**
     * Opens the filechooser for after pictures.
     * @param actionEvent triggers when the user activates the after picture button.
     */
    @FXML private void openFileChooserAfter(ActionEvent actionEvent){
        imageFileExplorer(false);
    }

    /**
     * Opens the filechooser for the before pictures.
     * @param actionEvent triggers when the user activates the before picture button.
     */
    @FXML private void openFileChooserBefore(ActionEvent actionEvent){
        imageFileExplorer(true);
    }

    /**
     * Creates a new TaskPictures from the filled fields.
     * @param actionEvent triggered when the user activates the create button.
     */
    @FXML private void createTaskPictures(ActionEvent actionEvent){
        TaskPictures taskPictures = new TaskPictures(afterPicture, beforePicture, afterComment.getText(),task.getDocID(), beforeComment.getText());
        functionsModel.addTaskPictures(taskPictures);
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
     * @param isAfter true if the picture being added is an after picture.
     */
    @FXML private void imageFileExplorer(Boolean isAfter) {
        try {
            FileChooser fileChooser = new FileChooser();
            setFileChooser(fileChooser);
            File file = fileChooser.showOpenDialog(new Stage());

            try{
                Path imagePath = FileSystems.getDefault().getPath(file.getPath());

                if(isAfter){
                    afterPicture = new Image(new FileInputStream(imagePath+file.getName()));
                    afterImageView.setImage(afterPicture);
                    afterTF.setText(afterPicture.getUrl());
                }else {
                    beforePicture = new Image(new FileInputStream(imagePath+file.getName()));
                    beforeImageView.setImage(beforePicture);
                    beforeTF.setText(beforePicture.getUrl());
                }
            }catch (NullPointerException n){
                String str = "There was a problem with selecting an image. Issue: NullPointerException.";
                pictureError(str);
            }
        } catch(IOException e){
            String str = "There was a problem with selecting an image. Issue: IOException.";
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
