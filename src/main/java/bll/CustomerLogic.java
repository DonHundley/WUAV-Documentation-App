package bll;

import be.*;
import dal.*;

import java.util.*;

public class CustomerLogic {
    private CustomerDAO customerDAO = new CustomerDAO();
    public List<Customer> getCustomers() {
        return customerDAO.getAllCustomers();
    }

    public void createCustomer(Customer customer) {
        customerDAO.createCustomer(customer);
    }

    public void editCustomer(Customer customer) {
        customerDAO.updateCustomer(customer);
    }

    public void deleteCustomer(Customer customer) {
        customerDAO.deleteCustomer(customer);
    }
}
