package com.WUAV.be;

public class CustomerWrapper {
    Customer customer;
    Project project;

    public CustomerWrapper(Customer customer, Project project) {
        this.customer = customer;
        this.project = project;
    }

    public Customer getCustomer() {
        return customer;
    }

    public Project getProject() {
        return project;
    }
}
