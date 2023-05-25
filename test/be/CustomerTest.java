package be;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

class CustomerTest {

    @Test
    void getCustID() {
        System.out.println("Get Customer ID");
        Customer instance = new Customer(567,"Customer", "email@email.com", "Denmarksgade 1", "6700","Esbjerg");
        int expResult = 567;
        int result = instance.getCustID();
        assertEquals(expResult, result);
    }

    @Test
    void setCustID() {
        System.out.println("Set Customer ID");
        Customer instance = new Customer(1,"Customer", "email@email.com", "Denmarksgade 1", "6700","Esbjerg");
        int expResult = 300;
        instance.setCustID(300);
        int result = instance.getCustID();
        assertEquals(expResult, result);
    }

    @Test
    void getCustName() {
        System.out.println("Get Customer Name");
        Customer instance = new Customer(1,"Customer", "email@email.com", "Denmarksgade 1", "6700","Esbjerg");
        String expResult = "Customer";
        String result = instance.getCustName();
        assertEquals(result, expResult);
    }

    @Test
    void setCustName() {
        System.out.println("Set Customer Name");
        Customer instance = new Customer(1,"Customer", "email@email.com", "Denmarksgade 1", "6700","Esbjerg");
        String expResult = "Smith";
        instance.setCustName("Smith");
        String result = instance.getCustName();
        assertEquals(result, expResult);
    }

    @Test
    void getCustEmail() {
        System.out.println("Get Customer Email");
        Customer instance = new Customer(1,"Customer", "email@email.com", "Denmarksgade 1", "6700","Esbjerg");
        String expResult = "email@email.com";
        String result = instance.getCustEmail();
        assertEquals(result, expResult);
    }

    @Test
    void setCustEmail() {
        System.out.println("Set Customer Email");
        Customer instance = new Customer(1,"Customer", "email@email.com", "Denmarksgade 1", "6700","Esbjerg");
        String expResult = "JaneSmith@MSN.Com";
        instance.setCustEmail("JaneSmith@MSN.Com");
        String result = instance.getCustEmail();
        assertEquals(result, expResult);
    }

    @Test
    void getCustAddress() {
        System.out.println("Get Customer Address");
        Customer instance = new Customer(1,"Customer", "email@email.com", "Denmarksgade 1", "6700","Esbjerg");
        String expResult = "Denmarksgade 1";
        String result = instance.getCustAddress();
        assertEquals(expResult, result);
    }

    @Test
    void setCustAddress() {
        System.out.println("Set Customer Address");
        Customer instance = new Customer(1,"Customer", "email@email.com", "Denmarksgade 1", "6700","Esbjerg");
        String expResult = "123 Normal Address Road";
        instance.setCustAddress("123 Normal Address Road");
        String result = instance.getCustAddress();
        assertEquals(expResult, result);
    }

    @Test
    void getPostalCode() {
        System.out.println("Get postal code");
        Customer instance = new Customer(1,"Customer", "email@email.com", "Denmarksgade 1", "6700","Esbjerg");
        String expResult = "6700";
        String result = instance.getPostalCode();
        assertEquals(expResult, result);
    }

    @Test
    void setPostalCode() {
        System.out.println("Set postal code");
        Customer instance = new Customer(1,"Customer", "email@email.com", "Denmarksgade 1", "6700","Esbjerg");
        String expResult = "4567";
        instance.setPostalCode("4567");
        String result = instance.getPostalCode();
        assertEquals(expResult, result);
    }

    @Test
    void getCity() {
        System.out.println("Get city");
        Customer instance= new Customer(1,"Customer", "email@email.com", "Denmarksgade 1", "6700","Esbjerg");
        String expResult = "Esbjerg";
        String result = instance.getCity();
        assertEquals(expResult, result);
    }

    @Test
    void setCity() {
        System.out.println("Set city");
        Customer instance= new Customer(1,"Customer", "email@email.com", "Denmarksgade 1", "6700","Esbjerg");
        String expResult = "København";
        instance.setCity("København");
        String result = instance.getCity();
        assertEquals(expResult, result);
    }
}