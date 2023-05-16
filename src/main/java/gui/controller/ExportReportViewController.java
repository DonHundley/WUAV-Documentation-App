package gui.controller;

import be.Customer;
import be.Project;
import be.Task;
import gui.model.Functions;
import gui.model.Observables;
import gui.model.Persistent;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.io.File;
import java.util.List;
import java.util.Optional;

public class ExportReportViewController {

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


    private Functions functionsModel = new Functions();


/**this method is used to set the thumbnails images int he view.
 * it also handles the selection of the images and show the user which images are selected**/
    public void setImages(List<Image> imageList) {
        images = imageList;
        selectImagePane.getChildren().clear();

        if(images.isEmpty()){
            noImagesFoundLabel.setText("No images uploaded for this task");
        }
        int imgCount = 1;
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
    }
/**These methods calculate the X and Y location of images based on their amount and size.**/
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
     * the method sets the selected task to the given value passed to the controller**/
    public void setSelectedTask(Task task) {
        selectedTask = task;
    }
    /**
     * the method sets the selected project to the given value passed to the controller**/
    public void setSelectedProject(Project project) {
        selectedProject = project;
    }
    /**
     * the method sets the selected customer to the given value passed to the controller**/
    public void setSelectedCustomer(Customer customer) {
        selectedCustomer = customer;
    }


    /**method used to show an alert to the user and warn them of an error**/
    private void exportError(String str) {
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
/**method used to show an alert to the user and confirm that the report is saved**/
    private void exportConfirmation(String str) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Report saved");
        alert.setHeaderText(str);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            alert.close();
        } else {
            alert.close();
        }
    }

    /**method to export thetask's Report. it checks if the report already exists and warns the user or confirm that the file has been saved. **/
    public void ExportReport(ActionEvent actionEvent) {

        String folderPath = "src/main/java/resources/Reports/";
        String filename = "Report " + selectedProject.getProjName() + ".pdf";

        File file = new File(folderPath + filename);
        if (file.exists()) {
            String str = "Error: file already saved";
            exportError(str);
        } else {
            functionsModel.exportReport(selectedCustomer, selectedProject, selectedTask, selectedImg1, selectedImg2);
            String str = "Report for project " + selectedProject.getProjName() + " and task " + selectedTask.getTaskName() + " saved";
            exportConfirmation(str);
        }


    }
}


