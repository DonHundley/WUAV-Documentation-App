package be;

import javafx.scene.image.Image;

public class Task {

    private int docID;

    private int projID;

    private String taskName;

    private Image taskLayout;

    private String taskDesc;

    private int taskState;

    public Task(int docID, int projID, String taskName, Image taskLayout, String taskDesc, int taskState) {
        this.docID = docID;
        this.projID = projID;
        this.taskName = taskName;
        this.taskLayout = taskLayout;
        this.taskDesc = taskDesc;
        this.taskState = taskState;
    }

    public Task(int projID, String taskName, Image taskLayout, String taskDesc, int taskState) {
        this.projID = projID;
        this.taskName = taskName;
        this.taskLayout = taskLayout;
        this.taskDesc = taskDesc;
        this.taskState = taskState;
    }

    public int getDocID() {
        return docID;
    }

    public void setDocID(int docID) {
        this.docID = docID;
    }

    public int getProjID() {
        return projID;
    }

    public void setProjID(int projID) {
        this.projID = projID;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public Image getTaskLayout() {
        return taskLayout;
    }

    public void setTaskLayout(Image taskLayout) {
        this.taskLayout = taskLayout;
    }

    public String getTaskDesc() {
        return taskDesc;
    }

    public void setTaskDesc(String taskDesc) {
        this.taskDesc = taskDesc;
    }

    public int getTaskState() {
        return taskState;
    }

    public void setTaskState(int taskState) {
        this.taskState = taskState;
    }
}
