package gui.controller;

import be.*;
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


import java.io.*;
import java.net.*;
import java.util.*;


public class DocumentationController implements Initializable {


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
    private Persistent persistenceModel = Persistent.getInstance();
    private Observables observablesModel = Observables.getInstance();
    private Functions functionsModel = new Functions();

    // private variables
    private Project selectedProject;
    private HashMap<String, Image> images = new HashMap<>();
    private List<String> pictureDescriptions = new ArrayList<>();


    /**
     * We use this to set our username label and window title label.
     */
    private void setUsernameLabel() {// set our username label to the users name
        windowTitleLabel.setText("Project Task Manager");
        usernameLabel.setText(persistenceModel.getLoggedInUser().getFirstName() + " " + persistenceModel.getLoggedInUser().getLastName());
    }

    /**
     * We use this to set our tableview with the observable list from Observables.
     */
    private void setTaskTV() {
        taskTV.setItems(observablesModel.getTasksByProject());
        observablesModel.loadTasksByProject(selectedProject);

        taskName.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTaskName()));
        taskState.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTaskState()));

    }

    /**
     * This controller needs the selected project in order to get a list of tasks for it.
     * We fetch the selected project from persistent in order to do this, if there is a problem with this, we show an alert.
     */
    private void setSelectedProject() {
        if (persistenceModel.getSelectedProject() != null) {
            selectedProject = persistenceModel.getSelectedProject();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Project does not exist.");
            alert.setHeaderText("An error has occurred, please contact system admin. Project was not selected or does not exist.");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                alert.close();
            }
        }
    }

    /**
     * this method generates thumbnail images for a list of picture belonging to a specific task and displays them in a pane.
     * A mouse click event is set on each thumbnail to open an image dialog
     */
    private List<Image> generateImgThumbnails() {
        Task task = persistenceModel.getSelectedTask();
        List<TaskPictures> pics = functionsModel.taskPicturesByDocID(task);

        List<Image> imageList = new ArrayList<>();
        for (TaskPictures taskPictures : pics) {
            imageList.add(taskPictures.getPicture());
        }

        imagePane.getChildren().clear();
        images.clear();
        pictureDescriptions.clear();

        int imgCount = 1;

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
            if (imgCount == 20) {
                break;
            }
        }
        return imageList;
    }

    /**
     * this method handles the click event on the thumbnails image and open an image dialog to see the selected image in a bigger size
     **/
    private void openImageDialogOnMouseClick(MouseEvent event, Image image, List<Image> imageList) {
        try {
            int selectedIndex = imageList.indexOf(image);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/view/ImageDialogView.fxml"));
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
            String str = "ImageDialogView.fxml";
            taskError(str);
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

    private void setDescriptionLabel() {
        if (persistenceModel.getSelectedTask().getTaskDesc() != null) {
            descriptionLabel.setText(persistenceModel.getSelectedTask().getTaskDesc());
        }
    }


    /**
     * This will open the New Task View.
     * We catch the IOException and show the user a crafted alert.
     *
     * @param actionEvent triggered by the create task button.
     */
    @FXML
    private void createTask(ActionEvent actionEvent) {
        newTask();
    }

    private void newTask() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/view/NewTask.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            Scene scene = new Scene(root);
            stage.setTitle("Create Task");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            String str = "NewTask.fxml";
            taskError(str);
        }
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
        if (taskTV.getSelectionModel().getSelectedItem() != null) {
            try {
                messageLabel.setText("");
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/view/EditTask.fxml"));
                Parent root = loader.load();
                EditTaskController controller = loader.getController();
                controller.setFieldsOnEdit();
                Stage stage = new Stage();
                Scene scene = new Scene(root);
                stage.setTitle("Update Task");
                stage.setScene(scene);
                stage.show();
            } catch (IOException e) {
                String str = "EditTask.fxml";
                taskError(str);
            }
        } else messageLabel.setText("Please select a task to be updated.");
    }

    /**
     * This will open the add pictures window.
     * We catch the IOException and show the user a crafted alert.
     *
     * @param actionEvent triggered by the add pictures button.
     */
    @FXML
    private void addPictures(ActionEvent actionEvent) {
        if (taskTV.getSelectionModel().getSelectedItem() != null) {
            try {
                messageLabel.setText("");
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/view/AddTaskPictures.fxml"));
                Parent root = loader.load();
                Stage stage = new Stage();
                Scene scene = new Scene(root);
                stage.setTitle("Add Task Pictures");
                stage.setScene(scene);
                stage.show();
            } catch (IOException e) {
                String str = "AddTaskPictures.fxml";
                taskError(str);
            }
        } else messageLabel.setText("Please select a task to add pictures to.");
    }

    /**
     * This will log the user out and change the view to the login.
     * We catch the IOException and show the user a crafted alert.
     *
     * @param actionEvent triggered by the logout button.
     */
    @FXML
    private void logOut(ActionEvent actionEvent) {
        try {
            persistenceModel.setLoggedInUser(null);
            Parent root = FXMLLoader.load(getClass().getResource("/gui/view/Login.fxml"));
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setTitle("WUAV");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            String str = "Login.fxml";
            taskError(str);
        }
    }

    /**
     * If loading any of our views has a problem, we show the user an alert along with the view name.
     *
     * @param str This is the name of the view that is causing the problem.
     */
    private void taskError(String str) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Error loading view");
        alert.setHeaderText("There has been an error loading " + str + ". Please contact system admin.");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            alert.close();
        } else {
            alert.close();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setSelectedProject();
        setUsernameLabel();
        setTaskTV();
        if (persistenceModel.getLoggedInUser().getAccess().toUpperCase().equals("TECHNICIAN")) {
            createTaskButton.setVisible(false);
        }

        if (persistenceModel.getLoggedInUser().getAccess().toUpperCase().equals("SALES")) {
            createTaskButton.setVisible(false);
            layoutButton.setVisible(false);
            picturesButton.setVisible(false);
            updateTaskButton.setVisible(false);
            windowTitleLabel.setText("Customer Documentation");
        }
    }



    @FXML
    private void taskTVOnMouse(MouseEvent mouseEvent) {
        if (taskTV.getSelectionModel().getSelectedItem() != null) {
            persistenceModel.setSelectedTask(taskTV.getSelectionModel().getSelectedItem());
            if (persistenceModel.getSelectedTask().getTaskLayout() != null) {
                largeImageView.setImage(persistenceModel.getSelectedTask().getTaskLayout());
            }
            setDescriptionLabel();
            //images.clear();
            generateImgThumbnails();
            if (mouseEvent.getClickCount() == 2) {
                editTask();
            }
        } else if (mouseEvent.getClickCount() == 2) {
            if (persistenceModel.getLoggedInUser().getAccess().toUpperCase().equals("MANAGER") || persistenceModel.getLoggedInUser().getAccess().toUpperCase().equals("ADMIN")) {
                newTask();
            }
        }

    }

    public void updateLayout(ActionEvent actionEvent) {
        if (taskTV.getSelectionModel().getSelectedItem() != null) {
            try {
                messageLabel.setText("");
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/view/EditLayout.fxml"));
                Parent root = loader.load();
                EditLayoutController controller = loader.getController();
                controller.setLayoutOnEdit();
                controller.setSelectedProjectForLayout();
                controller.setUpCanvas();
                Stage stage = new Stage();
                Scene scene = new Scene(root);
                stage.setTitle("Update Layout");
                stage.setScene(scene);
                stage.show();
            } catch (IOException e) {
                String str = "EditLayout.fxml";
                taskError(str);
            }
        } else messageLabel.setText("Please select a task to update the layout.");
    }

    @FXML
    private void anchorOnClick(MouseEvent mouseEvent) {
        if (taskTV.getSelectionModel().getSelectedItem() != null) {
            taskTV.getSelectionModel().clearSelection();
            taskTV.refresh();
        }
    }



/**method to open the window where it's possible to export the report for the selected task**/
    @FXML private void openExportReportView(ActionEvent actionEvent) {

        if (taskTV.getSelectionModel().getSelectedItem() != null) {
            try {
                Project project = persistenceModel.getSelectedProject();
                Task task = taskTV.getSelectionModel().getSelectedItem();
                Customer customer = persistenceModel.getSelectedCustomer();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/view/ExportReportView.fxml"));
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
                String str = "ExportReportView.fxml";
                taskError(str);
            }

        } else {
            messageLabel.setText("Please select a task to export its report");
        }
    }
}


