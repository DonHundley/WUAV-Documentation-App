package be;

import java.util.*;

public class Customer {

    private int custID;

    private String custName;

    private String custEmail;

    private String custAddress;

    private String postalCode;

    private String city;

    public Customer() {} // Blank constructor, used for testing purposes.

    // Constructor with customer ID.
    public Customer(int custID, String custName, String custEmail, String custAddress, String postalCode, String city) {
        this.custID = custID;
        this.custName = custName;
        this.custEmail = custEmail;
        this.custAddress = custAddress;
        this.postalCode = postalCode;
        this.city = city;
    }

    // Constructor without Customer ID, for new customer creation.
    public Customer(String custName, String custEmail, String custAddress, String postalCode, String city) {
        this.custName = custName;
        this.custEmail = custEmail;
        this.custAddress = custAddress;
        this.postalCode = postalCode;
        this.city = city;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Customer customer = (Customer) o;

        if (custID != customer.custID) return false;
        if (!Objects.equals(custName, customer.custName)) return false;
        if (!Objects.equals(custEmail, customer.custEmail)) return false;
        if (!Objects.equals(custAddress, customer.custAddress))
            return false;
        if (!Objects.equals(postalCode, customer.postalCode)) return false;
        return Objects.equals(city, customer.city);
    }

    @Override
    public int hashCode() {
        int result = custID;
        result = 31 * result + (custName != null ? custName.hashCode() : 0);
        result = 31 * result + (custEmail != null ? custEmail.hashCode() : 0);
        result = 31 * result + (custAddress != null ? custAddress.hashCode() : 0);
        result = 31 * result + (postalCode != null ? postalCode.hashCode() : 0);
        result = 31 * result + (city != null ? city.hashCode() : 0);
        return result;
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
