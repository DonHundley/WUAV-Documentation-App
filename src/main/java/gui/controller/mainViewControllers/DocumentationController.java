package gui.controller.mainViewControllers;

import be.*;
import gui.controller.newAndUpdateControllers.*;
import gui.model.*;
import javafx.beans.property.*;


import javafx.event.ActionEvent;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.stage.*;
import org.apache.logging.log4j.*;


import java.io.*;
import java.net.*;
import java.util.*;


public class DocumentationController extends BaseController implements Initializable {
    @FXML
    private AnchorPane imagePane;

    // Tableview
    @FXML
    private TableView<Task> taskTV;
    @FXML
    private TableColumn<Task, String> taskName;
    @FXML
    private TableColumn<Task, String> taskState;

    // Labels
    @FXML
    private Label windowTitleLabel;
    @FXML
    private Label descriptionLabel;
    @FXML
    private Label usernameLabel;
    @FXML
    private Label messageLabel;
    @FXML
    private Label deviceNamesLabel;
    @FXML
    private Label deviceCredentialsLabel;

    // Buttons
    @FXML
    private Button updateTaskButton;
    @FXML
    private Button picturesButton;
    @FXML
    private Button layoutButton;
    @FXML
    private Button createTaskButton;

    // ImageViews
    @FXML
    private ImageView largeImageView;

    // Models
    private final ProjectModel projectModel = ProjectModel.getInstance();
    private final AuthenticationModel authenticationModel = AuthenticationModel.getInstance();

    // logger
    private static final Logger logger = LogManager.getLogger("debugLogger");

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        logger.trace("initializing DocumentationController");
        setUsernameLabel();
        setTaskTV();
        if (authenticationModel.getLoggedInUser().getAccess().equalsIgnoreCase("TECHNICIAN")) {
            createTaskButton.setVisible(false);
        }

        if (authenticationModel.getLoggedInUser().getAccess().equalsIgnoreCase("SALES")) {
            createTaskButton.setVisible(false);
            layoutButton.setVisible(false);
            picturesButton.setVisible(false);
            updateTaskButton.setVisible(false);
            windowTitleLabel.setText("Customer Documentation");
        }
    }

    /**
     * We use this to set our username label and window title label.
     */
    private void setUsernameLabel() {// set our username label to the users name
        logger.trace("setting username Label in " + this.getClass().getName());
        windowTitleLabel.setText("Project Task Documentation");
        usernameLabel.setText(authenticationModel.getLoggedInUser().getFirstName() + " " + authenticationModel.getLoggedInUser().getLastName());
    }

    /**
     * We use this to set our tableview with the observable list from Observables.
     */
    private void setTaskTV() {
        logger.info("Setting taskTV in "+ this.getClass().getName());
        taskTV.setItems(projectModel.getTasksByProject());
        projectModel.loadAllProjLists();
        taskName.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTaskName()));
        taskState.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTaskState()));
    }

    /**
     * this method generates thumbnail images for a list of picture belonging to a specific task and displays them in a pane.
     * A mouse click event is set on each thumbnail to open an image dialog
     */
    private void generateImgThumbnails() {
        logger.info("generateImgThumbnails() was called in " + this.getClass().getName());
        List<Image> imageList = projectModel.getTaskPictureImages();
        imagePane.getChildren().clear();
        int imgCount = 1;

        logger.info("Iterating over all pictures for selected task");
        for (Image taskPicture : imageList) {
            ImageView bImage = new ImageView(taskPicture);
            bImage.setFitHeight(150);
            bImage.setFitWidth(200);
            bImage.setX(projectModel.getLocationX(imgCount, 200));
            bImage.setY(projectModel.getLocationY(imgCount, 150));
            imagePane.getChildren().add(bImage);
            bImage.setOnMouseClicked(event -> openImageDialogOnMouseClick(imageList.indexOf(taskPicture)));
            imgCount++;

            if (imgCount == 20) {
                break;
            }
        }
        logger.info("Finished generating image thumbnails.");
    }

    /**
     * This will set the device names and device credentials labels with the relevant information of the devices for the
     * selected task.
     */
    private void setDeviceLabels() {
        logger.trace("setting device labels.");
        deviceNamesLabel.setText(projectModel.getTaskPictureDevices());
        deviceCredentialsLabel.setText(projectModel.getTaskPictureCredentials());
    }

    /**
     * this method handles the click event on the thumbnails image and open an image dialog to see the selected image in a bigger size
     **/
    private void openImageDialogOnMouseClick(int imageIndex) {
        projectModel.setImageIndex(imageIndex);
        logger.info("openImageDialogOnMouseClick() activated in " + this.getClass().getName());
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/view/externalViews/ImageDialogView.fxml"));
            logger.info("Loading ImageDialogView.fxml");
            Parent root = loader.load();
            Stage stage = new Stage();
            Scene scene = new Scene(root);
            stage.setTitle("Image Dialog");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            logger.error("There has been an issue loading ImageDialogView.fxml.", e);
            String str = "There has been an issue loading ImageDialogView.fxml, please contact system admin.";
            super.createWarning(str);
        }
        logger.info("openImageDialogOnMouseClick() complete.");
    }



    private void setDescriptionLabel() {
        if (projectModel.getSelectedTask().getTaskDesc() != null) {
            descriptionLabel.setText(projectModel.getSelectedTask().getTaskDesc());
        }
        logger.trace("Description label set in " + this.getClass().getName());
    }


    /**
     * This will open the New Task View.
     */
    @FXML
    private void createTask() {
        super.newTask();
    }


    /**
     * This will open the edit task view.
     */
    @FXML
    private void updateTask() {
        editTask();
    }

    private void editTask() {
        logger.info("editTask() called in " + this.getClass().getName());
        if (taskTV.getSelectionModel().getSelectedItem() != null) {
            try {
                messageLabel.setText("");
                logger.info("Loading EditTask.fxml");
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/view/newAndUpdateViews/EditTask.fxml"));
                Parent root = loader.load();
                EditTaskController controller = loader.getController();
                controller.setFieldsOnEdit();
                Stage stage = new Stage();
                Scene scene = new Scene(root);
                stage.setTitle("Update Task");
                stage.setScene(scene);
                stage.show();
            } catch (IOException e) {
                logger.error("There has been a problem loading EditTask.fxml." , e);
                String str = "There has been an issue loading EditTask.fxml, please contact system Admin.";
                super.createWarning(str);
            }
        } else messageLabel.setText("Please select a task to be updated.");
        logger.info("editTask() complete.");
    }

    /**
     * This will open the add pictures window.
     * We catch the IOException and show the user a crafted alert.
     */
    @FXML
    private void addPictures() {
        logger.info("addPictures called in " + this.getClass().getName());
        if (taskTV.getSelectionModel().getSelectedItem() != null) {
            try {
                messageLabel.setText("");
                logger.info("Loading AddTaskPictures.fxml");
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/view/newAndUpdateViews/AddTaskPictures.fxml"));
                Parent root = loader.load();
                Stage stage = new Stage();
                Scene scene = new Scene(root);
                stage.setTitle("Add Task Pictures");
                stage.setScene(scene);
                stage.show();
            } catch (IOException e) {
                logger.error("There has been a problem loading AddTaskPictures.fxml.", e);
                String str = "There has been a problem loading AddTaskPictures.fxml, please contact system Admin.";
                super.createWarning(str);
            }
        } else messageLabel.setText("Please select a task to add pictures to.");
        logger.info("addPictures() complete.");
    }

    /**
     * This will log the user out and change the view to the login.
     * We catch the IOException and show the user a crafted alert.
     *
     * @param actionEvent triggered by the logout button.
     */
    @FXML
    private void logOut(ActionEvent actionEvent) {
        super.logout(actionEvent);
    }







    @FXML
    private void taskTVOnMouse(MouseEvent mouseEvent) {
        logger.trace("taskTV clicked in " + this.getClass().getName());
        if (taskTV.getSelectionModel().getSelectedItem() != null) {
            projectModel.setSelectedTask(taskTV.getSelectionModel().getSelectedItem());
            projectModel.createTaskPicturesList(projectModel.taskPicturesByDocID());
            setDeviceLabels();
            setDescriptionLabel();
            generateImgThumbnails();
            if (projectModel.getSelectedTask().getTaskLayout() != null) {
                largeImageView.setImage(projectModel.getSelectedTask().getTaskLayout());
            }
            if (mouseEvent.getClickCount() == 2) {
                editTask();
            }
        } else if (mouseEvent.getClickCount() == 2) {
            if (authenticationModel.getLoggedInUser().getAccess().equalsIgnoreCase("MANAGER") || authenticationModel.getLoggedInUser().getAccess().equalsIgnoreCase("ADMIN")) {
                newTask();
            }
        }
    }

    public void updateLayout() {
        logger.info("updateLayout() called in " + this.getClass().getName());
        if (taskTV.getSelectionModel().getSelectedItem() != null && projectModel.getSelectedTask() != null) {
            try {
                messageLabel.setText("");
                logger.info("Loading EditLayout.fxml");
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/view/newAndUpdateViews/EditLayout.fxml"));
                Parent root = loader.load();
                Stage stage = new Stage();
                Scene scene = new Scene(root);
                stage.setTitle("Update Layout");
                stage.setScene(scene);
                stage.show();
            } catch (IOException e) {
                logger.error("There has been a problem loading EditLayout.fxml.",e);
                String str = "There has been an issue loading EditLayout.fxml, please contact system Admin";
                super.createWarning(str);
            }
        } else messageLabel.setText("Please select a task to update the layout.");
        logger.info("updateLayout() complete.");
    }

    @FXML
    private void anchorOnClick() {
        logger.trace("User has clicked the anchor pane in " + this.getClass().getName());
        if (taskTV.getSelectionModel().getSelectedItem() != null) {
            taskTV.getSelectionModel().clearSelection();
            taskTV.refresh();
        }
    }


    /**
     * method to open the window where it's possible to export the report for the selected task
     **/
    @FXML private void openExportReportView() {
        logger.info("openExportReportView called in " + this.getClass().getName());
        if (taskTV.getSelectionModel().getSelectedItem() != null) {
            try {
                logger.info("Loading ExportReportView.fxml");
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/view/externalViews/ExportReportView.fxml"));
                Parent root = loader.load();
                Stage stage = new Stage();
                Scene scene = new Scene(root);
                stage.setTitle("Export Report View");
                stage.setScene(scene);
                stage.show();
            } catch (IOException e) {
                logger.error("There has been an issue loading ExportReportView.fxml.", e);
                String str = "There has been an issue loading ExportReportView.fxml, please contact system Admin.";
                super.createWarning(str);
            }

        } else {
            messageLabel.setText("Please select a task to export its report");
        }
        logger.info("Method complete.");
    }
}


