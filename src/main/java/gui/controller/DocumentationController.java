package gui.controller;

import be.*;
import gui.model.*;
import javafx.beans.property.*;
import javafx.event.ActionEvent;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.stage.*;


import java.io.*;
import java.net.*;
import java.util.*;

public class DocumentationController implements Initializable{

    // Tableview
    @FXML private TableView<Task> taskTV;
    @FXML private TableColumn<Task, String> taskName;
    @FXML private TableColumn<Task, String> taskState;

    // Labels
    @FXML private Label windowTitleLabel;
    @FXML private Label descriptionLabel;
    @FXML private Label imageComment;
    @FXML private Label usernameLabel;

    // ImageViews
    @FXML private ImageView largeImageView;

    // Models
    private Persistent persistenceModel = Persistent.getInstance();
    private Observables observablesModel = Observables.getInstance();
    private Functions functionsModel = new Functions();

    // private variables
    private Project selectedProject = persistenceModel.getSelectedProject();


    /**
     * We call this when this controller is called from navigation to set our models, tableview, and labels.
     * @param persistenceModel this is our instance of Persistent from navigation
     * @param observablesModel this is our instance of Observables from navigation
     * @param functionsModel this is our instance of Functions from navigation
     */
    public void userController(Persistent persistenceModel, Observables observablesModel, Functions functionsModel){
        this.persistenceModel = persistenceModel;
        this.functionsModel = functionsModel;
        this.observablesModel = observablesModel;

        setSelectedProject();
        setUsernameLabel();
        setTaskTV();
    }

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
        taskName.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTaskState()));

        taskTV.getSelectionModel().selectedItemProperty().addListener((((observable, oldValue, selectedTask) -> {
            persistenceModel.setSelectedTask(selectedTask);
            setDescriptionLabel();
            //generateImgThumbnails();
        })));
    }

    /**
     * This controller needs the selected project in order to get a list of tasks for it.
     * We fetch the selected project from persistent in order to do this, if there is a problem with this, we show an alert.
     */
    private void setSelectedProject(){
        if(persistenceModel.getSelectedProject() != null){
            selectedProject = persistenceModel.getSelectedProject();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Project does not exist.");
            alert.setHeaderText("An error has occurred, please contact system admin. Project was not selected or does not exist.");

            Optional<ButtonType> result = alert.showAndWait();
            if(result.get() == ButtonType.OK){
                alert.close();
            }
        }
    }
    private void generateImgThumbnails() {}

    private void setDescriptionLabel() {
        if(persistenceModel.getSelectedTask().getTaskDesc() != null){
        descriptionLabel.setText(persistenceModel.getSelectedTask().getTaskDesc());
        }
    }
    @FXML private void selectImage(ActionEvent actionEvent){}

    /**
     *  This will open the New Task View.
     *  We catch the IOException and show the user a crafted alert.
     * @param actionEvent triggered by the create task button.
     */
    @FXML private void createTask(ActionEvent actionEvent){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/view/NewTask.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            Scene scene = new Scene(root);
            stage.setTitle("Create Task");
            stage.setScene(scene);
            stage.show();
        }catch (IOException e){
            String str = "NewTask.fxml";
            taskError(str);
        }
    }

    /**
     * This will open the edit task view.
     * We catch the IOException and show the user a crafted alert.
     * @param actionEvent triggered by the update task button.
     */
    @FXML private void updateTask(ActionEvent actionEvent){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/view/EditTask.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            Scene scene = new Scene(root);
            stage.setTitle("Update Task");
            stage.setScene(scene);
            stage.show();
        }catch (IOException e){
            String str = "EditTask.fxml";
            taskError(str);
        }
    }

    /**
     * This will open the add pictures window.
     * We catch the IOException and show the user a crafted alert.
     * @param actionEvent triggered by the add pictures button.
     */
    @FXML private void addPictures(ActionEvent actionEvent){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/view/AddTaskPictures.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            Scene scene = new Scene(root);
            stage.setTitle("Add Task Pictures");
            stage.setScene(scene);
            stage.show();
        }catch (IOException e){
            String str = "AddTaskPictures.fxml";
            taskError(str);
        }
    }

    /**
     * This will log the user out and change the view to the login.
     * We catch the IOException and show the user a crafted alert.
     * @param actionEvent triggered by the logout button.
     */
    @FXML private void logOut(ActionEvent actionEvent){
        try {
            persistenceModel.setLoggedInUser(null);
            Parent root = FXMLLoader.load(getClass().getResource("/gui/view/Login.fxml"));
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setTitle("WUAV");
            stage.setScene(scene);
            stage.show();
        }catch (IOException e){
            String str = "Login.fxml";
            taskError(str);
        }
    }

    /**
     * If loading any of our views has a problem, we show the user an alert along with the view name.
     * @param str This is the name of the view that is causing the problem.
     */
    private void taskError(String str) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Error loading view");
        alert.setHeaderText("There has been an error loading " + str +". Please contact system admin.");

        Optional<ButtonType> result = alert.showAndWait();
        if(result.isPresent() && result.get() == ButtonType.OK){
            alert.close();
        } else {
            alert.close();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setTaskTV();
    }
}
