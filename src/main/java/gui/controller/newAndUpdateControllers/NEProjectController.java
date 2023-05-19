package gui.controller.newAndUpdateControllers;

import be.*;
import gui.controller.mainViewControllers.*;
import gui.model.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.*;
import javafx.scene.control.*;
import javafx.stage.*;
import org.apache.logging.log4j.*;


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

    //validation
    int maxProjName = 50;

    private static final Logger logger = LogManager.getLogger("debugLogger");

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
        logger.info("Creating a new project");
        if (!projectName.getText().isEmpty()) {
            if (validateProjectNameTFLength()) {
                project = new Project(projectName.getText(), java.sql.Date.valueOf(java.time.LocalDate.now()), nEProjectTV.getSelectionModel().getSelectedItem().getCustID());
                projectModel.createProject(project);

                projectModel.loadProjects();
                logger.info("New project created");
                Stage stage = (Stage) createOrEditProject.getScene().getWindow();
                stage.close();
            } else {
                alertProjectNameTF();
                logger.warn("Project creation failed: invalid project name length");
            }
        } else {
            String str = "Please fill in the project name";
            super.createWarning(str);
            logger.warn("Project creation failed: empty project name");
        }
    }

    /**
     * This method is used to edit the selected project from our persistent model with updated information.
     */
    private void editProject() {
        logger.info("Editing of a project");
        if (!projectName.getText().isEmpty()) {
            if (validateProjectNameTFLength()) {
                project.setProjName(projectName.getText());

                projectModel.editProject(project);

                projectModel.loadProjects();
                logger.info("Project edited");
                Stage stage = (Stage) createOrEditProject.getScene().getWindow();
                stage.close();
            } else {
                alertProjectNameTF();
                logger.warn("Project update failed: invalid project name length");
            }
        } else {
            String str = "Please fill in the project name";
            super.createWarning(str);
            logger.warn("Project update failed: empty project name");
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


    /**
     * This method checks if the length of the textfield is bigger than the max length for the field
     * it returns true if the length is okay, false if it's too long
     **/
    private boolean validateProjectNameTFLength() {
        if (projectName.getText().length() > maxProjName) {
            return false;
        } else {
            return true;
        }
    }


    /**
     * this method shows an alert to the user if the inserted text field length exceeds the max
     **/
    private void alertProjectNameTF() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Validate Project Name");
        alert.setContentText("Project name is too long, max is 50 characters.");
        alert.showAndWait();
    }
}
