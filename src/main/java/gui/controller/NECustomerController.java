package gui.controller;

import be.*;
import gui.model.*;
import javafx.event.ActionEvent;
import javafx.fxml.*;
import javafx.scene.control.*;
import javafx.stage.*;


import java.util.*;

public class NECustomerController {
    // FXML
    @FXML private TextField customerName;
    @FXML private TextField customerEmail;
    @FXML private TextField customerAddress;
    @FXML private Label windowTitleLabel;
    @FXML private Button cancelButton;

    // Customer instance
    private Customer customer;

    // Model instances
    private Persistent persistentModel = Persistent.getInstance();
    private Functions functionsModel = new Functions();

    // True if editing, false if creating a new customer.
    private boolean isEdit;

    /**
     * This method is used to set our models and choose if we are editing or creating a customer.
     * @param isEdit if true, we are editing a customer.
     * @param persistentModel the instance of the persistent model
     * @param functionsModel the instance of the functions model
     */
    public void setNEController(Boolean isEdit, Persistent persistentModel, Functions functionsModel){
        this.persistentModel = persistentModel;
        this.functionsModel = functionsModel;

        setEdit(isEdit); // sets the boolean to store if we are editing or creating.
        if(isEdit){ // if we are editing we set all text fields with the current information.
            setOnEdit();
        }else {windowTitleLabel.setText("New Customer");}

    }

    /**
     * This will create or edit a customer based on the isEdit boolean
     * @param actionEvent triggered when the user activates the create/edit button.
     */
    @FXML private void createOrEditCustomer(ActionEvent actionEvent){
        if(isEdit){
            editCustomer();
        } else {
            createCustomer();
        }
    }

    /**
     * This method is used to create a new customer if our isEdit boolean == false.
     */
    private void createCustomer(){
        customer = new Customer(customerName.getText(), customerEmail.getText(), customerAddress.getText());
        functionsModel.createCustomer(customer);
    }

    /**
     * This method is used to edit the selected customer from our persistent model with updated information.
     */
    private void editCustomer(){
        customer.setCustName(customerName.getText());
        customer.setCustAddress(customerAddress.getText());
        customer.setCustEmail(customerEmail.getText());

        functionsModel.editCustomer(customer);
    }

    /**
     * Closes the window with an action event.
     * @param actionEvent triggers when the user activates the cancel button.
     */
    @FXML private void cancel(ActionEvent actionEvent){
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    /**
     * If the boolean isEdit == true, we set all the fields with the information from the selected customer.
     */
    private void setOnEdit(){
        if(persistentModel.getSelectedCustomer() != null) {
            windowTitleLabel.setText("Edit Customer");
            customer = persistentModel.getSelectedCustomer();
            customerName.setText(customer.getCustName());
            customerEmail.setText(customer.getCustEmail());
            customerAddress.setText(customer.getCustAddress());
        } else {
            String str = "Could not find a customer to be edited. Please contact system admin. Class: NECustomerController.";
            customerError(str);
        }
    }

    /**
     * We use this to display an error to the user if there is a problem.
     * @param str This is the source of the problem so that the user is informed.
     */
    private void customerError(String str) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Error");
        alert.setHeaderText(str);

        Optional<ButtonType> result = alert.showAndWait();
        if(result.isPresent() && result.get() == ButtonType.OK){
            alert.close();
        } else {
            alert.close();
        }
    }

    public void setEdit(boolean edit) {
        isEdit = edit;
    }
}
