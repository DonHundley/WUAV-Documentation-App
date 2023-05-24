package be;



public class ProjectWrapper {
    Project project;
    int totalTasks;

    public ProjectWrapper(Project project, int totalTasks) {
        this.project = project;
        this.totalTasks = totalTasks;
    }

    public Project getProject() {
        return project;
    }

    public int getTotalTasks() {
        return totalTasks;
    }
}



