package gui.controller;

import be.*;
import gui.model.*;
import javafx.beans.property.*;
import javafx.fxml.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.*;

import java.awt.event.*;

public class ManageUsersController {
    @FXML private TableView<User> userTV;
    @FXML private TableColumn<User, String> userName;
    @FXML private TableColumn<User, String> firstName;
    @FXML private TableColumn<User, String> surname;
    @FXML private TableColumn<User, String> userRole;

    @FXML private Label usernameLabel;
    @FXML private Label windowTitleLabel;

    private Persistent persistenceModel;
    private Observables observablesModel;

    private void setUsernameLabel() {// set our username label to the users name and our window title label.
        windowTitleLabel.setText("User Management");
        usernameLabel.setText(persistenceModel.getLoggedInUser().getFirstName() + " " + persistenceModel.getLoggedInUser().getLastName());
    }

    private void setUserTV() {
        userTV.setItems(observablesModel.getUsers());
        observablesModel.loadUsers();

        userName.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getUserName()));
        firstName.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getFirstName()));
        surname.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getLastName()));
        userRole.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAccess()));
    }

    @FXML private void newUser(ActionEvent actionEvent){}

    @FXML private void deleteUser(ActionEvent actionEvent){}

    @FXML private void editUser(ActionEvent actionEvent) {}

    @FXML private void logOut(ActionEvent actionEvent){}
}
