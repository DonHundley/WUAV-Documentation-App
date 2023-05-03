package gui.controller;

import be.*;
import gui.model.*;
import javafx.beans.property.*;
import javafx.fxml.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.*;

import java.awt.event.*;
import java.util.*;

public class ManageProjectController {

    // TableViews
    @FXML private TableView<Project> projectTV;
    @FXML private TableColumn<Project, String> projectName;
    @FXML private TableColumn<Project, Date> projectDate;
    @FXML private TableColumn<Project, Integer> assignedUserCount;

    @FXML private TableView<User> techTV;
    @FXML private TableColumn<User, String> techName;
    @FXML private TableColumn<User, String> techSurname;
    @FXML private TableColumn<User, Integer> numberOfTasks;

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
     * Mixed list
     * This will be changed later
     */
    private void setProjectTV() {
        projectTV.setItems(observablesModel.getProjects());
        observablesModel.loadProjects();

        projectDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        projectName.setCellValueFactory(new PropertyValueFactory<>("name"));
        assignedUserCount.setCellValueFactory(new PropertyValueFactory<>("count")); // needs changing


    }

    /**
     * Mixed list
     * This will be changed later
     */
    private void setTechTV() {
        techTV.setItems(observablesModel.getTechs());
        observablesModel.loadTechs();

        techName.setCellValueFactory(new PropertyValueFactory<>("name"));
        techSurname.setCellValueFactory(new PropertyValueFactory<>("last_name"));
        numberOfTasks.setCellValueFactory(new PropertyValueFactory<>("number")); // needs changing
    }

    @FXML private void assignProject(ActionEvent actionEvent){}

    @FXML private void deleteProject(ActionEvent actionEvent){}

    @FXML private void editProject(ActionEvent actionEvent){}

    @FXML private void createProject(ActionEvent actionEvent){}
    @FXML private void logOut(ActionEvent actionEvent){}
}