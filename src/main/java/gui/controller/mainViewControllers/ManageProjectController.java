package gui.controller.mainViewControllers;

import be.*;
import gui.controller.newAndUpdateControllers.*;
import gui.model.*;
import javafx.beans.property.*;
import javafx.event.ActionEvent;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.stage.*;
import org.apache.logging.log4j.*;


import java.io.*;
import java.net.*;
import java.util.*;

public class ManageProjectController extends BaseController implements Initializable {

    // TableViews
    @FXML
    private TableView<ProjectWrapper> projectTV;
    @FXML
    private TableColumn<ProjectWrapper, String> projectName;
    @FXML
    private TableColumn<ProjectWrapper, Date> projectDate;
    @FXML
    private TableColumn<ProjectWrapper, Integer> assignedUserCount;

    @FXML
    private TableView<UserWrapper> techTV;
    @FXML
    private TableColumn<UserWrapper, String> techName;
    @FXML
    private TableColumn<UserWrapper, String> techSurname;
    @FXML
    private TableColumn<UserWrapper, Integer> numberOfTasks;

    // Labels
    @FXML
    private Label usernameLabel;
    @FXML
    private Label messageLabel;

    // Models
    private final ProjectModel projectModel = ProjectModel.getInstance();
    private final AuthenticationModel authenticationModel = AuthenticationModel.getInstance();
    private final UserModel userModel = UserModel.getInstance();

    private static final Logger logger = LogManager.getLogger("debugLogger");

    /**
     * We use this to set our username label and window title label.
     */
    private void setUsernameLabel() {// set our username label to the users name and our window title label.
        logger.trace("Setting user name label in ManageProjectController.");
        //windowTitleLabel.setText("Project and Technician management");
        usernameLabel.setText(authenticationModel.getLoggedInUser().getFirstName() + " " + authenticationModel.getLoggedInUser().getLastName());
    }

    /**
     * We use this to set up the tableview ProjectTV with relative columns and add a listener for selected items.
     */
    private void setProjectTV() {
        logger.info("setProjectTV() called in " + this.getClass().getName());
        projectTV.setItems(projectModel.getProjects());
        projectModel.loadAllProjLists();

        logger.trace("setting columns for projectTV");
        projectDate.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getProject().getProjDate()));
        projectName.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getProject().getProjName()));
        assignedUserCount.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getTotalTasks()).asObject());
        logger.info("setProjectTV() complete.");
    }

    /**
     * We use this to set up the tableview TechTV with relative columns and add a listener for selected items.
     */
    private void setTechTV() {
        logger.info("setTechTV() called in " + this.getClass().getName());
        techTV.setItems(userModel.getTechs());
        userModel.loadTechs();

        logger.trace("Setting columns for techTV");
        techName.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getUser().getFirstName()));
        techSurname.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getUser().getLastName()));
        numberOfTasks.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getAssignedTasks()).asObject());

        logger.info("setTechTV() complete.");
    }

    /**
     * Assigns a project to the selected user.
     *
     */
    @FXML
    private void assignProject() {
        logger.info("assignProject() called in " + this.getClass().getName());
        if (projectTV.getSelectionModel().getSelectedItem() != null && techTV.getSelectionModel().getSelectedItem() != null) {
            if (projectTV.getSelectionModel().getSelectedItem().getTotalTasks() > 0) {
                projectModel.assignProject(userModel.getSelectedUser(), projectModel.getSelectedProject());
                userModel.loadTechs();
                projectModel.loadAllProjLists();
            }
        } else
        {
            logger.warn("User attempted to assign a tech to a project without task. User was notified.");
            String str = "Only projects with tasks can be assigned to a technician";
            super.createWarning(str);
        }
        logger.info("assignProject() complete.");
    }


    /**
     * Deletes the selected project.
     */
    @FXML
    private void deleteProject() {
        logger.info("deleteProject() called in " + this.getClass().getName());
        if (projectTV.getSelectionModel().getSelectedItem() != null) {
            messageLabel.setText("");
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete Project");
            alert.setHeaderText("Are you sure you wish to delete the project " + projectModel.getSelectedProject().getProjName() + "?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                projectModel.deleteProject();
                alert.close();
            } else {
                alert.close();
            }
            projectModel.loadAllProjLists();
        } else {messageLabel.setText("Please select a task to be deleted.");}
        logger.info("deleteProject() complete.");
    }


    /**
     * Opens the new window for the selected project.
     */
    @FXML
    private void createProject() {
        editProj(false);
    }

    /**
     * Opens the edit window for the selected project.
     */
    @FXML
    private void editProject() {
        if(projectTV.getSelectionModel().getSelectedItem() != null){
            messageLabel.setText("");
            editProj(true);
        } else {messageLabel.setText("Please select a project to be edited.");}
    }


    /**
     * This method is called when we wish to edit or create a project.
     * @param isEdit if true we are editing the project.
     */
    private void editProj(boolean isEdit){
        logger.info("editProj() was called in " + this.getClass().getName());
        try {
            logger.info("Loading NEProject.fxml");
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/view/newAndUpdateViews/NEProject.fxml"));
            Parent root = loader.load();
            NEProjectController controller = loader.getController();
            controller.setNEProjectController(isEdit);
            Stage stage = new Stage();
            Scene scene = new Scene(root);

            if(isEdit){
                stage.setTitle("Edit Project");
            } else {
                stage.setTitle("New Project");
            }

            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            logger.error("There was a problem loading NEProject.fxml.", e);
            String str = "There has been an error loading NEProject.fxml. Please contact system Admin.";
            super.createWarning(str);
        }
        logger.info("editProj() complete.");
    }

    /**
     * This will log the user out and change the view to the login.
     * @param actionEvent triggered by the logout button.
     */
    @FXML
    private void logOut(ActionEvent actionEvent) {
       super.logout(actionEvent);
    }


    /**
     * This will open the New Task View.
     */
    @FXML
    private void createTask() {
        addTask();
    }

    private void addTask(){
        if (projectTV.getSelectionModel().getSelectedItem() != null){
            messageLabel.setText("");
            super.newTask();
        } else {messageLabel.setText("Please select a project to add tasks to.");}
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        logger.trace("Initializing ManageProjectController");
        setUsernameLabel();
        setProjectTV();
        setTechTV();
    }


    @FXML private void projectTVOnClick(MouseEvent mouseEvent) {
        logger.trace("User clicked projectTV in ManageProjectController.");
        messageLabel.setText("");
        if(projectTV.getSelectionModel().getSelectedItem() != null){
        projectModel.setSelectedProject(projectTV.getSelectionModel().getSelectedItem().getProject());
        if(mouseEvent.getClickCount() == 2){
            addTask();
        }
        }

        if(projectTV.getSelectionModel().getSelectedItem() == null){
            if(mouseEvent.getClickCount() == 2){
                editProj(false);
            }
        }
    }

    @FXML private void techTvOnClick() {
        logger.trace("User clicked techTV in ManageProjectController.");
        messageLabel.setText("");
        if(techTV.getSelectionModel().getSelectedItem() != null) {
            userModel.setSelectedUser(techTV.getSelectionModel().getSelectedItem().getUser());
        }
    }

    @FXML private void anchorOnClick() {
        logger.trace("User clicked anchorPane in ManageProjectController.");
        if(techTV.getSelectionModel().getSelectedItem() != null){
            techTV.getSelectionModel().clearSelection();
        }
        if(projectTV.getSelectionModel().getSelectedItem() != null){
            projectTV.getSelectionModel().clearSelection();
        }
        techTV.refresh();
        projectTV.refresh();
    }
}
