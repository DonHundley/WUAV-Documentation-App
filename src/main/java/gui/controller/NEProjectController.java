package gui.controller;

import be.*;
import gui.model.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.*;
import javafx.scene.control.*;
import javafx.stage.*;


import java.net.URL;
import java.util.*;

public class NEProjectController implements Initializable {
    @FXML
    private TableView<Customer> nEProjectTV;
    @FXML
    private TableColumn<Customer, String> customerName, customerEmail, customerAddress;
    @FXML
    private TextField projectName;
    @FXML
    private Label windowTitleLabel;
    @FXML
    private Button cancelButton;
    @FXML
    private Button createOrEditProject;

    private Project project;

    // Model instances
    private Observables observablesModel = Observables.getInstance();
    private Persistent persistentModel = Persistent.getInstance();
    private Functions functionsModel = new Functions();

    // True if editing, false if creating a new customer.
    private boolean isEdit;

    private Date creationDate;

    /**
     * This method is used to set our models and choose if we are editing or creating a project.
     *
     * @param isEdit          if true, we are editing a project.
     * @param persistentModel the instance of the persistent model
     * @param functionsModel  the instance of the functions model
     */
    public void setNEProjectController(Boolean isEdit, Persistent persistentModel, Functions functionsModel) {
        this.persistentModel = persistentModel;
        this.functionsModel = functionsModel;

        setEdit(isEdit); // sets the boolean to store if we are editing or creating.
        if (isEdit) { // if we are editing we set all text fields with the current information.
            setOnEdit();
        } else {
            windowTitleLabel.setText("New Project");
        }

    }

    public void setNEProjectTV() {
        nEProjectTV.setItems(observablesModel.getCustomers());
        observablesModel.loadCustomers();

        customerName.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCustName()));
        customerEmail.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCustEmail()));
        customerAddress.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCustAddress()));


    }

    /**
     * This will create or edit a Project based on the isEdit boolean
     *
     * @param actionEvent triggered when the user activates the create/edit button.
     */
    @FXML
    private void createOrEditProject(ActionEvent actionEvent) {
        if (isEdit) {
            editProject();
        } else {
            createProject();
        }
    }

    /**
     * This method is used to create a new project if our isEdit boolean == false.
     */
    private void createProject() {
        if (!projectName.getText().isEmpty()) {
            project = new Project(projectName.getText(), java.sql.Date.valueOf(java.time.LocalDate.now()), nEProjectTV.getSelectionModel().getSelectedItem().getCustID());
            functionsModel.createProject(project);

            observablesModel.loadProjects();
            Stage stage = (Stage) createOrEditProject.getScene().getWindow();
            stage.close();
        } else {
            String str = "Please fill in the project name";
            projectError(str);
        }
    }

    /**
     * This method is used to edit the selected project from our persistent model with updated information.
     */
    private void editProject() {
        if(!projectName.getText().isEmpty())
        {
        project.setProjName(projectName.getText());

        functionsModel.editProject(project);

        observablesModel.loadProjects();
        Stage stage = (Stage) createOrEditProject.getScene().getWindow();
        stage.close();
        } else {
        String str = "Please fill in the project name";
        projectError(str);
        }

    }

    /**
     * Closes the window with an action event.
     *
     * @param actionEvent triggers when the user activates the cancel button.
     */
    @FXML
    private void cancel(ActionEvent actionEvent) {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    /**
     * If the boolean isEdit == true, we set all the fields with the information from the selected customer.
     */
    private void setOnEdit() {
        if (persistentModel.getSelectedProject() != null) {
            nEProjectTV.setVisible(false);
            windowTitleLabel.setText("Edit Project");
            project = persistentModel.getSelectedProject();
            projectName.setText(project.getProjName());

        } else {
            String str = "Could not find a project to be edited. Please contact system admin. Class: NEProjectController.";
            projectError(str);
        }
    }


    /**
     * We use this to display an error to the user if there is a problem.
     *
     * @param str This is the source of the problem so that the user is informed.
     */
    private void projectError(String str) {
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

    public void setEdit(boolean edit) {
        isEdit = edit;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setNEProjectTV();
    }
}
