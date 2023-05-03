package dal;


import be.Customer;
import be.CustomerWrapper;
import be.Project;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerDAO {
    private DatabaseConnector databaseConnector;

    public CustomerDAO() {
        databaseConnector = new DatabaseConnector();
    }

    /**
     * Method to create customer in the database.
     */
    public Customer createCustomer(Customer customer) {
        try (Connection connection = databaseConnector.getConnection()) {
            String sql = "INSERT INTO customer(customer_name, customer_email, customer_address) VALUES (?,?,?)";

            PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, customer.getCustName());
            statement.setString(2, customer.getCustEmail());
            statement.setString(3, customer.getCustAddress());
            statement.execute();

            ResultSet keys = statement.getGeneratedKeys();
            keys.next();
            int id = keys.getInt(1);

            return new Customer(id, customer.getCustName(), customer.getCustEmail(), customer.getCustAddress());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Method to update customer in database.
     */
    public void updateCustomer(Customer customer) {
        try (Connection connection = databaseConnector.getConnection()) {
            String sql = "UPDATE customer SET customer_name = ?, customer_email = ?, customer_address = ?) " + "WHERE customerID = ?";

            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setString(1, customer.getCustName());
            statement.setString(2, customer.getCustEmail());
            statement.setString(3, customer.getCustAddress());
            statement.setInt(4, customer.getCustID());
            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Method to delete customer from database.
     */
    public void deleteCustomer(Customer customer) {
        try (Connection conn = databaseConnector.getConnection()) {
            String sql = "DELETE FROM customer WHERE customerID= ?";

            PreparedStatement pstmt = conn.prepareStatement(sql);

            pstmt.setInt(1, customer.getCustID());
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Method to get all users from database.
     */
    public List<Customer> getAllCustomers() {
        ArrayList<Customer> customers = new ArrayList<>();

        try (Connection connection = databaseConnector.getConnection()) {
            String sql = "SELECT * FROM customer";
            Statement statement = connection.createStatement();

            if(statement.execute(sql)) {
                ResultSet resultSet = statement.getResultSet();

                while (resultSet.next()) {
                    int id = resultSet.getInt("customerID");
                    String name = resultSet.getString("customer_name");
                    String email = resultSet.getString("customer_email");
                    String address = resultSet.getString("customer_address");

                    Customer customer = new Customer(id, name, email, address);
                    customers.add(customer);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return customers;
    }

    /**
     * Method to get the customer by ID from database.
     */
    public Customer getCustomerById(Customer customer) {
        try (Connection connection = databaseConnector.getConnection()) {
            String sql = "SELECT * FROM customer WHERE customerID = ?";

            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setInt(1, customer.getCustID());

            if(pstmt.execute(sql)) {
                ResultSet resultSet = pstmt.getResultSet();

                while (resultSet.next()) {
                    int id = resultSet.getInt("customerID");
                    String name = resultSet.getString("customer_name");
                    String email = resultSet.getString("customer_email");
                    String address = resultSet.getString("customer_address");

                    return new Customer(id, name, email, address);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
    /**method to get all customers and associated projects from the database. this is used to later display in a tableview*/

    public List<CustomerWrapper> getAllCustomersWithProjects() {
        List<CustomerWrapper> customersWithProjectsList = new ArrayList<>();
        String sql = "SELECT c.*, p.* " +
                "FROM customer c " +
                "LEFT JOIN project p ON c.customerID = p.customerID";
        try (Connection connection = databaseConnector.getConnection()) {
            Statement statement = connection.createStatement();
            if (statement.execute(sql)) {
                ResultSet resultSet = statement.getResultSet();
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
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return customersWithProjectsList;

}
}
