package gui.model;

import be.*;
import javafx.collections.*;
import logic.businessLogic.*;

public class CustomerModel {
    private Customer selectedCustomer;
    private final CustomerLogic customerLogic;
    private final ObservableList<CustomerWrapper> customerswithWrapper;
    private final ObservableList<Customer> customers;
    private final ObservableList<PostalCode> postalCodes;

    /**
     * Thread safe singleton of CustomerModel.
     */
    private CustomerModel(){
        customerLogic = new CustomerLogic();
        customerswithWrapper = FXCollections.observableArrayList();
        customers = FXCollections.observableArrayList();
        postalCodes = FXCollections.observableArrayList();
    }
    private static CustomerModel instance;
    public static synchronized CustomerModel getInstance(){
        if(instance == null){
            instance = new CustomerModel();
        }
        return instance;
    }

    public void search(String query) {
        customerswithWrapper.clear();
        customerswithWrapper.addAll(customerLogic.searchCustomer(customerLogic.getCustomersWithProjects(),query));
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
