package com.WUAV.gui.controller;

import com.WUAV.be.*;
import com.WUAV.gui.model.*;
import javafx.beans.property.*;
import javafx.event.*;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.stage.*;

import java.io.*;
import java.net.*;
import java.util.*;

public class ManageTaskController extends NavigationController implements Initializable{


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
    private Persistent persistenceModel = Persistent.getInstance();
    private Observables observablesModel = Observables.getInstance();

    // Userwrapper
    private UserWrapper userWrapper;


    /**
     * We use this to set our username label and window title label.
     */
    private void setUsernameLabel() {// set our username label to the users name.
        usernameLabel.setText(persistenceModel.getLoggedInUser().getFirstName() + " " + persistenceModel.getLoggedInUser().getLastName());
    }




    @FXML private void logOut(ActionEvent actionEvent){
        try {
            persistenceModel.setLoggedInUser(null);
            Parent root = FXMLLoader.load(getClass().getResource("/com/WUAV/gui/view/Login.fxml"));
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setTitle("WUAV");
            stage.setScene(scene);
            stage.show();
        }catch (IOException e){
            String str = "Login.fxml";
            taskError(str);
        }
    }

    /**
     * If loading any of our views has a problem, we show the user an alert along with the view name.
     * @param str This is the name of the view that is causing the problem.
     */
    private void taskError(String str) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Error loading view");
        alert.setHeaderText("There has been an error loading " + str +". Please contact system admin.");

        Optional<ButtonType> result = alert.showAndWait();
        if(result.isPresent() && result.get() == ButtonType.OK){
            alert.close();
        } else {
            alert.close();
        }
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setUsernameLabel();

        if(persistenceModel.getLoggedInUser().getAccess().toUpperCase().equals("TECHNICIAN"))
        {
            User loggedIn = persistenceModel.getLoggedInUser();
            taskTV.setItems(observablesModel.getTasksByUserID());
            observablesModel.loadProjectsByUser(loggedIn);
            customerName.setVisible(false);
            projectName.setMinWidth(projectName.getWidth() + 90);
            taskName.setMinWidth(taskName.getWidth() + 90);
            stateOfTask.setMinWidth(stateOfTask.getWidth() + 90);

            System.out.println(observablesModel.getTasksByUserID().size());
        }else {
            taskTV.setItems(observablesModel.getTasksInfo());
            observablesModel.loadTasksInfo();
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
            persistenceModel.setSelectedProject(taskTV.getSelectionModel().getSelectedItem().getProject());
            persistenceModel.setSelectedTask(taskTV.getSelectionModel().getSelectedItem().getTask());
            if(mouseEvent.getClickCount() == 2){
                openProjectInformation();
            }
        }

    }

    private void openProjectInformation(){
        try {
            persistenceModel.setSelectedProject(taskTV.getSelectionModel().getSelectedItem().getProject());
            Node n = FXMLLoader.load(getClass().getResource("/com/WUAV/gui/view/DocumentationView.fxml"));
            persistenceModel.getViewAnchor().getChildren().setAll(n);
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
