package gui.controller.mainViewControllers;

import be.*;
import gui.model.*;
import javafx.beans.property.*;
import javafx.event.ActionEvent;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.*;


import java.io.*;
import java.net.URL;
import java.util.*;

public class ManageTaskController extends BaseController implements Initializable{


    @FXML private AnchorPane taskAnchor;
    @FXML private TableView<TaskWrapper> taskTV;
    @FXML private TableColumn<TaskWrapper, String> projectName;
    @FXML private TableColumn<TaskWrapper, String> taskName;
    @FXML private TableColumn<TaskWrapper, String> stateOfTask;
    @FXML private TableColumn<TaskWrapper, String> customerName;

    // Labels
    @FXML private Label usernameLabel;
    @FXML private Label windowTitleLabel;
    @FXML private Label errorLabel;

    // Models
    private ProjectModel projectModel = ProjectModel.getInstance();
    private AuthenticationModel authenticationModel = AuthenticationModel.getInstance();
    private CustomerModel customerModel = CustomerModel.getInstance();




    /**
     * We use this to set our username label and window title label.
     */
    private void setUsernameLabel() {// set our username label to the users name.
        usernameLabel.setText(authenticationModel.getLoggedInUser().getFirstName() + " " + authenticationModel.getLoggedInUser().getLastName());
    }


    @FXML private void logOut(ActionEvent actionEvent){
        super.logout(actionEvent);
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setUsernameLabel();

        if(authenticationModel.getLoggedInUser().getAccess().toUpperCase().equals("TECHNICIAN"))
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
            projectModel.loadTasksInfo();
            customerName.setCellValueFactory(cellData-> new SimpleStringProperty(cellData.getValue().getCustomer().getCustName()));
        }
        projectName.setCellValueFactory(cellData-> new SimpleStringProperty(cellData.getValue().getProject().getProjName()));
        taskName.setCellValueFactory(cellData-> new SimpleStringProperty(cellData.getValue().getTask().getTaskName()));
        stateOfTask.setCellValueFactory(cellData-> new SimpleStringProperty(cellData.getValue().getTask().getTaskState()));

    }


    @FXML private void openProjectInfo(ActionEvent actionEvent)  {
        openProjectInformation();
    }

    @FXML private void taskTVOnClick(MouseEvent mouseEvent) {
        if(taskTV.getSelectionModel().getSelectedItem() != null){
            projectModel.setSelectedProject(taskTV.getSelectionModel().getSelectedItem().getProject());
            projectModel.setSelectedTask(taskTV.getSelectionModel().getSelectedItem().getTask());
            customerModel.setSelectedCustomer(taskTV.getSelectionModel().getSelectedItem().getCustomer());
            if(mouseEvent.getClickCount() == 2){
                openProjectInformation();
            }
        }

    }

    private void openProjectInformation(){
        try {
            projectModel.setSelectedProject(taskTV.getSelectionModel().getSelectedItem().getProject());
            Node n = FXMLLoader.load(getClass().getResource("/gui/view/mainViews/DocumentationView.fxml"));
            super.nav.getViewAnchor().getChildren().setAll(n);
        }  catch (IOException e) {
            errorLabel.setText("Please select an task to see related documentation.");
        }
    }

    @FXML private void taskAnchorOnClick(MouseEvent mouseEvent) {
        if(taskTV.getSelectionModel().getSelectedItem() != null){
            taskTV.getSelectionModel().clearSelection();
        }
        taskTV.refresh();
    }
}
