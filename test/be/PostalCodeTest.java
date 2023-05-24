package be;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

class PostalCodeTest {

    @Test
    void getPostalCode() {
        System.out.println("Get postal code");
        PostalCode instance = new PostalCode("6700", "Esbjerg");
        String expected = "6700";
        String result = instance.getPostalCode();
        assertEquals(result, expected);
    }

    @Test
    void setPostalCode() {
        System.out.println("Set postal code");
        PostalCode instance = new PostalCode("6700", "Esbjerg");
        String expected = "7000";
        instance.setPostalCode("7000");
        String result = instance.getPostalCode();
        assertEquals(result, expected);
    }

    @Test
    void getCity() {
        System.out.println("Get city code");
        PostalCode instance = new PostalCode("6700", "Esbjerg");
        String expected = "Esbjerg";
        String result = instance.getCity();
        assertEquals(result, expected);
    }

    @Test
    void setCity() {
        System.out.println("Set city code");
        PostalCode instance = new PostalCode("6700", "Esbjerg");
        String expected = "København";
        instance.setCity("København");
        String result = instance.getCity();
        assertEquals(result, expected);
    }

    @Test
    void testEquals() {
        System.out.println("Get postal code");
        PostalCode instance = new PostalCode("6700", "Esbjerg");
        PostalCode instance2 = new PostalCode("6700", "Esbjerg");
        assertEquals(instance2,instance);
    }
}