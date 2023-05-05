package gui.controller;

import be.*;
import gui.model.*;
import javafx.beans.property.*;
import javafx.event.ActionEvent;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.*;
import javafx.stage.*;


import java.io.*;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class ManageProjectController{

    // TableViews
    @FXML private TableView<ProjectWrapper> projectTV;
    @FXML private TableColumn<ProjectWrapper, String> projectName;
    @FXML private TableColumn<ProjectWrapper, Date> projectDate;
    @FXML private TableColumn<ProjectWrapper, Integer> assignedUserCount;

    @FXML private TableView<UserWrapper> techTV;
    @FXML private TableColumn<UserWrapper, String> techName;
    @FXML private TableColumn<UserWrapper, String> techSurname;
    @FXML private TableColumn<UserWrapper, Integer> numberOfTasks;

    // Labels
    @FXML private Label usernameLabel;
    @FXML private Label windowTitleLabel;

    // Models
    private Persistent persistenceModel;
    private Observables observablesModel;
    private Functions functionsModel;


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

        setUsernameLabel();
        setProjectTV();
        setTechTV();
    }

    /**
     * We use this to set our username label and window title label.
     */
    private void setUsernameLabel() {// set our username label to the users name and our window title label.
        windowTitleLabel.setText("Project Management");
        usernameLabel.setText(persistenceModel.getLoggedInUser().getFirstName() + " " + persistenceModel.getLoggedInUser().getLastName());
    }

    /**
     * We use this to set up the tableview ProjectTV with relative columns and add a listener for selected items.
     */
    private void setProjectTV() {
        projectTV.setItems(observablesModel.getProjects());
        observablesModel.loadProjects();

        projectDate.setCellValueFactory(new PropertyValueFactory<>("dateCreated"));
        projectDate.setCellFactory(column -> {
            return new TableCell<ProjectWrapper, Date>() {
                @Override
                protected void updateItem(Date date, boolean empty) {
                    super.updateItem(date, empty);
                    if (empty || date == null) {
                        setText(null);
                    } else {
                        setText(new SimpleDateFormat("yyyy-MM-dd").format(date));
                    }
                }
            };
        });

        projectName.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getProject().getProjName()));
        assignedUserCount.setCellValueFactory(cellData ->  new SimpleIntegerProperty(cellData.getValue().getTotalTasks()).asObject());

        projectTV.getSelectionModel().selectedItemProperty().addListener(((((observable, oldValue, selectedProject) ->
                persistenceModel.setSelectedProject(selectedProject.getProject())
                ))));
    }
    /**
     * We use this to set up the tableview TechTV with relative columns and add a listener for selected items.
     */
    private void setTechTV() {
        techTV.setItems(observablesModel.getTechs());
        observablesModel.loadTechs();

        techName.setCellValueFactory(cellData ->new SimpleStringProperty(cellData.getValue().getUser().getFirstName()));
        techSurname.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getUser().getLastName()));
        numberOfTasks.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getAssignedTasks()).asObject());

        techTV.getSelectionModel().selectedItemProperty().addListener((((observable, oldValue, selectedUser) -> {
            persistenceModel.setSelectedUser(selectedUser.getUser());
        })));
    }

    /**
     * Assigns a project to the selected user.
     * @param actionEvent triggered when the user activates the assign project button.
     */
    @FXML private void assignProject(ActionEvent actionEvent){
        if(projectTV.getSelectionModel().getSelectedItem() != null && techTV.getSelectionModel().getSelectedItem() != null){
            functionsModel.assignProject(persistenceModel.getSelectedUser(), persistenceModel.getSelectedProject());
        }
    }

    /**
     * Deletes the selected project.
     * @param actionEvent triggered when the user activates the delete project button.
     */
    @FXML private void deleteProject(ActionEvent actionEvent){
        if(projectTV.getSelectionModel().getSelectedItem() != null){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete Project");
            alert.setHeaderText("Are you sure you wish to delete the project " + persistenceModel.getSelectedProject().getProjName() + "?");

            Optional<ButtonType> result = alert.showAndWait();
            if(result.isPresent() && result.get() == ButtonType.OK){
                functionsModel.deleteProject(persistenceModel.getSelectedProject());
                alert.close();
            } else {
                alert.close();
            }
        }
    }

    /**
     * Opens the edit window for the selected project.
     * @param actionEvent triggers when the user activates the edit project button.
     */
    @FXML private void editProject(ActionEvent actionEvent){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/view/NEProject.fxml"));
            Parent root = loader.load();
            NEProjectController controller = loader.getController();
            controller.setNEProjectController(true, persistenceModel, functionsModel);
            Stage stage = new Stage();
            Scene scene = new Scene(root);
            stage.setTitle("Edit Project");
            stage.setScene(scene);
            stage.show();
        }catch (IOException e){
            String str = "There has been an error loading NEProject.fxml. Please contact system Admin.";
            projectError(str);
        }
    }

    /**
     * Opens the new window for the selected project.
     * @param actionEvent triggers when the user activates the new project button.
     */
    @FXML private void createProject(ActionEvent actionEvent){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/view/NEProject.fxml"));
            Parent root = loader.load();
            NEProjectController controller = loader.getController();
            controller.setNEProjectController(false, persistenceModel, functionsModel);
            Stage stage = new Stage();
            Scene scene = new Scene(root);
            stage.setTitle("Create Project");
            stage.setScene(scene);
            stage.show();
        }catch (IOException e){
            String str = "There has been an error loading NEProject.fxml. Please contact system Admin.";
            projectError(str);
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
            String str = "There has been an error loading Login.fxml. Please contact system Admin.";
            projectError(str);
        }
    }

    /**
     * We use this to display an error to the user if there is a problem.
     * @param str This is the source of the problem so that the user is informed.
     */
    private void projectError(String str) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Error");
        alert.setHeaderText(str);

        Optional<ButtonType> result = alert.showAndWait();
        if(result.isPresent() && result.get() == ButtonType.OK){
            alert.close();
        } else {
            alert.close();
        }
    }

}
