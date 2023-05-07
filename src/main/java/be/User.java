package be;


import java.util.ArrayList;
import java.util.List;

public class User {

    private int userID;

    private String userName;

    private String firstName;

    private String lastName;

    private String access;

    private String password;

    private List<Project> worksOn;

    public User(int userID, String userName, String password, String access, String firstName, String lastName, List<Project> worksOn) {
        this.userID = userID;
        this.userName = userName;
        this.password = password;
        this.access = access;
        this.firstName = firstName;
        this.lastName = lastName;
        this.worksOn = new ArrayList<>();
    }

    public User(int userID, String userName, String password, String access, String firstName, String lastName) {
        this.userID = userID;
        this.userName = userName;
        this.password = password;
        this.access = access;
        this.firstName = firstName;
        this.lastName = lastName;
        this.worksOn = new ArrayList<>();
    }

    public User(String userName, String password, String access, String firstName, String lastName, List<Project> worksOn) {
        this.userName = userName;
        this.password = password;
        this.access = access;
        this.firstName = firstName;
        this.lastName = lastName;
        this.worksOn = new ArrayList<>();
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
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    /*public List<String> getWorksOn() {
        return worksOn;
    }

    public void setWorksOn(List<String> worksOn) {
        this.worksOn = worksOn;
    }*/
}
