package gui.controller;

import be.*;
import gui.model.*;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.*;
import javafx.stage.*;

import java.awt.event.*;
import java.io.*;
import java.util.*;

public class ManagerTaskView {

    @FXML private TableView<Task> taskTV;
    @FXML private TableColumn<Task, String> projectName;
    @FXML private TableColumn<Task, String> taskName;
    @FXML private TableColumn<Task, String> stateOfTask;
    @FXML private TableColumn<Task, String> customerName;

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

        setUsernameLabel();
        setTaskTV();
    }

    /**
     * We use this to set our username label and window title label.
     */
    private void setUsernameLabel() {// set our username label to the users name and our window title label.
        windowTitleLabel.setText("Task Overview");
        usernameLabel.setText(persistenceModel.getLoggedInUser().getFirstName() + " " + persistenceModel.getLoggedInUser().getLastName());
    }

    private void setTaskTV() {
        taskTV.setItems(observablesModel.getAllTasks());
        observablesModel.loadAllTasks();

        //projectName.setCellValueFactory(); // needs a combined list
        taskName.setCellValueFactory(new PropertyValueFactory<>("task_name"));
        stateOfTask.setCellValueFactory(new PropertyValueFactory<>("task_state"));
        //customerName.setCellValueFactory(); // needs a combined list
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
