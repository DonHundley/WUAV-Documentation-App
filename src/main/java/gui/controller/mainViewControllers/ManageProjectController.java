package gui.controller.mainViewControllers;

import be.*;
import gui.controller.newAndUpdateControllers.*;
import gui.model.*;
import javafx.beans.property.*;
import javafx.event.ActionEvent;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.*;
import javafx.scene.input.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.*;


import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class ManageProjectController extends BaseController implements Initializable {

    @FXML private AnchorPane projectAnchor;
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
    private Label windowTitleLabel;

    // Models
    private ProjectModel projectModel = ProjectModel.getInstance();
    private AuthenticationModel authenticationModel = AuthenticationModel.getInstance();
    private UserModel userModel = UserModel.getInstance();



    /**
     * We use this to set our username label and window title label.
     */
    private void setUsernameLabel() {// set our username label to the users name and our window title label.
        windowTitleLabel.setText("Task Document Manager");
        usernameLabel.setText(authenticationModel.getLoggedInUser().getFirstName() + " " + authenticationModel.getLoggedInUser().getLastName());
    }

    /**
     * We use this to set up the tableview ProjectTV with relative columns and add a listener for selected items.
     */
    private void setProjectTV() {
        projectTV.setItems(projectModel.getProjects());
        projectModel.loadProjects();

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
        assignedUserCount.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getTotalTasks()).asObject());


    }

    /**
     * We use this to set up the tableview TechTV with relative columns and add a listener for selected items.
     */
    private void setTechTV() {
        techTV.setItems(userModel.getTechs());
        userModel.loadTechs();

        techName.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getUser().getFirstName()));
        techSurname.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getUser().getLastName()));
        numberOfTasks.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getAssignedTasks()).asObject());


    }

    /**
     * Assigns a project to the selected user.
     *
     * @param actionEvent triggered when the user activates the assign project button.
     */
    @FXML
    private void assignProject(ActionEvent actionEvent) {
        if (projectTV.getSelectionModel().getSelectedItem().getTotalTasks() > 0) {
            if (projectTV.getSelectionModel().getSelectedItem() != null && techTV.getSelectionModel().getSelectedItem() != null) {
                projectModel.assignProject(userModel.getSelectedUser(), projectModel.getSelectedProject());
                userModel.loadTechs();
            } }
        else {
                String str = "Only projects with tasks can be assigned to a technician";
                super.createWarning(str);
            }
        }


    /**
     * Deletes the selected project.
     *
     * @param actionEvent triggered when the user activates the delete project button.
     */
    @FXML
    private void deleteProject(ActionEvent actionEvent) {
        if (projectTV.getSelectionModel().getSelectedItem() != null) {
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
            projectModel.loadProjects();
        }
    }

    /**
     * Opens the edit window for the selected project.
     *
     * @param actionEvent triggers when the user activates the edit project button.
     */
    @FXML
    private void editProject(ActionEvent actionEvent) {
        if(projectTV.getSelectionModel().getSelectedItem() != null){
        editProj();
        }
    }

    private void editProj(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/view/newAndUpdateViews/NEProject.fxml"));
            Parent root = loader.load();
            NEProjectController controller = loader.getController();
            controller.setNEProjectController(true);
            Stage stage = new Stage();
            Scene scene = new Scene(root);
            stage.setTitle("Edit Project");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            String str = "There has been an error loading NEProject.fxml. Please contact system Admin.";
            super.createWarning(str);
        }
    }

    /**
     * Opens the new window for the selected project.
     *
     * @param actionEvent triggers when the user activates the new project button.
     */
    @FXML
    private void createProject(ActionEvent actionEvent) {
        newProject();
    }

    private void newProject(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/view/newAndUpdateViews/NEProject.fxml"));
            Parent root = loader.load();
            NEProjectController controller = loader.getController();
            controller.setNEProjectController(false);
            Stage stage = new Stage();
            Scene scene = new Scene(root);
            stage.setTitle("Create Project");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            String str = "There has been an error loading NEProject.fxml. Please contact system Admin.";
            super.createWarning(str);
        }
    }

    /**
     * This will log the user out and change the view to the login.
     * We catch the IOException and show the user a crafted alert.
     *
     * @param actionEvent triggered by the logout button.
     */
    @FXML
    private void logOut(ActionEvent actionEvent) {
       super.logout(actionEvent);
    }


    /**
     * This will open the New Task View.
     * We catch the IOException and show the user a crafted alert.
     *
     * @param actionEvent triggered by the create task button.
     */

    @FXML
    private void createTask(ActionEvent actionEvent) {
        addTask();
    }

    private void addTask(){
        if (projectTV.getSelectionModel().getSelectedItem() != null){
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/view/newAndUpdateViews/NewTask.fxml"));
                Parent root = loader.load();
                Stage stage = new Stage();
                Scene scene = new Scene(root);
                stage.setTitle("Create Task");
                stage.setScene(scene);
                stage.show();
            } catch (IOException e) {
                String str = "There has been a problem loading NewTask.fxml, please contact system Admin.";
                super.createWarning(str);
            }
        }
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setUsernameLabel();
        setProjectTV();
        setTechTV();
    }


    @FXML private void projectTVOnClick(MouseEvent mouseEvent) {
        if(projectTV.getSelectionModel().getSelectedItem() != null){
        projectModel.setSelectedProject(projectTV.getSelectionModel().getSelectedItem().getProject());
        if(mouseEvent.getClickCount() == 2){
            addTask();
        }
        }

        if(projectTV.getSelectionModel().getSelectedItem() == null){
            if(mouseEvent.getClickCount() == 2){
            newProject();
            }
        }
    }

    @FXML private void techTvOnClick(MouseEvent mouseEvent) {
        if(techTV.getSelectionModel().getSelectedItem() != null) {
            userModel.setSelectedUser(techTV.getSelectionModel().getSelectedItem().getUser());
        }
    }

    @FXML private void anchorOnClick(MouseEvent mouseEvent) {
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
