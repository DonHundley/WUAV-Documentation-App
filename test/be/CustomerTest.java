package be;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

class CustomerTest {

    @Test
    void getCustID() {
        System.out.println("Get Customer ID");
        Customer instance = new Customer();
        int expResult = 567;
        instance.setCustID(567);
        int result = instance.getCustID();
        assertEquals(expResult, result);
    }

    @Test
    void setCustID() {
        System.out.println("Set Customer ID");
        Customer instance = new Customer();
        int expResult = 300;
        instance.setCustID(300);
        int result = instance.getCustID();
        assertEquals(expResult, result);
    }

    @Test
    void getCustName() {
        System.out.println("Get Customer Name");
        Customer instance = new Customer();
        String expResult = "John";
        instance.setCustName("John");
        String result = instance.getCustName();
        assertEquals(result, expResult);
    }

    @Test
    void setCustName() {
        System.out.println("Set Customer Name");
        Customer instance = new Customer();
        String expResult = "Smith";
        instance.setCustName("Smith");
        String result = instance.getCustName();
        assertEquals(result, expResult);
    }

    @Test
    void getCustEmail() {
        System.out.println("Get Customer Email");
        Customer instance = new Customer();
        String expResult = "JohnSmith@AOL.Com";
        instance.setCustEmail("JohnSmith@AOL.Com");
        String result = instance.getCustEmail();
        assertEquals(result, expResult);
    }

    @Test
    void setCustEmail() {
        System.out.println("Set Customer Email");
        Customer instance = new Customer();
        String expResult = "JaneSmith@MSN.Com";
        instance.setCustEmail("JaneSmith@MSN.Com");
        String result = instance.getCustEmail();
        assertEquals(result, expResult);
    }

    @Test
    void getCustAddress() {
        System.out.println("Get Customer Address");
        Customer instance = new Customer();
        String expResult = "123 Generic Address Lane";
        instance.setCustAddress("123 Generic Address Lane");
        String result = instance.getCustAddress();
        assertEquals(expResult, result);
    }

    @Test
    void setCustAddress() {
        System.out.println("Set Customer Address");
        Customer instance = new Customer();
        String expResult = "456 Normal Address Road";
        instance.setCustAddress("456 Normal Address Road");
        String result = instance.getCustAddress();
        assertEquals(expResult, result);
    }

    @Test
    void getPostalCode() {
        System.out.println("Get postal code");
        Customer instance = new Customer();
        String expResult = "1234";
        instance.setPostalCode("1234");
        String result = instance.getPostalCode();
        assertEquals(expResult, result);
    }

    @Test
    void setPostalCode() {
        System.out.println("Set postal code");
        Customer instance = new Customer();
        String expResult = "4567";
        instance.setPostalCode("4567");
        String result = instance.getPostalCode();
        assertEquals(expResult, result);
    }

    @Test
    void getCity() {
        System.out.println("Get city");
        Customer instance= new Customer();
        String expResult = "Atlanta";
        instance.setCity("Atlanta");
        String result = instance.getCity();
        assertEquals(expResult, result);
    }

    @Test
    void setCity() {
        System.out.println("Set city");
        Customer instance= new Customer();
        String expResult = "København";
        instance.setCity("København");
        String result = instance.getCity();
        assertEquals(expResult, result);
    }
}