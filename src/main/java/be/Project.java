package be;

import java.util.Date;

public class Project {

    private int projID;

    private String projName;

    private Date projDate;

    private int custID;

    public Project(int projID, String projName, Date projDate, int custID) {
        this.projID = projID;
        this.projName = projName;
        this.projDate = projDate;
        this.custID = custID;
    }

    public Project(String projName, Date projDate, int custID) {
        this.projName = projName;
        this.projDate = projDate;
        this.custID = custID;
    }

    public int getProjID() {
        return projID;
    }

    public void setProjID(int projID) {
        this.projID = projID;
    }

    public String getProjName() {
        return projName;
    }

    public void setProjName(String projName) {
        this.projName = projName;
    }

    public Date getProjDate() {
        return projDate;
    }

    public void setProjDate(Date projDate) {
        this.projDate = projDate;
    }

    public int getCustID() {
        return custID;
    }

    public void setCustID(int custID) {
        this.custID = custID;
    }

    @Override
    public String toString() {
        return "Project{" +
                "projID=" + projID +
                ", projName='" + projName + '\'' +
                ", projDate=" + projDate +
                ", custID=" + custID +
                '}';
    }
}
