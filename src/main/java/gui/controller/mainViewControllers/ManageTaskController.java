package gui.controller.mainViewControllers;

import be.*;
import gui.model.*;
import javafx.beans.property.*;
import javafx.event.ActionEvent;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.input.*;
import org.apache.logging.log4j.*;


import java.io.*;
import java.net.URL;
import java.util.*;

public class ManageTaskController extends BaseController implements Initializable{
    // FXML
    @FXML private TableView<TaskWrapper> taskTV;
    @FXML private TableColumn<TaskWrapper, String> projectName;
    @FXML private TableColumn<TaskWrapper, String> taskName;
    @FXML private TableColumn<TaskWrapper, String> stateOfTask;
    @FXML private TableColumn<TaskWrapper, String> customerName;

    // Labels
    @FXML private Label usernameLabel;
    @FXML private Label errorLabel;

    // Models
    private final ProjectModel projectModel = ProjectModel.getInstance();
    private final AuthenticationModel authenticationModel = AuthenticationModel.getInstance();
    private final CustomerModel customerModel = CustomerModel.getInstance();

    private static final Logger logger = LogManager.getLogger("debugLogger");


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        logger.trace("Initializing ManageTaskController.");
        setUsernameLabel();

        logger.info("Checking user access in ManageTaskController.");

        // The TV is filled with different information based on the user access
        if(authenticationModel.getLoggedInUser().getAccess().equalsIgnoreCase("TECHNICIAN"))
        {
            User loggedIn = authenticationModel.getLoggedInUser();
            taskTV.setItems(projectModel.getTasksByUserID());
            projectModel.loadProjectsByUser(loggedIn);
            customerName.setVisible(false);
            projectName.setMinWidth(projectName.getWidth() + 90);
            taskName.setMinWidth(taskName.getWidth() + 90);
            stateOfTask.setMinWidth(stateOfTask.getWidth() + 90);

        }else {
            taskTV.setItems(projectModel.getTasksInfo());
            projectModel.loadAllProjLists();
            customerName.setCellValueFactory(cellData-> new SimpleStringProperty(cellData.getValue().getCustomer().getCustName()));
        }

        logger.trace("setting column values for taskTV in ManageTaskController");
        projectName.setCellValueFactory(cellData-> new SimpleStringProperty(cellData.getValue().getProject().getProjName()));
        taskName.setCellValueFactory(cellData-> new SimpleStringProperty(cellData.getValue().getTask().getTaskName()));
        stateOfTask.setCellValueFactory(cellData-> new SimpleStringProperty(cellData.getValue().getTask().getTaskState()));

        logger.trace("Initialize complete.");
    }

    /**
     * We use this to set our username label and window title label.
     */
    private void setUsernameLabel() {// set our username label to the users name.
        logger.trace("setting username label in ManageTaskController.");
        usernameLabel.setText(authenticationModel.getLoggedInUser().getFirstName() + " " + authenticationModel.getLoggedInUser().getLastName());
    }


    @FXML private void logOut(ActionEvent actionEvent){
        super.logout(actionEvent);
    }

    /**
     * Open the documentation for the selected project
     */
    @FXML private void openProjectInfo()  {
        if(taskTV.getSelectionModel().getSelectedItem() != null){
            openProjectInformation();
        } else errorLabel.setText("Please select a task to view documentation.");
    }

    /**
     * Set relevant information when an item is clicked in the tv
     * @param mouseEvent if an item is double-clicked, open documentation window.
     */
    @FXML private void taskTVOnClick(MouseEvent mouseEvent) {
        logger.trace("User clicked taskTV in ManageTaskController");
        if(taskTV.getSelectionModel().getSelectedItem() != null){
            errorLabel.setText("");
            projectModel.setSelectedProject(taskTV.getSelectionModel().getSelectedItem().getProject());
            projectModel.setSelectedTask(taskTV.getSelectionModel().getSelectedItem().getTask());
            customerModel.setSelectedCustomer(taskTV.getSelectionModel().getSelectedItem().getCustomer());
            if(mouseEvent.getClickCount() == 2){
                openProjectInformation();
            }
        }

    }

    /**
     * Open the documentation for the selected project.
     */
    private void openProjectInformation(){
        logger.info("openProjectInformation() called in ManageTaskController");
        try {
            errorLabel.setText("");
            projectModel.setSelectedProject(taskTV.getSelectionModel().getSelectedItem().getProject());
            logger.info("loading DocumentationView.fxml");
            Node n = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/gui/view/mainViews/DocumentationView.fxml")));
            super.getViewPane().getChildren().setAll(n);
        }  catch (IOException e) {
            logger.warn("User may not have selected a task to see information for, user was informed. Potential problem: ", e);
            errorLabel.setText("Please select an task to see related documentation.");
        }
    }

    /**
     * Clear the selected item in the tv
     */
    @FXML private void taskAnchorOnClick() {
        logger.trace("User clicked anchor pane in ManageTaskController.");
        if(taskTV.getSelectionModel().getSelectedItem() != null){
            taskTV.getSelectionModel().clearSelection();
        }
        taskTV.refresh();
    }
}
