package com.WUAV.be;

public class Customer {

    private int custID;

    private String custName;

    private String custEmail;

    private String custAddress;

    public Customer(int custID, String custName, String custEmail, String custAddress) {
        this.custID = custID;
        this.custName = custName;
        this.custEmail = custEmail;
        this.custAddress = custAddress;
    }

    public Customer(String custName, String custEmail, String custAddress) {
        this.custName = custName;
        this.custEmail = custEmail;
        this.custAddress = custAddress;
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
}
