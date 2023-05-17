package gui.controller.newAndUpdateControllers;

import be.*;
import gui.controller.mainViewControllers.*;
import gui.model.*;
import javafx.event.ActionEvent;
import javafx.fxml.*;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.stage.*;


import java.io.*;
import java.nio.file.*;
import java.util.*;

public class AddTaskPicturesController extends BaseController {
    @FXML private TextArea deviceNameTA;
    @FXML private TextArea deviceCredTA;
    @FXML private ImageView pictureImageView;

    @FXML private TextField  pictureTF;

    @FXML private Button cancelButton;

    @FXML private Button createTaskPictures;

    // Models
    private ProjectModel projectModel = ProjectModel.getInstance();

    // Task to have pictures added to
    private Task task = projectModel.getSelectedTask();

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
        projectModel.addTaskPictures(taskPictures);
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
                super.createWarning(str);
            } catch (FileNotFoundException e) {
                String str = "There was a problem with finding the image. Issue : FileNotFound.";
                super.createWarning(str);
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

}
