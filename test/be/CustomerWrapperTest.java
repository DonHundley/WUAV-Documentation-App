package be;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

class CustomerWrapperTest {

    @Test
    void getCustomer() {
        System.out.println("Get customer");
        Customer customer = new Customer(1,"Customer", "email@email.com", "Denmarksgade 1", "6700","Esbjerg");
        PostalCode postalCode = new PostalCode("6700", "Esbjerg");
        Project project = new Project(1, "Project 1", java.sql.Date.valueOf(java.time.LocalDate.now()), 1);
        CustomerWrapper customerWrapper = new CustomerWrapper(customer, project, postalCode);

        assertEquals(customer, customerWrapper.getCustomer());
    }

    @Test
    void getProject() {
        System.out.println("Get project");
        Customer customer = new Customer(1,"Customer", "email@email.com", "Denmarksgade 1", "6700","Esbjerg");
        PostalCode postalCode = new PostalCode("6700", "Esbjerg");
        Project project = new Project(1, "Project 1", java.sql.Date.valueOf(java.time.LocalDate.now()), 1);
        CustomerWrapper customerWrapper = new CustomerWrapper(customer, project, postalCode);

        assertEquals(project, customerWrapper.getProject());
    }

    @Test
    void getPostalCode() {
        System.out.println("Get postal code");
        Customer customer = new Customer(1,"Customer", "email@email.com", "Denmarksgade 1", "6700","Esbjerg");
        PostalCode postalCode = new PostalCode("6700", "Esbjerg");
        Project project = new Project(1, "Project 1", java.sql.Date.valueOf(java.time.LocalDate.now()), 1);
        CustomerWrapper customerWrapper = new CustomerWrapper(customer, project, postalCode);

        assertEquals(postalCode, customerWrapper.getPostalCode());
    }
}