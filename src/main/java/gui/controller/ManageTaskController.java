package gui.controller;

import be.*;
import gui.model.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.stage.*;

import java.awt.event.*;
import java.io.*;
import java.util.*;

public class ManageTaskController {

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
    private Persistent persistenceModel;
    private Observables observablesModel;


    /**
     * We call this when this controller is called from navigation to set our models, tableview, and labels.
     * @param persistenceModel this is our instance of Persistent from navigation
     * @param observablesModel this is our instance of Observables from navigation
     */
    public void userController(Persistent persistenceModel, Observables observablesModel){
        this.persistenceModel = persistenceModel;
        this.observablesModel = observablesModel;

        //setUsernameLabel();
        setTaskTV();
    }

    /**
     * We use this to set our username label and window title label.
     */
    private void setUsernameLabel() {// set our username label to the users name and our window title label.
        //windowTitleLabel.setText("Task Overview");
        usernameLabel.setText(persistenceModel.getLoggedInUser().getFirstName() + " " + persistenceModel.getLoggedInUser().getLastName());
    }


    /**
     * We use this to set up the tableview TaskTV with relative columns.
     */
    private void setTaskTV() {
        taskTV.setItems(observablesModel.getTasksInfo());
        observablesModel.loadTasksInfo();

        projectName.setCellValueFactory(cellData-> new SimpleStringProperty(cellData.getValue().getProject().getProjName()));
        taskName.setCellValueFactory(cellData-> new SimpleStringProperty(cellData.getValue().getTask().getTaskName()));
        stateOfTask.setCellValueFactory(cellData-> new SimpleStringProperty(cellData.getValue().getTask().getTaskState()));
        customerName.setCellValueFactory(cellData-> new SimpleStringProperty(cellData.getValue().getCustomer().getCustName()));
    }

    @FXML private void logOut(ActionEvent actionEvent){
        try {
            persistenceModel.setLoggedInUser(null);
            Parent root = FXMLLoader.load(getClass().getResource("/gui/view/Login.fxml"));
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
}