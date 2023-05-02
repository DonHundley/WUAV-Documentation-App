package bll;

import be.*;
import dal.*;

import java.util.*;

public class ProjectLogic {

    private ProjectDAO projectDAO = new ProjectDAO();
    private TaskDAO taskDAO = new TaskDAO();
    public List<Project> getProjects() {
        return projectDAO.getAllProjects();
    }

    public List<Task> getTasksByProject(Project selectedProject) {return taskDAO.getTaskByProject(selectedProject.getProjID());}
}
