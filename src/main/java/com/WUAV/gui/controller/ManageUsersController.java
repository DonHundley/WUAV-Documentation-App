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

public class ManageUsersController implements Initializable{

    @FXML private AnchorPane anchorUsers;
    // TableView
    @FXML private TableView<User> userTV;
    @FXML private TableColumn<User, String> userName;
    @FXML private TableColumn<User, String> firstName;
    @FXML private TableColumn<User, String> surname;
    @FXML private TableColumn<User, String> userRole;

    // Labels
    @FXML private Label usernameLabel;
    @FXML private Label windowTitleLabel;
    @FXML private Label errorLabel;

    // Models
    private Persistent persistenceModel = Persistent.getInstance();
    private Observables observablesModel = Observables.getInstance();
    private Functions functionsModel = new Functions();



    /**
     * We use this to set our username label and window title label.
     */
    private void setUsernameLabel() {// set our username label to the users name.

        usernameLabel.setText(persistenceModel.getLoggedInUser().getFirstName() + " " + persistenceModel.getLoggedInUser().getLastName());
    }

    /**
     * This model sets the items for our tableview using the users list from Observables.
     * We also add a listener to the tableview to change the currently selected user stored in Persistent.
     */
    private void setUserTV() {
        userTV.setItems(observablesModel.getUsers());
        observablesModel.loadUsers();

        userName.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getUserName()));
        firstName.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getFirstName()));
        surname.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getLastName()));
        userRole.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAccess()));

        userTV.getSelectionModel().selectedItemProperty().addListener((((observable, oldValue, selectedUser) -> {
            persistenceModel.setSelectedUser(selectedUser);
        })));
    }


    /**
     * This method is an alert confirmation to delete the selected user stored in Persistent.
     * @param actionEvent This is triggered when the user activates the Delete User button.
     */
    @FXML private void deleteUser(ActionEvent actionEvent){
        if(persistenceModel.getSelectedUser() != null){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirm deletion of user.");
            alert.setHeaderText("Do you really want to DELETE " + persistenceModel.getSelectedUser().getUserName() + "?");

            Optional<ButtonType> result = alert.showAndWait();
            if(result.isPresent() && result.get() == ButtonType.OK){
                functionsModel.deleteUser(persistenceModel.getSelectedUser());
                observablesModel.loadUsers();
            } else {
                alert.close();
            }
        } else {
            errorLabel.setText("No selected user found. Please select a user to delete.");
        }
    }

    /**
     * We use this method to open our new user view to the user. If this fails we show the user an alert.
     * @param actionEvent When the user activates the New User button, we open a window.
     */
    @FXML private void newUser(ActionEvent actionEvent){
        createUser();
    }

    private void createUser(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/WUAV/gui/view/NEUser.fxml"));
            Parent root = loader.load();
            NEUserController controller = loader.getController();
            controller.setNEUserController(false, persistenceModel, functionsModel);
            Stage stage = new Stage();
            Scene scene = new Scene(root);
            stage.setTitle("New user.");
            stage.setScene(scene);
            stage.show();
        }catch (IOException e){
            loadNEError();
        }
    }

    /**
     * We use this method to open our edit user view to the user. If this fails we show the user an alert.
     * @param actionEvent When the user activates the Edit User button, we open a window.
     */
    @FXML private void editUser(ActionEvent actionEvent) {
        updateUser();
    }

    private void updateUser(){
        if(userTV.getSelectionModel().getSelectedItem() != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/WUAV/gui/view/NEUser.fxml"));
                Parent root = loader.load();
                NEUserController controller = loader.getController();
                controller.setNEUserController(true, persistenceModel, functionsModel);
                Stage stage = new Stage();
                Scene scene = new Scene(root);
                stage.setTitle("Edit user.");
                stage.setScene(scene);
                stage.show();
            } catch (IOException e) {
                loadNEError();
            }
        }
    }
    /**
     * This is what we use to show the user an alert if NEUser.fxml fails.
     * Since we use the same window for new and edit user, we give the user the same alert.
     */
    private void loadNEError(){
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Error loading view");
        alert.setHeaderText("There has been an error loading NEUser.fxml, please contact system admin.");

        Optional<ButtonType> result = alert.showAndWait();
        if(result.isPresent() && result.get() == ButtonType.OK){
            alert.close();
        } else {
            alert.close();
        }
    }

    /**
     * This method determines what happens when the user clicks logout. If there is a problem we show an alert.
     * @param actionEvent When the logout button is activated we change the view to Login.fxml and change the logged-in user to null.
     */
    @FXML private void logOut(ActionEvent actionEvent) {
        try {
            persistenceModel.setLoggedInUser(null);
            Parent root = FXMLLoader.load(getClass().getResource("/com/WUAV/gui/view/Login.fxml"));
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setTitle("WUAV");
            stage.setScene(scene);
            stage.show();
        }catch (IOException e){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error loading view");
            alert.setHeaderText("There has been an error loading login.fxml, please contact system admin.");

            Optional<ButtonType> result = alert.showAndWait();
            if(result.isPresent() && result.get() == ButtonType.OK){
                alert.close();
            } else {
                alert.close();
            }
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setUserTV();
        setUsernameLabel();
    }

    @FXML private void usersAnchorOnClick(MouseEvent mouseEvent) {
        if(userTV.getSelectionModel().getSelectedItem() != null){
            userTV.getSelectionModel().clearSelection();
        }
        userTV.refresh();
    }

    @FXML private void tvOnClick(MouseEvent mouseEvent) {
        if(userTV.getSelectionModel().getSelectedItem() != null){
            if(mouseEvent.getClickCount() == 2){
                updateUser();
            }
        }
        if(userTV.getSelectionModel().getSelectedItem() == null){
            if(mouseEvent.getClickCount() == 2){
                createUser();
            }
        }
    }
}
