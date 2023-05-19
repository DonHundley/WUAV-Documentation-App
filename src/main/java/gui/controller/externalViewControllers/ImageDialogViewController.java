package gui.controller.externalViewControllers;

import be.TaskPictures;
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



    @FXML
    private Button nextButton, previousButton;
    @FXML
    private ImageView largeImg;
    private int currentImageIndex;

    private List<Image> imageList = new ArrayList<>();

    private static final Logger logger = LogManager.getLogger("debugLogger");

    /**
     * this method sets the selected image in the image dialog view and updates the current image index
     **/
    public void setSelectedImage(Image image) {
        logger.trace("setSelectedImage() called");
        largeImg.setImage(image);
        currentImageIndex = getImageIndex(image);
        setCurrentImageIndex(currentImageIndex);
    }

    /**
     * this method sets the images list to be displayed in the dialog view. If the list is null, an empty list is initialized
     **/
    public void setImageList(List imageList) {
        if (imageList == null) {
            this.imageList = new ArrayList<>();
        } else {
            this.imageList = imageList;
        }
    }

    /**
     * the method sets the current image index to the given value passed to the controller
     **/
    public void setCurrentImageIndex(int currentImageIndex) {
        this.currentImageIndex = currentImageIndex;
    }

    /**
     * the method takes an image as input and returns its index in the imageList
     **/
    private int getImageIndex(Image image) {
        return imageList.indexOf(image);
    }

    /**
     * this method is used to show the next image pressing the next button in the image dialog
     * the image index is updated. If the list is not empty it shows the next image,
     * it checks if the first picture has been reached and changes the index to show the last image
     **/
    @FXML
    public void clickNextImg(ActionEvent actionEvent) {
        currentImageIndex++;
        logger.trace("Selecting next image.");
        if (currentImageIndex >= imageList.size()) {
            currentImageIndex = 0;
        }
        if (!imageList.isEmpty()) {

            Image image = getImageByIndex(currentImageIndex);
            if (image != null) {
                largeImg.setImage(image);

            }
        }
    }

    /**
     * this method takes an index as input and returns the image at that index in the imageList**/
    private Image getImageByIndex(int currentImageIndex) {
        if (imageList.isEmpty()) {
            return null;
        }

        return imageList.get(currentImageIndex);
    }


    /**
     * this method is used to show the previous image pressing the previous button in the image dialog
     * the image index is updated. If the list is not empty it shows the previous image,
     * it checks if the last picture has been reached to restart from the 1st image**/
    @FXML
    public void clickPreviousImg(ActionEvent actionEvent) {
        currentImageIndex--;
        logger.trace("Selecting previous image.");
        if (currentImageIndex < 0) {
            currentImageIndex = imageList.size() - 1;
        }
        Image image = getImageByIndex(currentImageIndex);
        if (image != null) {
            largeImg.setImage(image);
            setCurrentImageIndex(currentImageIndex);

        }

    }

    /**
     * In the initialize method, event handlers are set up for the "Previous" and "Next" buttons
     * **/
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        logger.trace("Initializing ImageDialogViewController.");
        previousButton.setOnAction(this::clickPreviousImg);

        nextButton.setOnAction(this::clickNextImg);
    }

}

