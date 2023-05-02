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
    @FXML private TableView<Task> taskTV;
    @FXML private TableColumn<Task, String> taskName;
    @FXML private TableColumn<Task, String> taskState;

    @FXML private Label windowTitleLabel;
    @FXML private Label descriptionLabel;
    @FXML private Label imageComment;
    @FXML private Label usernameLabel;

    @FXML private ImageView largeImageView;

    private Persistent persistenceModel;
    private Observables observablesModel;
    private Project selectedProject;

    private void setUsernameLabel() {// set our username label to the users name
        windowTitleLabel.setText("Project Task Manager");
        usernameLabel.setText(persistenceModel.getLoggedInUser().getFirstName() + " " + persistenceModel.getLoggedInUser().getLastName());
    }

    private void setTaskTV() {
        taskTV.setItems(observablesModel.getTasksByProject());
        observablesModel.loadTasksByProject(selectedProject);

        taskName.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTaskName()));
        taskName.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTaskState()));
    }

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
