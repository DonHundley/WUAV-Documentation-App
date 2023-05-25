package gui.controller.newAndUpdateControllers;

import be.*;
import gui.controller.mainViewControllers.*;
import gui.model.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.*;
import javafx.scene.control.*;
import javafx.stage.*;
import org.apache.logging.log4j.*;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NECustomerController extends BaseController {
    // FXML
    @FXML
    private TextField customerName;
    @FXML
    private TextField customerEmail;
    @FXML
    private TextField customerAddress;
    @FXML
    private Label windowTitleLabel;
    @FXML
    private Button cancelButton;
    @FXML
    private Button createOrEditCustomer;
    @FXML
    private TableView<PostalCode> postalCodeTV;
    @FXML
    private TableColumn<PostalCode, String> postalCode, city;

    // Customer instance
    private Customer customer;

    // Model instances
    private final CustomerModel customerModel = CustomerModel.getInstance();


    // True if editing, false if creating a new customer.
    private boolean isEdit;

    //validation fields length
    int maxCustNameLength = 40;
    int maxCustEmailLength = 25;
    int maxCustAddressLength = 50;


    private static final Logger logger = LogManager.getLogger("debugLogger");

    /**
     * This method is used to set our models and choose if we are editing or creating a customer.
     *
     * @param isEdit          if true, we are editing a customer.
     */
    public void setNEController(Boolean isEdit) {
        logger.trace("setNEController() called.");
        setEdit(isEdit); // sets the boolean to store if we are editing or creating.
        if (isEdit) { // if we are editing we set all text fields with the current information.
            setUpPostalCodeTV();
            setOnEdit();
            logger.trace("Customer is being edited");
        } else {
            setUpPostalCodeTV();
            windowTitleLabel.setText("New Customer");
            createOrEditCustomer.setText("Create");
            logger.trace("Customer is being created.");
        }
        logger.trace("setNEController() complete.");
    }

    /**
     * This will create or edit a customer based on the isEdit boolean
     *
     */
    @FXML
    private void createOrEditCustomer() {
        if (isEdit) {
            editCustomer();
        } else {
            createCustomer();
        }
    }

    /**
     * This method is used to create a new customer if our isEdit boolean == false.
     */
    private void createCustomer() {
        logger.info("Creating a new customer");
        if (validateCustomerFields()) {
            customer = new Customer(
                    customerName.getText(),
                    customerEmail.getText(),
                    customerAddress.getText(),
                    postalCodeTV.getSelectionModel().getSelectedItem().getPostalCode(),
                    postalCodeTV.getSelectionModel().getSelectedItem().getCity());
            customerModel.createCustomer(customer);
            customerModel.loadCustomersWithWrapper();
            Stage stage = (Stage) createOrEditCustomer.getScene().getWindow();
            stage.close();
            logger.info("Customer created successfully");
        }
        else{
            logger.warn("Customer creation failed due to invalid fields");
            warningLoggerForCustomer();
        }
    }

    /**
     * This method is used to edit the selected customer from our persistent model with updated information.
     */
    private void editCustomer() {
        logger.info("Updating a customer");
        if (validateCustomerFields()) {
            customer.setCustName(customerName.getText());
            customer.setCustAddress(customerAddress.getText());
            customer.setCustEmail(customerEmail.getText());
            customer.setPostalCode(postalCodeTV.getSelectionModel().getSelectedItem().getPostalCode());
            customerModel.editCustomer(customer);
            customerModel.loadCustomersWithWrapper();
            logger.info("Edit successful.");
            Stage stage = (Stage) createOrEditCustomer.getScene().getWindow();
            stage.close();
            logger.info("Customer updated successfully");
        } else {
            logger.warn("Customer update failed due to invalid fields");
            warningLoggerForCustomer();

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
        if (customerModel.getSelectedCustomer() != null) {
            windowTitleLabel.setText("Edit Customer");
            customer = customerModel.getSelectedCustomer();
            customerName.setText(customer.getCustName());
            customerEmail.setText(customer.getCustEmail());
            customerAddress.setText(customer.getCustAddress());
            for (PostalCode pc:postalCodeTV.getItems()) {
                if(pc.getPostalCode().equals(customer.getPostalCode()) && pc.getCity().equals(customer.getCity())) {
                    postalCodeTV.getSelectionModel().select(pc);
                    break;
                }
            }
        } else {
            logger.warn("Could not find user to be edited for setOnEdit() in NECustomerController.");
            String str = "Could not find a customer to be edited. Please contact system admin. Class: NECustomerController.";
            super.createWarning(str);
        }
    }


    private void setUpPostalCodeTV() {
        logger.trace("Setting postalCodeTV");
        postalCodeTV.setItems(customerModel.getPostalCodes());
        customerModel.loadPostalCodes();

        postalCode.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPostalCode()));
        city.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCity()));
    }

    public void setEdit(boolean edit) {
        isEdit = edit;
    }

    private boolean isCustEmailTFValid() {
        Pattern p = Pattern.compile("^[a-zA-Z0-9_+&*-]+(?:\\." +
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$");
        Matcher m = p.matcher(customerEmail.getText());

        if (m.find() && m.group().equals(customerEmail.getText()) && (customerEmail.getText().length() <= maxCustEmailLength)) {
            return true;
        } else {
            return false;
        }
    }


    /**
     * This method checks if the length of the customer name textfield is bigger than the max length for the field
     * it returns true if the length is valid
     **/
    private boolean isCustNameTFValid() {
        return customerName.getText().length() > maxCustNameLength;
    }


    /**
     * This method checks if the length of the address textfield is bigger than the max length for the field
     * it returns true if the length is valid
     **/
    private boolean isCustAddressTFValid() {
        return customerAddress.getText().length() > maxCustAddressLength;
    }


    /**
     * An alert created when there is an input validation issue
     * @param context Context for the created alert.
     */
    private void validationAlert(String context){
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Field validation");
        alert.setContentText(context);
        alert.showAndWait();
    }

    /**
     * method to check all combination of fields and show the corresponding alerts or error message
     **/
    private boolean validateCustomerFields() {
        boolean isValid = true;

        if (customerName.getText().isEmpty() || customerEmail.getText().isEmpty() || customerAddress.getText().isEmpty() || postalCodeTV.getSelectionModel().getSelectedItem() == null) {
            String str = "Please fill all the fields";
            super.createWarning(str);
            isValid = false;
        } else {
            if (isCustNameTFValid() && !isCustEmailTFValid() && isCustAddressTFValid()) {
                validationAlert("Customer Email not valid. Customer name and address are too long, max is "
                        + maxCustNameLength + " and " + maxCustAddressLength + " characters respectively");
                isValid = false;
            } else if (isCustNameTFValid() && !isCustEmailTFValid()) {
                validationAlert("Customer name is too long, max is " + maxCustNameLength + " characters. Enter a valid email.");
                isValid = false;
            } else if (isCustNameTFValid() && isCustAddressTFValid()) {
                validationAlert("Customer name and address are too long, max is " + maxCustNameLength + " and " + maxCustAddressLength + " characters respectively");
                isValid = false;
            } else if (!isCustEmailTFValid() && isCustAddressTFValid()) {
                validationAlert("Customer Email not valid. Customer address is too long, max is " + maxCustNameLength + " characters");
                isValid = false;

            } else if (isCustNameTFValid()) {
                validationAlert("Customer Name is too long, max is " + maxCustNameLength + " characters.");
                isValid = false;
            } else if (!isCustEmailTFValid()) {
                validationAlert("Enter a valid email");
                isValid = false;
            } else if (isCustAddressTFValid()) {
                validationAlert("Customer Address is too long, max is " + maxCustAddressLength + " characters.");
                isValid = false;
            }
        }

        return isValid;
    }
    /**
     * method to log about invalid fields when creating or editing a customer
     **/

    private void warningLoggerForCustomer() {
        if (isCustNameTFValid()) {
            logger.warn("Invalid customer name field: customer name exceeds the maximum character limit");
        }
        if (!isCustEmailTFValid() && !customerEmail.getText().isEmpty()) {
            logger.warn("Invalid email field: email exceeds the maximum character limit or is not in a valid format");
        }
        if (isCustAddressTFValid()) {
            logger.warn("Invalid customer address field: customer address exceeds the maximum character limit");
        }

        if(postalCodeTV.getSelectionModel().getSelectedItem() == null){
            logger.warn("Postal code/City not selected");
        }
    }
}

