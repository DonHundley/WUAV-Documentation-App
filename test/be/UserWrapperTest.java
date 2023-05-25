package be;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

class UserWrapperTest {

    @Test
    void getUser() {
        System.out.println("Get user");
        User instance =new User(1,"username1", "password1","Admin","Name1", "lastname1");
        UserWrapper userWrapper = new UserWrapper(instance, 1);
        User result = userWrapper.getUser();
        assertEquals(instance,result);
    }

    @Test
    void getAssignedTasks() {
        System.out.println("Get assigned tasks");
        User instance =new User(1,"username1", "password1","Admin","Name1", "lastname1");
        UserWrapper userWrapper = new UserWrapper(instance, 1);
        int expected = 1;
        int result = userWrapper.getAssignedTasks();
        assertEquals(expected, result);
    }
}