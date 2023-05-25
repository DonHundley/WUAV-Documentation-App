package gui.controller.newAndUpdateControllers;

import be.*;
import gui.controller.mainViewControllers.*;
import gui.model.*;
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
    private final ProjectModel projectModel = ProjectModel.getInstance();

    // Task to have pictures added to
    private final Task task = projectModel.getSelectedTask();

    private String pictureAbsolute;

    private static final Logger logger = LogManager.getLogger("debugLogger");

    //validation fields
    private final int maxDeviceNameLength = 100;
    private final int maxDeviceCredLength = 100;

    /**
     * Opens the filechooser for pictures.
     */
    @FXML private void openFileChooser(){
        imageFileExplorer();
    }


    /**
     * Creates a new TaskPictures from the filled fields.
     */
    @FXML
    private void createTaskPictures() {
        logger.info("Creating Task Picture");
        if (validateTaskPictureFields()) {
            TaskPictures taskPictures = new TaskPictures(task.getDocID(), deviceNameTA.getText(), deviceCredTA.getText(), pictureAbsolute);
            projectModel.addTaskPictures(taskPictures);
            projectModel.loadAllProjLists();
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
     */
    @FXML private void cancel(){
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

                Image picture = new Image(new FileInputStream(imagePath.toFile()));
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
     * This method checks if the length of the device name text field is bigger than the max length for the field
     * it returns true if the length is valid
     **/
    private boolean isDeviceNameTFValid() {
        return deviceNameTA.getText().length() > maxDeviceNameLength;
    }

    /**
     * This method checks if the length of the credentials textfield is bigger than the max length for the field
     * it returns true if the length is valid
     **/
    private boolean isDeviceCredTFValid() {
        return deviceCredTA.getText().length() > maxDeviceCredLength;
    }


    /**
     * This method checks if the picture text field is empty
     * it returns true if it's present, false if it's empty
     **/
    private boolean isPictureValid() {
        return !pictureTF.getText().isEmpty();
    }

    /**
     * Used to generate dynamic alerts based on different failures in validation
     * @param title The title of the alert.
     * @param context The context of the alert.
     */
    private void validationAlert(String title, String context){
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setContentText(context);
        alert.showAndWait();
    }


    /**
     * method to check all combination of fields and show the corresponding alerts or error message
     **/
    private boolean validateTaskPictureFields() {
        boolean isValid = true;

        if (isPictureValid()) {
            if (isDeviceCredTFValid() && isDeviceNameTFValid()) {
                validationAlert("Validate device name and credentials",
                        "Device name and credential are too long, max is " + maxDeviceNameLength
                                + " and " + maxDeviceCredLength + " characters respectively.");
                isValid = false;
            } else if (isDeviceNameTFValid()) {
                validationAlert("Validate device name",
                        "Device name is too long, max is " + maxDeviceNameLength + " characters.");
                isValid = false;
            } else if (isDeviceCredTFValid()) {
                validationAlert("Validate device credentials",
                        "Device credential is too long, max is " + maxDeviceCredLength + " characters.");
                isValid = false;
            }
        } else {
            validationAlert("Validate picture",
                    "No picture selected, please select one");
            isValid = false;
        }
        return isValid;
    }

    private void warningLoggerForTaskPicture() {
        if (isDeviceCredTFValid()) {
            logger.warn("Invalid device credential field: credential exceeds the maximum character limit");
        }
        if (isDeviceNameTFValid()) {
            logger.warn("Invalid device name field: device name exceeds the maximum character limit");

        }
        if (!isPictureValid()) {
            logger.warn("No picture inserted when adding new task documentation");
        }
    }
}
