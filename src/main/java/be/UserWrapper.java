package be;

import java.util.Date;

public class UserWrapper {
    User user;
    int assignedTasks;

    public UserWrapper(User user, int assignedTasks) {
        this.user = user;
        this.assignedTasks = assignedTasks;
    }

    public User getUser() {
        return user;
    }

    public int getAssignedTasks() {
        return assignedTasks;
    }


}
