package gui.controller.newAndUpdateControllers;

import be.*;
import gui.controller.mainViewControllers.*;
import gui.model.*;
import javafx.event.ActionEvent;
import javafx.fxml.*;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.stage.*;
import org.apache.logging.log4j.*;


import java.io.*;
import java.nio.file.*;

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

    private static final Logger logger = LogManager.getLogger("debugLogger");

    //validation fields

    int maxDeviceNameLength = 100;
    int maxDeviceCredLength = 100;

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
    @FXML
    private void createTaskPictures(ActionEvent actionEvent) {
        logger.info("Creating Task Picture");
        if (validateTaskPictureFields()) {
            TaskPictures taskPictures = new TaskPictures(task.getDocID(), deviceNameTA.getText(), deviceCredTA.getText(), pictureAbsolute);
            projectModel.addTaskPictures(taskPictures);
            Stage stage = (Stage) createTaskPictures.getScene().getWindow();
            stage.close();
            logger.info("Task picture created");
        } else {
            logger.info("Task picture not created due to invalid fields");
            warningLoggerForTaskPicture();
        }

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
            logger.info("Choosing a file.");
            FileChooser fileChooser = new FileChooser();
            setFileChooser(fileChooser);
            File file = fileChooser.showOpenDialog(new Stage());

            logger.info("fetching selected file.");
            try{
                Path imagePath = FileSystems.getDefault().getPath(file.getPath());

                picture = new Image(new FileInputStream(imagePath.toFile()));
                pictureAbsolute = file.getAbsolutePath();
                pictureImageView.setImage(picture);
                pictureTF.setText(imagePath.toString());

            }catch (NullPointerException n){
                logger.error("File chooser null pointer", n);
                String str = "There was a problem with selecting an image. Issue: NullPointerException.";
                super.createWarning(str);
            } catch (FileNotFoundException e) {
                logger.error("file not found for file chooser", e);
                String str = "There was a problem with finding the image. Issue : FileNotFound.";
                super.createWarning(str);
            }
            logger.info("imageFileExplorer() complete.");
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
     * This method checks if the length of the device name textfield is bigger than the max length for the field
     * it returns true if the length is valid
     **/
    private boolean isDeviceNameTFValid() {
        return deviceNameTA.getText().length() <= maxDeviceNameLength;
    }

    /**
     * This method checks if the length of the credentials textfield is bigger than the max length for the field
     * it returns true if the length is valid
     **/
    private boolean isDeviceCredTFValid() {
        return deviceCredTA.getText().length() <= maxDeviceCredLength;
    }


    /**
     * This method checks if the picture textfield is empty
     * it returns true if it's present, false if it's empty
     **/
    private boolean isPictureValid() {
        if (pictureTF.getText().isEmpty()) {
            return false;
        } else {
            return true;
        }

    }


    /**
     * this method shows an alert to the user if the inserted text field length exceeds the max
     **/
    private void alertDeviceNameTF() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Validate device name");
        alert.setContentText("Device name is too long, max is " + maxDeviceNameLength + " characters.");
        alert.showAndWait();
    }

    /**
     * this method shows an alert to the user if the inserted text field length exceeds the max
     **/
    private void alertDeviceCredTF() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Validate device credentials");
        alert.setContentText("Device credential is too long, max is " + maxDeviceCredLength + " characters.");
        alert.showAndWait();
    }


    /**
     * this method shows an alert to the user if the inserted text field length exceeds the max
     **/
    private void alertDeviceNameAndCredTF() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Validate device name and credentials");
        alert.setContentText("Device name and credential are too long, max is " + maxDeviceNameLength + " and " + maxDeviceCredLength + " characters respectively.");
        alert.showAndWait();
    }

    /**
     * this method shows an alert to the user if a picture is not selected
     **/
    private void alertNoPicture() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Validate picture");
        alert.setContentText("No picture selected, please select one");
        alert.showAndWait();
    }





    /**
     * method to check all combination of fields and show the corresponding alerts or error message
     **/
    private boolean validateTaskPictureFields() {
        boolean isValid = true;

        if (isPictureValid()) {

            if (!isDeviceCredTFValid() && !isDeviceNameTFValid()) {
                alertDeviceNameAndCredTF();
                isValid = false;
            } else if (!isDeviceNameTFValid()) {
                alertDeviceNameTF();
                isValid = false;
            } else if (!isDeviceCredTFValid()) {
                alertDeviceCredTF();
                isValid = false;
            }
        } else {
            alertNoPicture();
            isValid = false;
        }
        return isValid;
    }

    private void warningLoggerForTaskPicture() {
        if (!isDeviceCredTFValid()) {
            logger.warn("Invalid device credential field: credential exceeds the maximum character limit");
        }
        if (!isDeviceNameTFValid()) {
            logger.warn("Invalid device name field: device name exceeds the maximum character limit");

        }
        if (!isPictureValid()) {
            logger.warn("No picture inserted when adding new task documentation");
        }
    }
}
