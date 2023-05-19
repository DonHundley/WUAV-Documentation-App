package logic.businessLogic;

import be.*;
import dal.*;
import org.apache.logging.log4j.*;

import java.util.*;

public class CustomerLogic {
    private CustomerDAO customerDAO = new CustomerDAO();
    private static final Logger logger = LogManager.getLogger("debugLogger");

    private PostalCodeDAO postalCodeDAO = new PostalCodeDAO();

    /**
     * @return This returns the list of postal codes created by PostalCodeDAO.
     */
    public List<PostalCode> getAllPostalCodes() {
        return postalCodeDAO.getAllPostalCodes();
    }

    public List<CustomerWrapper> searchCustomer(List<CustomerWrapper> customers,String query) {
        logger.info("Creating customer filtered list");
        List<CustomerWrapper> filtered = new ArrayList<>();
        logger.info("Checking list for query");
        for(CustomerWrapper c : customers){
            if((""+c.getCustomer().getCustAddress()).toLowerCase().contains(query.toLowerCase()) || c.getPostalCode().getPostalCode().contains(query)) {
                filtered.add(c);
            }
        }
        logger.info("Returning filtered list, process complete.");
        return filtered;
    }

    /**
     * @return This returns the list of customers created by CustomerDAO.
     */
    public List<Customer> getCustomers() {
        return customerDAO.getAllCustomers();
    }

    /**
     * @return This returns the list of customers with associated projects created by CustomerDAO.
     */
    public List<CustomerWrapper> getCustomersWithProjects() {
        return customerDAO.getAllCustomersWithProjects();
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
