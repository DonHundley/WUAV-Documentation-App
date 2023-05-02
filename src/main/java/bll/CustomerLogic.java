package bll;

import be.*;
import dal.*;

import java.util.*;

public class CustomerLogic {
    private CustomerDAO customerDAO = new CustomerDAO();
    public List<Customer> getCustomers() {
        return customerDAO.getAllCustomers();
    }
}
