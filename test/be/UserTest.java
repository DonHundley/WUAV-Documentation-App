package be;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void getUserID() {
        System.out.println("Get user ID");
        User instance =new User(1,"username1", "password1","Admin","Name1", "lastname1");
        int expected = 1;
        int result = instance.getUserID();
        assertEquals(expected,result);
    }

    @Test
    void setUserID() {
        System.out.println("Set user ID");
        User instance =new User(1,"username1", "password1","Admin","Name1", "lastname1");
        int expected = 5;
        instance.setUserID(5);
        int result = instance.getUserID();
        assertEquals(expected,result);
    }

    @Test
    void getUserName() {
        System.out.println("Get user name");
        User instance =new User(1,"username1", "password1","Admin","Name1", "lastname1");
        String expected = "username1";
        String result = instance.getUserName();
        assertEquals(expected,result);
    }

    @Test
    void setUserName() {
        System.out.println("Set username");
        User instance =new User(1,"username1", "password1","Admin","Name1", "lastname1");
        String expected = "different name";
        instance.setUserName("different name");
        String result = instance.getUserName();
        assertEquals(expected,result);
    }

    @Test
    void getFirstName() {
        System.out.println("Get first name");
        User instance =new User(1,"username1", "password1","Admin","Name1", "lastname1");
        String expected = "Name1";
        String result = instance.getFirstName();
        assertEquals(expected,result);
    }

    @Test
    void setFirstName() {
        System.out.println("Set first name");
        User instance =new User(1,"username1", "password1","Admin","Name1", "lastname1");
        String expected = "new name";
        instance.setFirstName("new name");
        String result = instance.getFirstName();
        assertEquals(expected,result);
    }

    @Test
    void getLastName() {
        System.out.println("Get last name");
        User instance =new User(1,"username1", "password1","Admin","Name1", "lastname1");
        String expected = "lastname1";
        String result = instance.getLastName();
        assertEquals(expected,result);
    }

    @Test
    void setLastName() {
        System.out.println("Set last name");
        User instance =new User(1,"username1", "password1","Admin","Name1", "lastname1");
        String expected = "new name";
        instance.setLastName("new name");
        String result = instance.getLastName();
        assertEquals(expected,result);
    }

    @Test
    void getAccess() {
        System.out.println("Get Access");
        User instance =new User(1,"username1", "password1","Admin","Name1", "lastname1");
        String expected = "Admin";
        String result = instance.getAccess();
        assertEquals(expected,result);
    }

    @Test
    void setAccess() {
        System.out.println("Set Access");
        User instance =new User(1,"username1", "password1","Admin","Name1", "lastname1");
        String expected = "Technician";
        instance.setAccess("Technician");
        String result = instance.getAccess();
        assertEquals(expected,result);
    }

    @Test
    void getPassword() {
        System.out.println("Get password");
        User instance =new User(1,"username1", "password1","Admin","Name1", "lastname1");
        String expected = "password1";
        String result = instance.getPassword();
        assertEquals(expected,result);
    }

    @Test
    void setPassword() {
        System.out.println("Set password");
        User instance =new User(1,"username1", "password1","Admin","Name1", "lastname1");
        String expected = "new password";
        instance.setPassword("new password");
        String result = instance.getPassword();
        assertEquals(expected,result);
    }

    @Test
    void testEquals() {
        System.out.println("Equals");
        User instance =new User(1,"username1", "password1","Admin","Name1", "lastname1");
        User instance2 =new User(1,"username1", "password1","Admin","Name1", "lastname1");
        assertEquals(instance2,instance);
    }
}