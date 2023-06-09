package gui.controller.externalViewControllers;

import be.Customer;
import be.Project;
import be.Task;
import gui.model.*;
import javafx.fxml.*;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;

import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.apache.logging.log4j.*;

import java.io.File;

import java.net.*;
import java.util.*;


public class ExportReportViewController implements Initializable {

    @FXML
    private Button cancelButton;
    @FXML
    private Label noImagesFoundLabel;
    @FXML
    private AnchorPane selectImagePane;

    // Models
    private ProjectModel projectModel;

    // BE instances
    private Task selectedTask;
    private Project selectedProject;
    private Customer selectedCustomer;

    // Private variables
    private Image selectedImg1;
    private Image selectedImg2;
    private String deviceNames;
    private String devicePasswords;

    private static final Logger logger = LogManager.getLogger("debugLogger");

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        projectModel = ProjectModel.getInstance();
        CustomerModel customerModel = CustomerModel.getInstance();

        selectedProject = projectModel.getSelectedProject();
        selectedTask = projectModel.getSelectedTask();
        selectedCustomer = customerModel.getSelectedCustomer();

        setImages(projectModel.getTaskPictureImages());
        deviceNames = projectModel.getTaskPictureDevices();
        devicePasswords = projectModel.getTaskPictureCredentials();
    }
    
    /**
     * this method is used to set the thumbnails images int the view.
     * it also handles the selection of the images and show the user which images are selected
     **/
    public void setImages(List<Image> imageList)  {
        logger.info("Setting images in ExportReportViewController.");

        if(selectedTask.getTaskLayout()!=null){
            imageList.add(selectedTask.getTaskLayout());
        }
        selectImagePane.getChildren().clear();

        logger.trace("Checking list.");
        if (imageList.isEmpty()) {
            noImagesFoundLabel.setText("No images uploaded for this task");
            logger.warn("No images found for setImages() in ExportReportViewController.");
            return;
        }
        int imgCount = 1;

        logger.info("Iterating over images found.");
        for (Image image : imageList) {
            ImageView bImage = new ImageView(image);
            bImage.setFitHeight(150);
            bImage.setFitWidth(200);
            bImage.setX(projectModel.getLocationX(imgCount, 200));
            bImage.setY(projectModel.getLocationY(imgCount, 150));

            bImage.setOnMouseClicked((e) -> imageOnClick(e, bImage));
            selectImagePane.getChildren().add(bImage);
            imgCount++;

            // Break out of the loop after adding 20 images
            if (imgCount == 20) {
                break;
            }
        }
        logger.info("image creation process finished.");
    }

    private void imageOnClick(MouseEvent e, ImageView bImage) {
            if (e.getClickCount() == 1) {
                if (selectedImg1 == null) {
                    selectedImg1 = bImage.getImage();
                    bImage.getStyleClass().add("selected");
                } else if (selectedImg1.equals(bImage.getImage())) { // Image is already selected
                    selectedImg1 = null;
                    bImage.getStyleClass().remove("selected");
                } else if (selectedImg2 == null) {
                    selectedImg2 = bImage.getImage();
                    bImage.getStyleClass().add("selected");
                } else if (selectedImg2.equals(bImage.getImage())) { // Image is already selected
                    selectedImg2 = null;
                    bImage.getStyleClass().remove("selected");
                }
            }
    }


    /**
     * method used to show an alert to the user and warn them of an error
     **/
    private void exportError(String str) {
        logger.warn("There was an issue exporting the report, user informed.");
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Error");
        alert.setHeaderText(str);
        alert.show();
    }

    /**
     * method used to show an alert to the user and confirm that the report is saved
     **/
    private void exportConfirmation(String str) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Report saved");
        alert.setHeaderText(str);
        alert.show();
    }

    /**
     * method to export the task's Report. it checks if the report already exists and warns the user or confirm that the file has been saved.
     **/
    public void ExportReport() {
        logger.info("Exporting report from ExportReportViewController");

        String folderPath = "src/main/resources/report/printedReports/";
        String filename = "Report " + selectedProject.getProjName() + "-" + selectedTask.getTaskName() + ".pdf";

        File file = new File(folderPath + filename);
        if (file.exists()) {
            logger.warn("The user attempted to create a file that already exists.");
            String str = "Error: file already saved";
            exportError(str);
        } else {

            projectModel.exportReport(selectedCustomer, selectedProject, selectedTask, selectedImg1, selectedImg2, deviceNames, devicePasswords);
            String str = "Report for project " + selectedProject.getProjName() + " and task " + selectedTask.getTaskName() + " saved";
            exportConfirmation(str);
        }
        logger.info("Report exported.");
    }


    /**
     * Closes the window with an action event.
     */
    @FXML
    private void cancel() {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }


}


