package gui.controller.externalViewControllers;

import be.Customer;
import be.Project;
import be.Task;
import be.TaskPictures;
import gui.model.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;

import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.apache.logging.log4j.*;

import java.io.File;

import java.util.List;


public class ExportReportViewController {

    @FXML
    private Button cancelButton;
    @FXML
    private Label noImagesFoundLabel;
    @FXML
    private AnchorPane selectImagePane;
    private List<Image> images;
    private Task selectedTask;
    private Project selectedProject;
    private Customer selectedCustomer;
    private Image selectedImg1;
    private Image selectedImg2;


    private String deviceNames;
    private String devicePasswords;


    private ProjectModel projectModel = ProjectModel.getInstance();

    private static final Logger logger = LogManager.getLogger("debugLogger");

    /**
     * this method is used to set the thumbnails images int the view.
     * it also handles the selection of the images and show the user which images are selected
     **/
    public void setImages(List<Image> imageList)  {
        logger.info("Setting images in ExportReportViewController.");
        images = imageList;

        if(selectedTask.getTaskLayout()!=null){
            images.add(selectedTask.getTaskLayout());
        }
        selectImagePane.getChildren().clear();

        logger.trace("Checking list.");
        if (images.isEmpty()) {
            noImagesFoundLabel.setText("No images uploaded for this task");
            logger.warn("No images found for setImages() in ExportReportViewController.");
        }
        int imgCount = 1;

        logger.info("Iterating over images found.");
        for (Image image : images) {
            ImageView bImage = new ImageView(image);
            bImage.setFitHeight(150);
            bImage.setFitWidth(200);
            bImage.setX(imageLocationX(imgCount, 200));
            bImage.setY(imageLocationY(imgCount, 150));

            bImage.setOnMouseClicked(event -> {

                if (event.getClickCount() == 1) {
                    if (selectedImg1 == null) {
                        selectedImg1 = image;
                        bImage.getStyleClass().add("selected");
                    } else if (selectedImg1.equals(image)) { // Image is already selected
                        selectedImg1 = null;
                        bImage.getStyleClass().remove("selected");
                    } else if (selectedImg2 == null) {
                        selectedImg2 = image;
                        bImage.getStyleClass().add("selected");
                    } else if (selectedImg2.equals(image)) { // Image is already selected
                        selectedImg2 = null;
                        bImage.getStyleClass().remove("selected");
                    }
                }
            });

            selectImagePane.getChildren().add(bImage);
            imgCount++;


            // Break out of the loop after adding 20 images
            if (imgCount == 20) {
                break;
            }
        }
        logger.info("image creation process finished.");
    }

    /**
     * These methods calculate the X and Y location of images based on their amount and size.
     **/
    private int imageLocationX(int imgCount, int imgWidth) {
        int getX;
        int spacing;
        if (imgCount <= 4) {
            getX = imgCount * imgWidth;
            spacing = imgCount * 5;
            return getX - imgWidth + spacing;
        } else if (imgCount <= 8) {
            imgCount = imgCount - 4;
            getX = imgCount * imgWidth;
            spacing = imgCount * 5;
            return getX - imgWidth + spacing;
        } else if (imgCount <= 12) {
            imgCount = imgCount - 8;
            getX = imgCount * imgWidth;
            spacing = imgCount * 5;
            return getX - imgWidth + spacing;
        } else if (imgCount <= 16) {
            imgCount = imgCount - 12;
            getX = imgCount * imgWidth;
            spacing = imgCount * 5;
            return getX - imgWidth + spacing;
        } else {
            imgCount = imgCount - 16;
            getX = imgCount * imgWidth;
            spacing = imgCount * 5;
            return getX - imgWidth + spacing;
        }
    }

    private int imageLocationY(int imgCount, int imgHeight) {
        int getY;

        if (imgCount <= 4) {
            return 0;
        } else if (imgCount <= 8) {
            return imgHeight + 5;
        } else if (imgCount <= 12) {

            getY = imgHeight * 2;
            return getY + 10;
        } else if (imgCount <= 16) {
            getY = imgHeight * 3;
            return getY + 15;
        } else {
            getY = imgHeight * 4;
            return getY + 20;
        }
    }

    /**
     * the method sets the selected task to the given value passed to the controller
     **/
    public void setSelectedTask(Task task) {
        selectedTask = task;
    }

    /**
     * the method sets the selected project to the given value passed to the controller
     **/
    public void setSelectedProject(Project project) {
        selectedProject = project;
    }

    /**
     * the method sets the selected customer to the given value passed to the controller
     **/
    public void setSelectedCustomer(Customer customer) {
        selectedCustomer = customer;
    }

    /**
     * the method sets the device names and credentials (passwords). It stores the information for the
     * specific task in two strings appending all the results separated by a comma
     **/
    public void setSelectedDeviceNameAndPasswords() {
        logger.trace("setSelectedDeviceNameAndPasswords() called.");
        if (selectedTask != null) {
            List<TaskPictures> taskPictures = projectModel.taskPicturesByDocID(selectedTask);
            StringBuilder devicePasswordsBuilder = new StringBuilder();
            StringBuilder deviceNamesBuilder = new StringBuilder();
            logger.trace("Iterating over list.");
            for (TaskPictures taskPicture : taskPictures) {
                String deviceName = taskPicture.getDeviceName();
                String devicePassword = taskPicture.getPassword();
                if (deviceName != null) {
                    deviceNamesBuilder.append(deviceName).append(", ");
                }
                if (devicePassword != null) {
                    devicePasswordsBuilder.append(devicePassword).append(", ");
                }
                if (deviceNamesBuilder.length() > 2) {
                    deviceNames = deviceNamesBuilder.toString().substring(0, deviceNamesBuilder.length() - 2);
                }
                if (devicePasswordsBuilder.length() > 2) {
                    devicePasswords = devicePasswordsBuilder.toString().substring(0, devicePasswordsBuilder.length() - 2);
                }
            }
            logger.trace("setSelectedDeviceNameAndPasswords() complete.");
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
    public void ExportReport(ActionEvent actionEvent) {
        logger.info("Exporting report from ExportReportViewController");

        String folderPath = "src/main/java/resources/Reports/";
        String filename = "Report " + selectedProject.getProjName() + "-" + selectedTask.getTaskName() + ".pdf";

        File file = new File(folderPath + filename);
        if (file.exists()) {
            logger.warn("The user attempted to create a file that already exists.");
            String str = "Error: file already saved";
            exportError(str);
        } else {
            setSelectedDeviceNameAndPasswords();
            projectModel.exportReport(selectedCustomer, selectedProject, selectedTask, selectedImg1, selectedImg2, deviceNames, devicePasswords);
            String str = "Report for project " + selectedProject.getProjName() + " and task " + selectedTask.getTaskName() + " saved";
            exportConfirmation(str);
        }
        logger.info("Report exported.");
    }


    /**
     * Closes the window with an action event.
     *
     * @param actionEvent triggers when the user activates the cancel button.
     */
    @FXML
    private void cancel(ActionEvent actionEvent) {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }
}


