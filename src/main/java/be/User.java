package be;


import java.util.*;

public class User {

    private int userID;
    private String userName;
    private String firstName;
    private String lastName;
    private String access;
    private String password;


    public User(int userID, String userName, String password, String access, String firstName, String lastName) {
        this.userID = userID;
        this.userName = userName;
        this.password = password;
        this.access = access;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public User(String userName, String password, String access, String firstName, String lastName) {
        this.userName = userName;
        this.password = password;
        this.access = access;
        this.firstName = firstName;
        this.lastName = lastName;
    }


    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAccess() {
        return access;
    }

    public void setAccess(String access) {
        this.access = access;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (userID != user.userID) return false;
        if (!Objects.equals(userName, user.userName)) return false;
        if (!Objects.equals(firstName, user.firstName)) return false;
        if (!Objects.equals(lastName, user.lastName)) return false;
        if (!Objects.equals(access, user.access)) return false;
        return Objects.equals(password, user.password);
    }

    @Override
    public int hashCode() {
        int result = userID;
        result = 31 * result + (userName != null ? userName.hashCode() : 0);
        result = 31 * result + (firstName != null ? firstName.hashCode() : 0);
        result = 31 * result + (lastName != null ? lastName.hashCode() : 0);
        result = 31 * result + (access != null ? access.hashCode() : 0);
        result = 31 * result + (password != null ? password.hashCode() : 0);
        return result;
    }
}
