package be;

import java.util.List;

public class UserWrapper {
    User user;
    int assignedTasks;
    List<Project> assignedProjectsList;

    public UserWrapper(User user, int assignedTasks, List<Project> assignedProjectsList) {
        this.user = user;
        this.assignedTasks = assignedTasks;
        this.assignedProjectsList=assignedProjectsList;
    }

    public User getUser() {
        return user;
    }

    public int getAssignedTasks() {
        return assignedTasks;
    }

    public List<Project> getAssignedProjectsList() {
        return assignedProjectsList;
    }
}
