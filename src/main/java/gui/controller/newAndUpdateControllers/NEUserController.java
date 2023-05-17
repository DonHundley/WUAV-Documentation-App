package gui.controller.newAndUpdateControllers;

import be.*;
import gui.controller.mainViewControllers.*;
import gui.model.*;
import javafx.event.ActionEvent;
import javafx.fxml.*;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.*;


import java.util.*;

public class NEUserController extends BaseController {

    // FXML
    @FXML
    private TextField userNameTF;
    @FXML
    private ComboBox<String> accessCB;
    @FXML
    private TextField passTF;
    @FXML
    private TextField firstNameTF;
    @FXML
    private TextField surnameTF;
    @FXML
    private Label windowTitleLabel;
    @FXML
    private Button cancelButton;
    @FXML
    private Button createOrEditUser;

    // selections for the accessCB
    private String[] accessLevels = {"Admin", "Manager", "Sales", "Technician"};

    // if editing this is will be the selected user from persistence, otherwise it is the new user to be created.
    private User user;

    // Model instances
    private UserModel userModel = UserModel.getInstance();


    // True if editing, false if creating a new customer.
    private boolean isEdit;


    /**
     * This method is used to set our models and choose if we are editing or creating a user.
     *
     * @param isEdit          if true, we are editing a user.
     */
    public void setNEUserController(Boolean isEdit) {

        setEdit(isEdit); // sets the boolean to store if we are editing or creating.
        if (isEdit) { // if we are editing we set all text fields with the current information.
            setOnEdit();
            createOrEditUser.setText("Edit");
        } else {
            windowTitleLabel.setText("New User");
            createOrEditUser.setText("Create");
        }
        setAccessSelection();
    }

    /**
     * This will create or edit a User based on the isEdit boolean
     *
     * @param actionEvent triggered when the user activates the create/edit button.
     */
    @FXML
    private void createOrEditUser(ActionEvent actionEvent) {
        if (isEdit) {
            editUser();
        } else {
            createUser();
        }
    }

    /**
     * This method is used to create a new User if our isEdit boolean == false.
     */
    private void createUser() {
        if (!userNameTF.getText().isEmpty() && !passTF.getText().isEmpty() && !accessCB.getValue().isEmpty()&& !firstNameTF.getText().isEmpty() && !surnameTF.getText().isEmpty()) {
            user = new User(userNameTF.getText(), passTF.getText(), accessCB.getValue(), firstNameTF.getText(), surnameTF.getText());
            userModel.createUser(user);
            userModel.loadUsers();
            Stage stage = (Stage) createOrEditUser.getScene().getWindow();
            stage.close();
        }
        else{
            String str="Please fill in all the fields to create a new user";
            super.createWarning(str);
        }
    }

    /**
     * This method is used to edit the selected User from our persistent model with updated information.
     */
    private void editUser() {
        if (!userNameTF.getText().isEmpty() && !passTF.getText().isEmpty() && !accessCB.getValue().isEmpty()&& !firstNameTF.getText().isEmpty() && !surnameTF.getText().isEmpty()) {
        user.setUserName(userNameTF.getText());
        user.setPassword(passTF.getText());
        user.setAccess(accessCB.getValue());
        user.setFirstName(firstNameTF.getText());
        user.setLastName(surnameTF.getText());
        userModel.editUser(user);
        userModel.loadUsers();
        Stage stage = (Stage) createOrEditUser.getScene().getWindow();
        stage.close();
    } else{
            String str="Please fill in all the fields to edit a user";
            super.createWarning(str);
        }
    }

    /**
     * Closes the window with an action event.
     *
     * @param actionEvent triggers when the user activates the cancel button.
     */
    @FXML
    private void cancel(ActionEvent actionEvent) {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    /**
     * If the boolean isEdit == true, we set all the fields with the information from the selected customer.
     */
    private void setOnEdit() {
        if (userModel.getSelectedUser() != null) {
            windowTitleLabel.setText("Edit User");
            user = userModel.getSelectedUser();
            userNameTF.setText(user.getUserName());
            passTF.setText(user.getPassword());
            accessCB.setValue(user.getAccess());
            firstNameTF.setText(user.getFirstName());
            surnameTF.setText(user.getLastName());

        } else {
            String str = "Could not find a user to be edited. Please contact system admin. Class: NEUserController.";
            super.createWarning(str);
        }
    }

    /**
     * We add all of our access levels into our access selection combobox and then set the selected value to the current value stored in our selected user if editing.
     */
    private void setAccessSelection() {
        for (String level : accessLevels) {
            accessCB.getItems().add(level);
        }
        if (isEdit) {
            accessCB.setValue(user.getAccess());
        }
        accessCB.setOnAction(this::changeAccess);
    }

    /**
     * Changes the access level of the user.
     *
     * @param actionEvent This triggers when the user selects an option in the access level combobox.
     */
    private void changeAccess(javafx.event.ActionEvent actionEvent) {
        if (isEdit) {
            user.setAccess(accessCB.getValue());
        }
    }



    public void setEdit(boolean edit) {
        isEdit = edit;
    }
}
