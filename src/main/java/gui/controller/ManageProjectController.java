package gui.controller;

import be.*;
import gui.model.*;
import javafx.beans.property.*;
import javafx.fxml.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.*;

import java.awt.event.*;
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

        //setUsernameLabel();
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
     * We use this to set up the tableview ProjectTV with relative columns.
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

    }
    /**
     * We use this to set up the tableview TechTV with relative columns.
     */
    private void setTechTV() {
        techTV.setItems(observablesModel.getTechs());
        observablesModel.loadTechs();

        //techName.setCellValueFactory(new PropertyValueFactory<>("name"));
        //techSurname.setCellValueFactory(new PropertyValueFactory<>("last_name"));
        techName.setCellValueFactory(cellData ->new SimpleStringProperty(cellData.getValue().getUser().getFirstName()));
        techSurname.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getUser().getLastName()));
        numberOfTasks.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getAssignedTasks()).asObject());
    }

    @FXML private void assignProject(ActionEvent actionEvent){}

    @FXML private void deleteProject(ActionEvent actionEvent){}

    @FXML private void editProject(ActionEvent actionEvent){}

    @FXML private void createProject(ActionEvent actionEvent){}
    @FXML private void logOut(ActionEvent actionEvent){}


}
