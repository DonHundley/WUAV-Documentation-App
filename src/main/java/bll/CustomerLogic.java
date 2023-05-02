package bll;

import be.*;
import dal.*;

import java.util.*;

public class CustomerLogic {
    private CustomerDAO customerDAO = new CustomerDAO();

    /**
     * @return This returns the list of customers created by CustomerDAO.
     */
    public List<Customer> getCustomers() {
        return customerDAO.getAllCustomers();
    }

    /**
     * Functions model uses this to create a new customer.
     * @param customer is a new Customer to be added to the database.
     */
    public void createCustomer(Customer customer) {
        customerDAO.createCustomer(customer);
    }

    /**
     * Functions model uses this to edit a customer.
     * @param customer is the Customer to be edited in the database.
     */
    public void editCustomer(Customer customer) {
        customerDAO.updateCustomer(customer);
    }

    /**
     * Functions model uses this to delete a customer.
     * @param customer is the customer to be deleted from the database.
     */
    public void deleteCustomer(Customer customer) {
        customerDAO.deleteCustomer(customer);
    }
}
