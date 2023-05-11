package com.WUAV.be;

public class TaskWrapper {
    Project project;
    Task task;
    Customer customer;

    public TaskWrapper(Project project, Task task, Customer customer) {
        this.project = project;
        this.task = task;
        this.customer = customer;
    }

    public Project getProject() {
        return project;
    }

    public Task getTask() {
        return task;
    }

    public Customer getCustomer() {
        return customer;
    }
}
