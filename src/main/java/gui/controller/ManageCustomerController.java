package gui.controller;

import be.*;
import gui.model.*;
import javafx.fxml.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.*;

import java.awt.event.*;

public class ManageCustomerController {

    @FXML private TableView<Customer> customersTV;
    @FXML private TableColumn<Customer, String> customerEmail;
    @FXML private TableColumn<Customer, String> customerName;
    @FXML private TableColumn<Customer, String> customerAddress;
    @FXML private TableColumn<Project, String> projectName;
    @FXML private TextField searchCustomer;
    @FXML private Label usernameLabel;
    @FXML private Label windowTitle;

    private Persistent persistenceModel;
    private Observables observablesModel;

    private void setUsernameLabel() {// set our username label to the users name
        windowTitle.setText("Customers");
        usernameLabel.setText(persistenceModel.getLoggedInUser().getFirstName() + " " + persistenceModel.getLoggedInUser().getLastName());
    }

    /**
     * Mixed List
     */
    private void setCustomerTableView() {
        customersTV.setItems(observablesModel.getCustomers());
        observablesModel.loadCustomers();

        customerAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        customerName.setCellValueFactory(new PropertyValueFactory<>("name"));
        customerEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        projectName.setCellValueFactory(new PropertyValueFactory<>("project")); // needs changing
    }

    private void clickCustomerTV(){}
    @FXML private void editCustomer(ActionEvent actionEvent){}

    @FXML private void createCustomer(ActionEvent actionEvent){}

    @FXML private void logOut(ActionEvent actionEvent){}
}
