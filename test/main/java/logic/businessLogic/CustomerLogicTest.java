package main.java.logic.businessLogic;

import be.Customer;
import be.CustomerWrapper;
import be.PostalCode;
import be.Project;
import logic.businessLogic.CustomerLogic;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


public class CustomerLogicTest {
    CustomerLogic customerLogic;

    @BeforeEach
    void setUp() {
        customerLogic = new CustomerLogic();
    }

    @Test
    void testSearchCustomer() {
        //Arrange
        Customer customer1 = new Customer(1, "Customer", "email@email.com", "Denmarksgade 1", "6700");
        Project project1 = new Project(1, "Project", java.sql.Date.valueOf(java.time.LocalDate.now()), 1);
        PostalCode postalCode1 = new PostalCode("6700", "Esbjerg");

        Customer customer2 = new Customer(1, "Customer", "email@email.com", "Kirkevej 1", "6705");
        Project project2 = new Project(1, "Project", java.sql.Date.valueOf(java.time.LocalDate.now()), 2);
        PostalCode postalCode2 = new PostalCode("6705", "Esbjerg Ø");

        CustomerWrapper customerWrapper1 = new CustomerWrapper(customer1, project1, postalCode1);
        CustomerWrapper customerWrapper2 = new CustomerWrapper(customer2, project2, postalCode2);
        List<CustomerWrapper> arrayList = new ArrayList<>();
        arrayList.add(customerWrapper1);
        arrayList.add(customerWrapper2);

        List<CustomerWrapper> expectedResult = new ArrayList<>();
        expectedResult.add(customerWrapper1);

        /*testing  postal code match*/

        String query1 = "6700";
        //Act
        List<CustomerWrapper> result1 = customerLogic.searchCustomer(arrayList, query1);
        //Assert
        assertEquals(expectedResult, result1);

        /*testing address match*/

        String query2 = "Denmarksgade";
        //Act
        List<CustomerWrapper> result2 = customerLogic.searchCustomer(expectedResult, query2);
        //Assert
        assertEquals(expectedResult, result2);

        /*testing address match uppercase*/

        String query3 = "DENMARKSGADE";
        //Act
        List<CustomerWrapper> result3 = customerLogic.searchCustomer(expectedResult, query3);
        //Assert
        assertEquals(expectedResult, result3);

        /*testing not matching postal code*/

        String query4 = "2200";
        //Act
        List<CustomerWrapper> result4 = customerLogic.searchCustomer(expectedResult, query4);
        //Assert
        assertNotEquals(expectedResult, result4);

        /*testing not matching address*/

        String query5 = "Storegade";
        //Act
        List<CustomerWrapper> result5 = customerLogic.searchCustomer(expectedResult, query5);
        //Assert
        assertNotEquals(expectedResult, result5);
    }
}
