package gui.controller.externalViewControllers;

import be.TaskPictures;
import gui.model.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.apache.logging.log4j.*;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

public class ImageDialogViewController implements Initializable {
    // FXML
    @FXML
    private Button nextButton, previousButton;
    @FXML
    private ImageView largeImg;

    // Model
    private ProjectModel projectModel;

    // private variables.
    private int currentImageIndex;
    private List<Image> imageList;

    // logger
    private static final Logger logger = LogManager.getLogger("debugLogger");

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        logger.trace("Initializing ImageDialogViewController.");
        projectModel = ProjectModel.getInstance();
        imageList = new ArrayList<>();

        imageList.addAll(projectModel.getTaskPictureImages()); // Getting images for selected task.
        currentImageIndex = projectModel.getImageIndex(); // The index of the image selected in Documentation controller.
        largeImg.setImage(imageList.get(currentImageIndex)); // Setting the image.

        previousButton.setOnAction(this::clickPreviousImg);
        nextButton.setOnAction(this::clickNextImg);
    }

    /**
     * this method is used to show the next image pressing the next button in the image dialog
     **/
    @FXML
    public void clickNextImg(ActionEvent actionEvent) {
        cycleImage(true);
    }
    /**
     * this method is used to show the previous image pressing the previous button in the image dialog
     **/
    @FXML
    public void clickPreviousImg(ActionEvent actionEvent) {
        cycleImage(false);
    }

    /**
     * Cycle through the list of images by adding or subtracting from our index.
     * @param isNext If true, we are getting the next image, else we get the previous image.
     */
    private void cycleImage(boolean isNext){
        logger.trace("Cycling images.");
        if(!imageList.isEmpty()) {
            if (isNext) {
                currentImageIndex++;
                if (currentImageIndex >= imageList.size()) {
                    currentImageIndex = 0;
                }
            } else {
                currentImageIndex--;
                if (currentImageIndex < 0) {
                    currentImageIndex = imageList.size() - 1;
                }
            }
            Image image = imageList.get(currentImageIndex);
            if (image != null) {
                largeImg.setImage(image);
            }
        }else {// This should never happen.
            logger.warn("The image list for ImageDialogViewController is empty. This window should not have opened with 0 images.");
        }
    }
}

