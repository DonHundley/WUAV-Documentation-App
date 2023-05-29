package gui.controller.newAndUpdateControllers;

import be.*;
import gui.controller.mainViewControllers.*;
import gui.model.*;
import javafx.fxml.*;
import javafx.scene.control.*;
import javafx.stage.*;
import org.apache.logging.log4j.*;



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
    private final String[] accessLevels = {"Admin", "Manager", "Sales", "Technician"};

    // if editing this is will be the selected user from persistence, otherwise it is the new user to be created.
    private User user;

    // Model instances
    private final UserModel userModel = UserModel.getInstance();


    // True if editing, false if creating a new customer.
    private boolean isEdit;

    //fields to validate
    int maxUsernameLenght = 10;
    int maxPasswordLenght = 50;
    int maxNameLenght = 25;
    int maxLastNameLenght = 25;

    private static final Logger logger = LogManager.getLogger("debugLogger");


    /**
     * This method is used to set our models and choose if we are editing or creating a user.
     *
     * @param isEdit if true, we are editing a user.
     */
    public void setNEUserController(Boolean isEdit) {
        logger.trace("setNEUserController() called.");
        setEdit(isEdit); // sets the boolean to store if we are editing or creating.
        if (isEdit) { // if we are editing we set all text fields with the current information.
            setOnEdit();
            createOrEditUser.setText("Edit");
            logger.trace("Editing user");
        } else {
            windowTitleLabel.setText("New User");
            createOrEditUser.setText("Create");
            logger.trace("Creating new user.");
        }
        setAccessSelection();
    }

    /**
     * This will create or edit a User based on the isEdit boolean
     *
     */
    @FXML
    private void createOrEditUser() {
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
        logger.info("Creating a new User");
        if (validateUserFields()) {
            user = new User(userNameTF.getText(), passTF.getText(), accessCB.getValue(), firstNameTF.getText(), surnameTF.getText());
            userModel.createUser(user);
            userModel.loadUsers();
            Stage stage = (Stage) createOrEditUser.getScene().getWindow();
            stage.close();
            logger.info("User created successfully");
        } else {
            logger.warn("User creation failed due to invalid fields");
            warningLoggerForUser();
        }
    }

    /**
     * This method is used to edit the selected User from our persistent model with updated information.
     */
    private void editUser() {
        logger.info("Editing of a User");
        if (validateUserFields()) {
            user.setUserName(userNameTF.getText());
            user.setPassword(passTF.getText());
            user.setAccess(accessCB.getValue());
            user.setFirstName(firstNameTF.getText());
            user.setLastName(surnameTF.getText());
            userModel.editUser(user);
            userModel.loadUsers();
            Stage stage = (Stage) createOrEditUser.getScene().getWindow();
            stage.close();
            logger.info("User edited successfully");
        } else {
            logger.warn("User creation failed due to invalid fields");
            warningLoggerForUser();
        }
    }

    /**
     * Closes the window with an action event.
     *
     */
    @FXML
    private void cancel() {
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
        logger.trace("setting access combobox.");
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
        logger.trace("Changing access level of user.");
        if (isEdit) {
            user.setAccess(accessCB.getValue());
        }
    }



    public void setEdit(boolean edit) {
        isEdit = edit;
    }


    /**
     * This method checks if the length of the username textfield is bigger than the max length for the field
     * it returns true if the length is accepted
     **/
    private boolean isUsernameTFValid() {
        return userNameTF.getText().length() <= maxUsernameLenght;
    }

    /**
     * This method checks if the length of the password textfield is bigger than the max length for the field
     * it returns true if the length is accepted
     **/
    private boolean isPasswordTFValid() {
        return passTF.getText().length() <= maxPasswordLenght;
    }

    /**
     * This method checks if the length of the first name textfield is bigger than the max length for the field
     * it returns true if the length is accepted
     **/
    private boolean isFirstNameTFValid() {
        return firstNameTF.getText().length() <= maxNameLenght;
    }

    /**
     * This method checks if the length of the lasname textfield is bigger than the max length for the field
     * it returns true if the length is accepted
     **/
    private boolean isLastNameTFValid() {
        return surnameTF.getText().length() <= maxLastNameLenght;
    }

    /**
     * this method shows an alert to the user if the inserted username text field length exceeds the max
     **/
    private void alertUsernameTF() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Validate Username");
        alert.setContentText("Username is too long, max is " + maxUsernameLenght + " characters.");
        alert.showAndWait();
    }


    /**
     * this method shows an alert to the user if the inserted password text field length exceeds the max
     **/
    private void alertPasswordTF() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Validate Password");
        alert.setContentText("Password is too long, max is " + maxPasswordLenght + " characters.");
        alert.showAndWait();
    }


    /**
     * this method shows an alert to the user if the inserted first name text field length exceeds the max
     **/
    private void alertFirstNameTF() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Validate First Name");
        alert.setContentText("First Name is too long, max is " + maxNameLenght + " characters.");
        alert.showAndWait();
    }


    /**
     * this method shows an alert to the user if the inserted last name text field length exceeds the max
     **/
    private void alertLastNameTF() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Validate Last Name");
        alert.setContentText("Last Name is too long, max is " + maxLastNameLenght + " characters.");
        alert.showAndWait();
    }

    /**
     * This method shows an alert to the user if the inserted username and password text field lengths exceed the max
     */
    private void alertUsernameAndPasswordTF() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Validate Username and Password");
        alert.setContentText("Username and password are too long, max is " + maxUsernameLenght + " and " + maxPasswordLenght + " characters respectively.");
        alert.showAndWait();
    }


    /**
     * This method shows an alert to the user if the inserted username and first name lengths exceed the max
     */
    private void alertUsernameAndFirstNameTF() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Validate Username and First Name");
        alert.setContentText("Username and first name are too long, max is " + maxUsernameLenght + " and " + maxNameLenght + " characters respectively.");
        alert.showAndWait();
    }


    /**
     * This method shows an alert to the user if the inserted username and last name lengths exceed the max
     */
    private void alertUsernameAndLastNameTF() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Validate Username and Last Name");
        alert.setContentText("Username and last name are too long, max is " + maxUsernameLenght + " and " + maxLastNameLenght + " characters respectively.");
        alert.showAndWait();
    }

    /**
     * This method shows an alert to the user if the inserted password and first name lengths exceed the max
     */
    private void alertPasswordAndFirstNameTF() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Validate Password and First Name");
        alert.setContentText("Password and first name are too long, max is " + maxPasswordLenght + " and " + maxNameLenght + " characters respectively.");
        alert.showAndWait();
    }

    /**
     * This method shows an alert to the user if the inserted password and last name lengths exceed the max
     */
    private void alertPasswordAndLastNameTF() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Validate Password and Last Name");
        alert.setContentText("Password and last name are too long, max is " + maxPasswordLenght + " and " + maxLastNameLenght + " characters respectively.");
        alert.showAndWait();
    }

    /**
     * This method shows an alert to the user if the inserted first name and last name lengths exceed the max
     */
    private void alertFirstNameAndLastNameTF() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Validate First Name and Last Name");
        alert.setContentText("First name and last name are too long, max is " + maxNameLenght + " characters.");
        alert.showAndWait();
    }

    /*Combination of 3 fields validation*/

    /**
     * This method shows an alert to the user if the inserted username, password, and first name lengths exceed the max
     */
    private void alertUsernamePasswordAndFirstNameTF() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Validate Username, Password, and First Name");
        alert.setContentText("Username, password, and first name are too long, max is " + maxUsernameLenght + ", " + maxPasswordLenght + ",  and " + maxNameLenght + " characters respectively.");
        alert.showAndWait();
    }

    /**
     * This method shows an alert to the user if the inserted username, password, and last name lengths exceed the max
     */
    private void alertUsernamePasswordAndLastNameTF() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Validate Username, Password, and Last Name");
        alert.setContentText("Username, password, and last name are too long, max is " + maxUsernameLenght + ", " + maxPasswordLenght + ",  and " + maxLastNameLenght + " characters respectively.");
        alert.showAndWait();
    }

    /**
     * This method shows an alert to the user if the inserted username, first name, and last name lengths exceed the max
     */
    private void alertUsernameFirstNameAndLastNameTF() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Validate Username, First Name, and Last Name");
        alert.setContentText("Username, first name, and last name are too long, max is " + maxUsernameLenght + " and " + maxNameLenght + " characters respectively.");
        alert.showAndWait();
    }

    /**
     * This method shows an alert to the user if the inserted password, first name, and last name lengths exceed the max
     */
    private void alertPasswordFirstNameAndLastNameTF() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Validate Password, First Name, and Last Name");
        alert.setContentText("Password, first name, and last name are too long, max is " + maxPasswordLenght + " and " + maxNameLenght + " characters respectively.");
        alert.showAndWait();
    }

    /**
     * This method shows an alert to the user if the inserted username, password, first name, and last name lengths exceed the max
     */
    private void alertUsernamePasswordFirstNameAndLastNameTF() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Validate Username, Password, First Name, and Last Name");
        alert.setContentText("Username, password, first name, and last name are too long, max is " + maxUsernameLenght + ", " + maxPasswordLenght + ", " + maxNameLenght + ", " + maxLastNameLenght + " characters respectively.");
        alert.showAndWait();
    }

    /**
     * method to check all combination of fields and show the corresponding alerts or error message
     **/
    private boolean validateUserFields() {
        boolean isValid = true;

        if (userNameTF.getText().isEmpty() || passTF.getText().isEmpty() || accessCB.getSelectionModel().getSelectedItem() == null || firstNameTF.getText().isEmpty() || surnameTF.getText().isEmpty()) {
            String str = "Please fill in all the fields";
            super.createWarning(str);
            isValid = false;
        } else if (!isUsernameTFValid() && !isPasswordTFValid() && !isFirstNameTFValid() && !isLastNameTFValid()) {
            alertUsernamePasswordFirstNameAndLastNameTF();
            isValid = false;
        } else if (!isUsernameTFValid() && !isPasswordTFValid() && !isFirstNameTFValid()) {
            alertUsernamePasswordAndFirstNameTF();
            isValid = false;
        } else if (!isUsernameTFValid() && !isPasswordTFValid() && !isLastNameTFValid()) {
            alertUsernamePasswordAndLastNameTF();
            isValid = false;
        } else if (!isUsernameTFValid() && !isFirstNameTFValid() && !isLastNameTFValid()) {
            alertUsernameFirstNameAndLastNameTF();
            isValid = false;
        } else if (!isPasswordTFValid() && !isFirstNameTFValid() && !isLastNameTFValid()) {
            alertPasswordFirstNameAndLastNameTF();
            isValid = false;
        } else if (!isUsernameTFValid() && !isPasswordTFValid()) {
            alertUsernameAndPasswordTF();
            isValid = false;
        } else if (!isUsernameTFValid() && !isFirstNameTFValid()) {
            alertUsernameAndFirstNameTF();
            isValid = false;
        } else if (!isUsernameTFValid() && !isLastNameTFValid()) {
            alertUsernameAndLastNameTF();
            isValid = false;
        } else if (!isPasswordTFValid() && !isFirstNameTFValid()) {
            alertPasswordAndFirstNameTF();
            isValid = false;
        } else if (!isPasswordTFValid() && !isLastNameTFValid()) {
            alertPasswordAndLastNameTF();
            isValid = false;
        } else if (!isFirstNameTFValid() && !isLastNameTFValid()) {
            alertFirstNameAndLastNameTF();
            isValid = false;
        } else if (!isUsernameTFValid()) {
            alertUsernameTF();
            isValid = false;
        } else if (!isPasswordTFValid()) {
            alertPasswordTF();
            isValid = false;
        } else if (!isFirstNameTFValid()) {
            alertFirstNameTF();
            isValid = false;
        } else if (!isLastNameTFValid()) {
            alertLastNameTF();
            isValid = false;
        }

        return isValid;
    }

    /**
     * method to log about invalid fields when creating or editing a user
     **/
    private void warningLoggerForUser() {
        if (!isUsernameTFValid()) {
            logger.warn("Invalid username field: username exceeds the maximum character limit");
        }
        if (!isPasswordTFValid()) {
            logger.warn("Invalid password field: password exceeds the maximum character limit");
        }
        if (!isFirstNameTFValid()) {
            logger.warn("Invalid first name field: first name exceeds the maximum character limit");
        }
        if (!isLastNameTFValid()) {
            logger.warn("Invalid last name field: last name exceeds the maximum character limit");
        }
        if (userNameTF.getText().isEmpty() || passTF.getText().isEmpty() || accessCB.getSelectionModel().getSelectedItem() == null || firstNameTF.getText().isEmpty() || surnameTF.getText().isEmpty()) {
            logger.warn("User creation failed due to one or more empty fields");
        }

    }
}
