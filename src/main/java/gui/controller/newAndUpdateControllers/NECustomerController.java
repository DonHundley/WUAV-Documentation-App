package gui.controller.newAndUpdateControllers;

import be.*;
import gui.controller.mainViewControllers.*;
import gui.model.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.*;
import javafx.scene.control.*;
import javafx.stage.*;


import java.util.*;

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
    private CustomerModel customerModel = CustomerModel.getInstance();


    // True if editing, false if creating a new customer.
    private boolean isEdit;

    /**
     * This method is used to set our models and choose if we are editing or creating a customer.
     *
     * @param isEdit          if true, we are editing a customer.
     */
    public void setNEController(Boolean isEdit) {

        setEdit(isEdit); // sets the boolean to store if we are editing or creating.
        if (isEdit) { // if we are editing we set all text fields with the current information.
            setUpPostalCodeTV();
            setOnEdit();
        } else {
            setUpPostalCodeTV();
            windowTitleLabel.setText("New Customer");
            createOrEditCustomer.setText("Create");
        }

    }

    /**
     * This will create or edit a customer based on the isEdit boolean
     *
     * @param actionEvent triggered when the user activates the create/edit button.
     */
    @FXML
    private void createOrEditCustomer(ActionEvent actionEvent) {
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
        if (!customerName.getText().isEmpty() && !customerEmail.getText().isEmpty() && !customerAddress.getText().isEmpty() && postalCodeTV.getSelectionModel().getSelectedItem() != null) {
            customer = new Customer(customerName.getText(), customerEmail.getText(), customerAddress.getText(), postalCodeTV.getSelectionModel().getSelectedItem().getPostalCode());
            customerModel.createCustomer(customer);
            customerModel.loadCustomersWithWrapper();
            Stage stage = (Stage) createOrEditCustomer.getScene().getWindow();
            stage.close();
        } else {
            String str = "Please fill all the fields to create a customer";
            super.createWarning(str);

        }
    }

    /**
     * This method is used to edit the selected customer from our persistent model with updated information.
     */
    private void editCustomer() {
        if (!customerName.getText().isEmpty() && !customerEmail.getText().isEmpty() && !customerAddress.getText().isEmpty() && postalCodeTV.getSelectionModel().getSelectedItem() != null) {
            customer.setCustName(customerName.getText());
            customer.setCustAddress(customerAddress.getText());
            customer.setCustEmail(customerEmail.getText());
            customer.setPostalCode(postalCodeTV.getSelectionModel().getSelectedItem().getPostalCode());

            customerModel.editCustomer(customer);

            customerModel.loadCustomersWithWrapper();
            Stage stage = (Stage) createOrEditCustomer.getScene().getWindow();
            stage.close();
        } else {
            String str = "Please fill all the fields to edit a customer";
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
            String str = "Could not find a customer to be edited. Please contact system admin. Class: NECustomerController.";
            super.createWarning(str);
        }
    }


    private void setUpPostalCodeTV() {
        postalCodeTV.setItems(customerModel.getPostalCodes());
        customerModel.loadPostalCodes();

        postalCode.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPostalCode()));
        city.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCity()));
    }

    public void setEdit(boolean edit) {
        isEdit = edit;
    }
}
