package gui.controller;

import be.*;
import gui.model.*;
import javafx.beans.property.*;
import javafx.fxml.*;
import javafx.scene.control.*;
import javafx.scene.image.*;

import java.awt.event.*;
import java.util.*;

public class DocumentationController {
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
    private Persistent persistenceModel;
    private Observables observablesModel;
    private Functions functionsModel;

    // private variables
    private Project selectedProject;


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

    private void setDescriptionLabel() {}

    @FXML private void createTask(ActionEvent actionEvent){}
    @FXML private void selectImage(ActionEvent actionEvent){}
    @FXML private void updateTask(ActionEvent actionEvent){}
    @FXML private void addPictures(ActionEvent actionEvent){}
    @FXML private void logOut(ActionEvent actionEvent){}

}
