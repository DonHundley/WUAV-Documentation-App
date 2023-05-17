package be;

import javafx.geometry.Pos;

public class CustomerWrapper {
    Customer customer;
    Project project;
    PostalCode postalCode;

    public CustomerWrapper(Customer customer, Project project, PostalCode postalCode) {
        this.customer = customer;
        this.project = project;
        this.postalCode = postalCode;
    }

    public Customer getCustomer() {
        return customer;
    }

    public Project getProject() {
        return project;
    }

    public PostalCode getPostalCode() {
        return postalCode;
    }
}
