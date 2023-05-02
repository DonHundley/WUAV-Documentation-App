package gui.controller;

import be.*;
import gui.model.*;
import javafx.fxml.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.*;

import java.awt.event.*;

public class ManageCustomerController {
    // Tableview
    @FXML private TableView<Customer> customersTV;
    @FXML private TableColumn<Customer, String> customerEmail;
    @FXML private TableColumn<Customer, String> customerName;
    @FXML private TableColumn<Customer, String> customerAddress;
    @FXML private TableColumn<Project, String> projectName;

    // TextField
    @FXML private TextField searchCustomer;

    // Labels
    @FXML private Label usernameLabel;
    @FXML private Label windowTitle;

    // Models
    private Persistent persistenceModel;
    private Observables observablesModel;
    private Functions functionsModel;

    /**
     * We call this when this controller is called from navigation to set our models, tableview, and labels.
     * @param persistenceModel this is our instance of Persistent from navigation
     * @param observablesModel this is our instance of Observables from navigation
     * @param functionsModel this is our instance of Functions from navigation
     */
    public void userController(Persistent persistenceModel, Observables observablesModel, Functions functionsModel){
        this.persistenceModel = persistenceModel;
        this.functionsModel = functionsModel;
        this.observablesModel = observablesModel;

        setUsernameLabel();
        setCustomerTableView();
    }

    /**
     * We use this to set our username label and window title label.
     */
    private void setUsernameLabel() {// set our username label to the users name
        windowTitle.setText("Customers");
        usernameLabel.setText(persistenceModel.getLoggedInUser().getFirstName() + " " + persistenceModel.getLoggedInUser().getLastName());
    }

    /**
     * Mixed List
     * This will be changed later
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
