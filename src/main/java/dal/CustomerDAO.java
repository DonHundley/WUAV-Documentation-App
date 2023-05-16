package dal;


import be.Customer;
import be.CustomerWrapper;
import be.Project;
import org.apache.logging.log4j.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerDAO {
    private DatabaseConnector databaseConnector;

    public CustomerDAO() {
        databaseConnector = new DatabaseConnector();
    }
    private static final Logger logger = LogManager.getLogger("debugLogger");

    public Customer createCustomer(Customer customer) {
        logger.info("Opening connection to create customer.");
        try (Connection connection = databaseConnector.getConnection()) {
            String sql = "INSERT INTO customer(customer_name, customer_email, customer_address) VALUES (?,?,?)";

            PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, customer.getCustName());
            statement.setString(2, customer.getCustEmail());
            statement.setString(3, customer.getCustAddress());
            logger.info("Executing "+ sql);
            statement.execute();

            ResultSet keys = statement.getGeneratedKeys();
            keys.next();
            int id = keys.getInt(1);

            logger.debug("Creating customer." + customer.toString());
            customer.setCustID(id);
            //return new Customer(id, customer.getCustName(), customer.getCustEmail(), customer.getCustAddress());
        } catch (SQLException e) {
            logger.error("There has been an issue adding customer to database. CLASS: CustomerDAO CAUSE: " + e);
        }
        logger.info("Returning customer");
        return customer;
    }

    /**
     * Method to update customer in database.
     */
    public void updateCustomer(Customer customer) {
        logger.info("Opening connection to update customer.");
        try (Connection connection = databaseConnector.getConnection()) {
            String sql = "UPDATE customer SET customer_name = ?, customer_email = ?, customer_address = ? " + "WHERE customerID = ?";

            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setString(1, customer.getCustName());
            statement.setString(2, customer.getCustEmail());
            statement.setString(3, customer.getCustAddress());
            statement.setInt(4, customer.getCustID());
            logger.info("Executing " + sql);
            statement.executeUpdate();

        } catch (SQLException e) {
            logger.error("There has been an issue updating the customer. CLASS: CustomerDAO CAUSE: " + e);
        }
        logger.info("update customer is complete.");
    }

    /**
     * Method to delete customer from database.
     */
    public void deleteCustomer(Customer customer) {
        logger.info("opening connection to delete customer");
        try (Connection conn = databaseConnector.getConnection()) {
            String sql = "DELETE FROM customer WHERE customerID= ?";

            PreparedStatement pstmt = conn.prepareStatement(sql);

            pstmt.setInt(1, customer.getCustID());
            logger.info("Executing " + sql);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            logger.error("There has been an issue deleting the customer. CLASS: CustomerDAO CAUSE: " + e);
        }
        logger.info("delete customer is complete.");
    }

    /**
     * Method to get all users from database.
     */
    public List<Customer> getAllCustomers() {
        ArrayList<Customer> customers = new ArrayList<>();
        logger.info("Opening connection to create list of customers");
        try (Connection connection = databaseConnector.getConnection()) {
            String sql = "SELECT * FROM customer";
            Statement statement = connection.createStatement();

            logger.info("Executing " + sql);
            if(statement.execute(sql)) {
                ResultSet resultSet = statement.getResultSet();
                logger.info("Iterating over result set for " + sql);
                while (resultSet.next()) {
                    int id = resultSet.getInt("customerID");
                    String name = resultSet.getString("customer_name");
                    String email = resultSet.getString("customer_email");
                    String address = resultSet.getString("customer_address");

                    Customer customer = new Customer(id, name, email, address);
                    customers.add(customer);
                }
                if(customers.size() == 0){
                    logger.warn("There are no customers in the list.");
                }
            }
        } catch (SQLException e) {
            logger.error("There has been an issue creating a list of customers. CLASS: CustomerDAO CAUSE: " + e);
        }
        logger.info("Returning list customers");
        return customers;
    }


    /**method to get all customers and associated projects from the database. this is used to later display in a tableview*/

    public List<CustomerWrapper> getAllCustomersWithProjects() {
        List<CustomerWrapper> customersWithProjectsList = new ArrayList<>();


        logger.info("opening connection in getAllCustomersWithProjects()");
        try (Connection connection = databaseConnector.getConnection()) {
            String sql = "SELECT c.*, p.* " +
                    "FROM customer c " +
                    "LEFT JOIN project p ON c.customerID = p.customerID";

            Statement statement = connection.createStatement();
            logger.info("Executing " + sql);
            if (statement.execute(sql)) {
                ResultSet resultSet = statement.getResultSet();
                logger.info("Iterating over " + sql);
                while (resultSet.next()) {
                    Customer customer = new Customer(
                            resultSet.getInt("customerID"),
                            resultSet.getString("customer_name"),
                            resultSet.getString("customer_email"),
                            resultSet.getString("customer_address")
                    );
                    Project project = new Project(
                            resultSet.getInt("projectID"),
                            resultSet.getString("project_name"),
                            resultSet.getDate("date_created"),
                            resultSet.getInt("customerID")
                    );
                    CustomerWrapper wrapper = new CustomerWrapper(customer, project);
                    customersWithProjectsList.add(wrapper);
                }
                if(customersWithProjectsList.isEmpty()){
                    logger.warn("There are no items in the list!");
                }
            }
        } catch (SQLException e) {
            logger.error("There has been a problem creating a list of customers with projects. CLASS: CustomerDAO CAUSE: " + e);
        }
        logger.info("Returning list customersWithProjectsList");
        return customersWithProjectsList;
    }
}
