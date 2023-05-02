package bll;

import be.*;
import dal.*;

import java.util.*;

public class ProjectLogic {

    private ProjectDAO projectDAO = new ProjectDAO();
    public List<Project> getProjects() {
        return projectDAO.getAllProjects();
    }
}
