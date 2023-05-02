package bll;

import be.*;
import dal.*;

import java.util.*;

public class UserLogic {

    private UserDAO userDAO = new UserDAO();
    public List<User> getUsers() {
        return userDAO.getAllUsers();
    }

    public List<User> getTechs() {
        return userDAO.getTechnicians();
    }
}
