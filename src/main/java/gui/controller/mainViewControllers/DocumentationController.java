package gui.controller.mainViewControllers;

import be.*;
import gui.controller.externalViewControllers.*;
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
    private Label deviceNamesLabel;
    @FXML
    private Label deviceCredentialsLabel;
    @FXML
    private Button updateTaskButton;
    @FXML
    private Button picturesButton;
    @FXML
    private Button layoutButton;

    @FXML
    private Button createTaskButton;
    @FXML
    private AnchorPane imagePane;
    @FXML
    private Label messageLabel;

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

    // ImageViews
    @FXML
    private ImageView largeImageView;

    // Models
    private ProjectModel projectModel = ProjectModel.getInstance();
    private AuthenticationModel authenticationModel = AuthenticationModel.getInstance();
    private CustomerModel customerModel = CustomerModel.getInstance();

    // private variables
    private Project selectedProject;
    private HashMap<String, Image> images = new HashMap<>();
    private List<String> deviceNames = new ArrayList<>();
    private List<String> deviceCredentials = new ArrayList<>();

    // logger
    private static final Logger logger = LogManager.getLogger("debugLogger");

    /**
     * We use this to set our username label and window title label.
     */
    private void setUsernameLabel() {// set our username label to the users name
        logger.trace("setting username Label in " + this.getClass().getName());
        windowTitleLabel.setText("Project Task Manager");
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
     * This controller needs the selected project in order to get a list of tasks for it.
     * We fetch the selected project from persistent in order to do this, if there is a problem with this, we show an alert.
     */
    private void setSelectedProject() {
        logger.trace("Setting selected project from "+ this.getClass().getName());
        if (projectModel.getSelectedProject() != null) {
            selectedProject = projectModel.getSelectedProject();
        } else {
            logger.warn("User may not have selected a project in " + this.getClass().getName() +". User was notified.");
            String str = "An error has occurred, please contact system admin. Project was not selected or does not exist.";
            super.createWarning(str);
        }
    }

    /**
     * this method generates thumbnail images for a list of picture belonging to a specific task and displays them in a pane.
     * A mouse click event is set on each thumbnail to open an image dialog
     */
    private List<Image> generateImgThumbnails() {
        logger.info("generateImgThumbnails() was called in " + this.getClass().getName());
        Task task = projectModel.getSelectedTask();
        logger.trace("Creating list in generateImgThumbnails()");
        List<TaskPictures> pics = projectModel.taskPicturesByDocID(task);
        List<Image> imageList = new ArrayList<>();
        for (TaskPictures taskPictures : pics) {
            imageList.add(taskPictures.getPicture());
        }

        logger.trace("Clearing appropriate items for image Thumbnails.");

        imagePane.getChildren().clear();
        images.clear();
        resetDeviceLabels();

        int imgCount = 1;
        int deviceCount = 1;

        logger.info("Iterating over all pictures for selected task in generateImgThumbnails()");
        for (TaskPictures picture : pics) {
            if (picture.getPicture() != null) {
                ImageView bImage = new ImageView(picture.getPicture());
                bImage.setFitHeight(150);
                bImage.setFitWidth(200);
                bImage.setX(imageLocationX(imgCount, 200));
                bImage.setY(imageLocationY(imgCount, 150));
                imagePane.getChildren().add(bImage);
                bImage.setOnMouseClicked(event -> openImageDialogOnMouseClick(event, bImage.getImage(), imageList));
                imgCount++;

            }

            logger.info("Iterating over all devices for selected task in generateImgThumbnails()");

            if(picture.getDeviceName() != null){
                deviceNames.add("Device " + deviceCount + ": " + picture.getDeviceName() + ", ");
                if(picture.getPassword() != null){
                    deviceCredentials.add("Device " + deviceCount +": " + picture.getPassword() + ", ");
                }
                deviceCount++;
            }

            logger.trace("setting device labels.");
            setDeviceLabels();

            if (imgCount == 20) {
                break;
            }
        }
        logger.info("Finished generating image thumbnails.");
        return imageList;
    }

    private void resetDeviceLabels() {
        logger.trace("Activated resetDeviceLabels() in " + this.getClass().getName());
        deviceNames.clear();
        deviceCredentials.clear();
        deviceNamesLabel.setText("No device names listed.");
        deviceCredentialsLabel.setText("No device credentials listed.");
        logger.trace("Device labels reset.");
    }

    private void setDeviceLabels() {

        if(!deviceNames.isEmpty()) {
            String names = new String();
            for (String device : deviceNames
            ) {
                names = names + device;
                deviceNamesLabel.setText(names);
            }
        }else {
            deviceNamesLabel.setText("No devices found.");
        }

        if(!deviceCredentials.isEmpty()){
            String credentials = new String();
        for (String credential:deviceCredentials
             ) {
            credentials = credentials + credential;
            deviceCredentialsLabel.setText(credentials);
        }
        }else {
            deviceCredentialsLabel.setText("No device credentials found.");
        }
        logger.trace("Device labels set.");
    }

    /**
     * this method handles the click event on the thumbnails image and open an image dialog to see the selected image in a bigger size
     **/
    private void openImageDialogOnMouseClick(MouseEvent event, Image image, List<Image> imageList) {
        logger.info("openImageDialogOnMouseClick() activated in " + this.getClass().getName());
        try {
            int selectedIndex = imageList.indexOf(image);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/view/externalViews/ImageDialogView.fxml"));
            logger.info("Loading ImageDialogView.fxml");
            Parent root = loader.load();
            ImageDialogViewController controller = loader.getController();
            controller.setImageList(imageList);
            controller.setSelectedImage(image);
            controller.setCurrentImageIndex(selectedIndex);
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

    /**These methods calculate the X and Y location of images based on their amount and size.**/
    private int imageLocationX(int imgCount, int imgWidth) {
        logger.trace("Determining image X coordinates.");
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
        logger.trace("Determining image y coordinates");
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

    private void setDescriptionLabel() {
        if (projectModel.getSelectedTask().getTaskDesc() != null) {
            descriptionLabel.setText(projectModel.getSelectedTask().getTaskDesc());
        }
        logger.trace("Description label set in " + this.getClass().getName());
    }


    /**
     * This will open the New Task View.
     * We catch the IOException and show the user a crafted alert.
     *
     * @param actionEvent triggered by the create task button.
     */
    @FXML
    private void createTask(ActionEvent actionEvent) {
        super.newTask();
    }



    /**
     * This will open the edit task view.
     * We catch the IOException and show the user a crafted alert.
     *
     * @param actionEvent triggered by the update task button.
     */
    @FXML
    private void updateTask(ActionEvent actionEvent) {
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
     *
     * @param actionEvent triggered by the add pictures button.
     */
    @FXML
    private void addPictures(ActionEvent actionEvent) {
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



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        logger.trace("initializing DocumentationController");
        setSelectedProject();
        setUsernameLabel();
        setTaskTV();
        if (authenticationModel.getLoggedInUser().getAccess().toUpperCase().equals("TECHNICIAN")) {
            createTaskButton.setVisible(false);
        }

        if (authenticationModel.getLoggedInUser().getAccess().toUpperCase().equals("SALES")) {
            createTaskButton.setVisible(false);
            layoutButton.setVisible(false);
            picturesButton.setVisible(false);
            updateTaskButton.setVisible(false);
            windowTitleLabel.setText("Customer Documentation");
        }
    }



    @FXML
    private void taskTVOnMouse(MouseEvent mouseEvent) {
        logger.trace("taskTV clicked in " + this.getClass().getName());
        if (taskTV.getSelectionModel().getSelectedItem() != null) {
            projectModel.setSelectedTask(taskTV.getSelectionModel().getSelectedItem());
            if (projectModel.getSelectedTask().getTaskLayout() != null) {
                largeImageView.setImage(projectModel.getSelectedTask().getTaskLayout());
            }
            setDescriptionLabel();
            //images.clear();
            generateImgThumbnails();
            if (mouseEvent.getClickCount() == 2) {
                editTask();
            }
        } else if (mouseEvent.getClickCount() == 2) {
            if (authenticationModel.getLoggedInUser().getAccess().toUpperCase().equals("MANAGER") || authenticationModel.getLoggedInUser().getAccess().toUpperCase().equals("ADMIN")) {
                newTask();
            }
        }

    }

    public void updateLayout(ActionEvent actionEvent) {
        logger.info("updateLayout() called in " + this.getClass().getName());
        if (taskTV.getSelectionModel().getSelectedItem() != null && projectModel.getSelectedTask() != null) {
            try {
                messageLabel.setText("");
                logger.info("Loading EditLayout.fxml");
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/view/newAndUpdateViews/EditLayout.fxml"));
                Parent root = loader.load();
                EditLayoutController controller = loader.getController();
                controller.setUpCanvas();
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
    private void anchorOnClick(MouseEvent mouseEvent) {
        logger.trace("User has clicked the anchor pane in " + this.getClass().getName());
        if (taskTV.getSelectionModel().getSelectedItem() != null) {
            taskTV.getSelectionModel().clearSelection();
            taskTV.refresh();
        }
    }


    /**
     * method to open the window where it's possible to export the report for the selected task
     **/
    @FXML private void openExportReportView(ActionEvent actionEvent) {
        logger.info("openExportReportView called in " + this.getClass().getName());
        if (taskTV.getSelectionModel().getSelectedItem() != null) {
            try {
                logger.trace("Collecting selected project, selected item from task tv, and selected customer.");
                Project project = projectModel.getSelectedProject();
                Task task = taskTV.getSelectionModel().getSelectedItem();
                Customer customer = customerModel.getSelectedCustomer();
                logger.info("Loading ExportReportView.fxml");
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/view/externalViews/ExportReportView.fxml"));
                Parent root = loader.load();
                ExportReportViewController controller = loader.getController();
               List<Image> imageList = generateImgThumbnails();
                controller.setSelectedTask(task);
                controller.setSelectedProject(project);
                controller.setSelectedCustomer(customer);
                controller.setImages(imageList);
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


