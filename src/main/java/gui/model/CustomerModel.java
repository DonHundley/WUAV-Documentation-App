package gui.model;

import be.*;
import javafx.collections.*;
import logic.businessLogic.*;

public class CustomerModel {
    private Customer selectedCustomer;
    private CustomerLogic customerLogic = new CustomerLogic();

    private ObservableList<CustomerWrapper> customerswithWrapper = FXCollections.observableArrayList();
    private ObservableList<Customer> customers = FXCollections.observableArrayList();


    /**
     * Thread safe singleton of CustomerModel.
     */
    private CustomerModel(){}
    private static CustomerModel instance;
    public static synchronized CustomerModel getInstance(){
        if(instance == null){
            instance = new CustomerModel();
        }
        return instance;
    }

    public void search(String query) {
        customerswithWrapper.clear();
        customerswithWrapper.addAll(customerLogic.searchCustomer(query));
    }

    /**
     * Customer lists.
     */
    public ObservableList<CustomerWrapper> getCustomersWithWrapper() {
        return customerswithWrapper;
    }
    public ObservableList<Customer> getCustomers() {
        return customers;
    }
    private ObservableList<PostalCode> postalCodes = FXCollections.observableArrayList();
    public ObservableList<PostalCode> getPostalCodes() {return postalCodes;}

    /**
     * List loaders.
     */
    public void loadCustomersWithWrapper() {
        customerswithWrapper.clear();
        customerswithWrapper.addAll(customerLogic.getCustomersWithProjects());
    }
    public void loadCustomers() {
        customers.clear();
        customers.addAll(customerLogic.getCustomers());
    }

    public void loadPostalCodes() {
        postalCodes.clear();
        postalCodes.addAll(customerLogic.getAllPostalCodes());
    }

    /**
     * Customer crud functions
     */
    public void createCustomer(Customer customer) {
        customerLogic.createCustomer(customer);
    }

    public void editCustomer(Customer customer) {
        customerLogic.editCustomer(customer);
    }

    public void deleteCustomer(Customer customer) {
        customerLogic.deleteCustomer(customer);
    }

    /**
     * getter and setter for selected customer.
     */
    public Customer getSelectedCustomer() {return selectedCustomer;}
    public void setSelectedCustomer(Customer selectedCustomer) {this.selectedCustomer = selectedCustomer;}
}
