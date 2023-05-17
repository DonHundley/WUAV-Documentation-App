package gui.controller.mainViewControllers;

import be.*;
import gui.controller.newAndUpdateControllers.*;
import gui.model.*;
import javafx.beans.property.*;
import javafx.event.ActionEvent;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.*;

import java.io.*;
import java.net.URL;
import java.util.*;

public class ManageUsersController extends BaseController implements Initializable{

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
    private UserModel userModel = UserModel.getInstance();
    private AuthenticationModel authenticationModel = AuthenticationModel.getInstance();



    /**
     * We use this to set our username label and window title label.
     */
    private void setUsernameLabel() {// set our username label to the users name.
        usernameLabel.setText(authenticationModel.getLoggedInUser().getFirstName() + " " + authenticationModel.getLoggedInUser().getLastName());
    }

    /**
     * This model sets the items for our tableview using the users list from Observables.
     * We also add a listener to the tableview to change the currently selected user stored in Persistent.
     */
    private void setUserTV() {
        userTV.setItems(userModel.getUsers());
        userModel.loadUsers();

        userName.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getUserName()));
        firstName.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getFirstName()));
        surname.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getLastName()));
        userRole.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAccess()));

        userTV.getSelectionModel().selectedItemProperty().addListener((((observable, oldValue, selectedUser) -> {
            userModel.setSelectedUser(selectedUser);
        })));
    }


    /**
     * This method is an alert confirmation to delete the selected user stored in Persistent.
     * @param actionEvent This is triggered when the user activates the Delete User button.
     */
    @FXML private void deleteUser(ActionEvent actionEvent){
        if(userModel.getSelectedUser() != null){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirm deletion of user.");
            alert.setHeaderText("Do you really want to DELETE " + userModel.getSelectedUser().getUserName() + "?");

            Optional<ButtonType> result = alert.showAndWait();
            if(result.isPresent() && result.get() == ButtonType.OK){
                userModel.deleteUser();
                userModel.loadUsers();
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/view/newAndUpdateViews/NEUser.fxml"));
            Parent root = loader.load();
            NEUserController controller = loader.getController();
            controller.setNEUserController(false);
            Stage stage = new Stage();
            Scene scene = new Scene(root);
            stage.setTitle("New user.");
            stage.setScene(scene);
            stage.show();
        }catch (IOException e){
            String str = "There has been a problem loading NEUser.fxml, please contact system Admin.";
            super.createWarning(str);
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
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/view/newAndUpdateViews/NEUser.fxml"));
                Parent root = loader.load();
                NEUserController controller = loader.getController();
                controller.setNEUserController(true);
                Stage stage = new Stage();
                Scene scene = new Scene(root);
                stage.setTitle("Edit user.");
                stage.setScene(scene);
                stage.show();
            } catch (IOException e) {
                String str = "there has been a problem loading NEUser.fxml, please contact system Admin.";
                super.createWarning(str);
            }
        }
    }


    /**
     * This method determines what happens when the user clicks logout. If there is a problem we show an alert.
     * @param actionEvent When the logout button is activated we change the view to Login.fxml and change the logged-in user to null.
     */
    @FXML private void logOut(ActionEvent actionEvent) {
        super.logout(actionEvent);
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
