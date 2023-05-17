package be;

public class Customer {

    private int custID;

    private String custName;

    private String custEmail;

    private String custAddress;

    private String postalCode;

    private String city;

    public Customer(int custID, String custName, String custEmail, String custAddress, String postalCode) {
        this.custID = custID;
        this.custName = custName;
        this.custEmail = custEmail;
        this.custAddress = custAddress;
        this.postalCode = postalCode;
    }

    public Customer(int custID, String custName, String custEmail, String custAddress, String postalCode, String city) {
        this.custID = custID;
        this.custName = custName;
        this.custEmail = custEmail;
        this.custAddress = custAddress;
        this.postalCode = postalCode;
        this.city = city;
    }

    public Customer(int custID, String custName, String custEmail, String custAddress) {
        this.custID = custID;
        this.custName = custName;
        this.custEmail = custEmail;
        this.custAddress = custAddress;
    }

    public Customer(String custName, String custEmail, String custAddress, String postalCode) {
        this.custName = custName;
        this.custEmail = custEmail;
        this.custAddress = custAddress;
        this.postalCode = postalCode;
    }

    public int getCustID() {
        return custID;
    }

    public void setCustID(int custID) {
        this.custID = custID;
    }

    public String getCustName() {
        return custName;
    }

    public void setCustName(String custName) {
        this.custName = custName;
    }

    public String getCustEmail() {
        return custEmail;
    }

    public void setCustEmail(String custEmail) {
        this.custEmail = custEmail;
    }

    public String getCustAddress() {
        return custAddress;
    }

    public void setCustAddress(String custAddress) {
        this.custAddress = custAddress;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "custID=" + custID +
                ", custName='" + custName + '\'' +
                ", custEmail='" + custEmail + '\'' +
                ", custAddress='" + custAddress + '\'' +
                ", postalCode='" + postalCode + '\'' +
                ", city='" + city + '\'' +
                '}';
    }
}
