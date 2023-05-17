package gui.controller.newAndUpdateControllers;

import be.*;
import gui.controller.mainViewControllers.*;
import gui.model.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.*;
import javafx.scene.control.*;
import javafx.stage.*;


import java.net.URL;
import java.util.*;

public class NEProjectController extends BaseController {
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
    private ProjectModel projectModel = ProjectModel.getInstance();
    private CustomerModel customerModel = CustomerModel.getInstance();

    // True if editing, false if creating a new customer.
    private boolean isEdit;

    private Date creationDate;

    /**
     * This method is used to set our models and choose if we are editing or creating a project.
     * @param isEdit          if true, we are editing a project.
     */
    public void setNEProjectController(Boolean isEdit) {
        setNEProjectTV();
        setEdit(isEdit); // sets the boolean to store if we are editing or creating.
        if (isEdit) { // if we are editing we set all text fields with the current information.
            setOnEdit();
            createOrEditProject.setText("Edit");
        } else {
            windowTitleLabel.setText("New Project");
            createOrEditProject.setText("Create");
        }

    }

    public void setNEProjectTV() {
        nEProjectTV.setItems(customerModel.getCustomers());
        customerModel.loadCustomers();

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
            projectModel.createProject(project);

            projectModel.loadProjects();
            Stage stage = (Stage) createOrEditProject.getScene().getWindow();
            stage.close();
        } else {
            String str = "Please fill in the project name";
            super.createWarning(str);
        }
    }

    /**
     * This method is used to edit the selected project from our persistent model with updated information.
     */
    private void editProject() {
        if(!projectName.getText().isEmpty()) {
            project.setProjName(projectName.getText());

            projectModel.editProject(project);

            projectModel.loadProjects();
            Stage stage = (Stage) createOrEditProject.getScene().getWindow();
            stage.close();
        } else {
            String str = "Please fill in the project name";
            super.createWarning(str);
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
        if (projectModel.getSelectedProject() != null) {
            nEProjectTV.setVisible(false);
            windowTitleLabel.setText("Edit Project");
            project = projectModel.getSelectedProject();
            projectName.setText(project.getProjName());

        } else {
            String str = "Could not find a project to be edited. Please contact system admin. Class: NEProjectController.";
            super.createWarning(str);
        }
    }




    public void setEdit(boolean edit) {
        isEdit = edit;
    }


}
