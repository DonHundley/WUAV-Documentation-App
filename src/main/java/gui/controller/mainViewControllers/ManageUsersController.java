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
import javafx.stage.*;
import org.apache.logging.log4j.*;

import java.io.*;
import java.net.URL;
import java.util.*;

public class ManageUsersController extends BaseController implements Initializable{

    // TableView
    @FXML private TableView<User> userTV;
    @FXML private TableColumn<User, String> userName;
    @FXML private TableColumn<User, String> firstName;
    @FXML private TableColumn<User, String> surname;
    @FXML private TableColumn<User, String> userRole;

    // Labels
    @FXML private Label usernameLabel;
    @FXML private Label errorLabel;

    // Models
    private final UserModel userModel = UserModel.getInstance();
    private final AuthenticationModel authenticationModel = AuthenticationModel.getInstance();

    private static final Logger logger = LogManager.getLogger("debugLogger");

    /**
     * We use this to set our username label and window title label.
     */
    private void setUsernameLabel() {// set our username label to the users name.
        logger.trace("setting username label in ManageUsersController");
        usernameLabel.setText(authenticationModel.getLoggedInUser().getFirstName() + " " + authenticationModel.getLoggedInUser().getLastName());
    }

    /**
     * This model sets the items for our tableview using the users list from Observables.
     * We also add a listener to the tableview to change the currently selected user stored in Persistent.
     */
    private void setUserTV() {
        logger.info("setUserTV called in ManageUsersController");
        userTV.setItems(userModel.getUsers());
        userModel.loadUsers();

        logger.trace("Setting column values for userTV");
        userName.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getUserName()));
        firstName.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getFirstName()));
        surname.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getLastName()));
        userRole.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAccess()));

        logger.trace("adding listener to userTV");
        userTV.getSelectionModel().selectedItemProperty().addListener((((observable, oldValue, selectedUser) -> userModel.setSelectedUser(selectedUser))));
        logger.info("setUserTV() complete.");
    }


    /**
     * This method is an alert confirmation to delete the selected user stored in Persistent.
     */
    @FXML private void deleteUser(){
        logger.info("deleteUser() called in ManageUsersController");
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
            logger.warn("user did not select a user to be deleted and was informed.");
            errorLabel.setText("No selected user found. Please select a user to delete.");
        }
        logger.info("deleteUser() complete.");
    }

    /**
     * We use this method to open our new user view to the user. If this fails we show the user an alert.
     */
    @FXML private void newUser(){
        newOrEditUser(false);
    }
    /**
     * We use this method to open our edit user view to the user. If this fails we show the user an alert.
     */
    @FXML private void editUser() {
        if(userTV.getSelectionModel().getSelectedItem() != null) {
            newOrEditUser(true);
        }
    }

    private void newOrEditUser(boolean isEdit){
        logger.info("newOrEditUser() called in ManageUsersController");
        try {
            logger.info("Loading NEUser.fxml");
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/view/newAndUpdateViews/NEUser.fxml"));
            Parent root = loader.load();
            NEUserController controller = loader.getController();
            controller.setNEUserController(isEdit);
            Stage stage = new Stage();
            Scene scene = new Scene(root);

            if(!isEdit) {
                stage.setTitle("New user.");
            } else {
                stage.setTitle("Edit User");
            }

            stage.setScene(scene);
            stage.show();
        }catch (IOException e){
            logger.error("There was a problem loading NEUser.fxml.",e);
            String str = "There has been a problem loading NEUser.fxml, please contact system Admin.";
            super.createWarning(str);
        }
        logger.info("newOrEditUser() complete.");
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
        logger.trace("Initializing ManageUsersController");
        setUserTV();
        setUsernameLabel();
    }

    @FXML private void usersAnchorOnClick() {
        logger.trace("User clicked anchor pane in ManageUsersController");
        if(userTV.getSelectionModel().getSelectedItem() != null){
            userTV.getSelectionModel().clearSelection();
        }
        userTV.refresh();
    }

    @FXML private void tvOnClick(MouseEvent mouseEvent) {
        logger.trace("User clicked the tableview in ManageUsersController.");
        if(userTV.getSelectionModel().getSelectedItem() != null){
            if(mouseEvent.getClickCount() == 2){
               newOrEditUser(true);
            }
        }
        if(userTV.getSelectionModel().getSelectedItem() == null){
            if(mouseEvent.getClickCount() == 2){
                newOrEditUser(false);
            }
        }
    }
}
