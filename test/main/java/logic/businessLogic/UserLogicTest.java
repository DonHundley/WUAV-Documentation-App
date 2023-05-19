package main.java.logic.businessLogic;

import be.User;
import logic.businessLogic.UserLogic;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class UserLogicTest {
    UserLogic userLogic;

    @BeforeEach
    void setUp() {
        userLogic = new UserLogic();
    }

    @Test
    void  testLogInInformation () {
        List<User> users=new ArrayList<>();
        User user1=new User("username1", "password1","Admin","Name1", "lastname1");
        User user2 = new User("username2", "password2", "Admin", "Name2", "Lastname2");
        User user3 = new User("username3", "password3", "Admin", "Name2", "Lastname2");
         users.add(user1);
         users.add(user2);

        HashMap<Integer, Integer> expectedResult = new HashMap<>();
        expectedResult.put(user1.getUserName().hashCode(), user1.getPassword().hashCode());
        expectedResult.put(user2.getUserName().hashCode(), user2.getPassword().hashCode());

        HashMap<Integer, Integer> result = userLogic.loginInformation(users);

        assertEquals(expectedResult, result);
    }
}
