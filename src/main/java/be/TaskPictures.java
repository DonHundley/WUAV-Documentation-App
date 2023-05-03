package be;

import javafx.scene.image.Image;

public class TaskPictures {

    private int pictureID;

    private Image beforePicture;

    private Image afterPicture;

    private int docID;

    private String afterComment;

    private String beforeComment;

    public TaskPictures(int pictureID, Image afterPicture, Image beforePicture, String afterComment, int docID, String beforeComment) {
        this.pictureID = pictureID;
        this.beforePicture = beforePicture;
        this.afterPicture = afterPicture;
        this.docID = docID;
        this.afterComment = afterComment;
        this.beforeComment = beforeComment;
    }

    public TaskPictures(Image afterPicture, Image beforePicture, String afterComment, int docID, String beforeComment) {
        this.beforePicture = beforePicture;
        this.afterPicture = afterPicture;
        this.docID = docID;
        this.afterComment = afterComment;
        this.beforeComment = beforeComment;
    }

    public int getPictureID() {
        return pictureID;
    }

    public void setPictureID(int pictureID) {
        this.pictureID = pictureID;
    }

    public Image getBeforePicture() {
        return beforePicture;
    }

    public void setBeforePicture(Image beforePicture) {
        this.beforePicture = beforePicture;
    }

    public Image getAfterPicture() {
        return afterPicture;
    }

    public void setAfterPicture(Image afterPicture) {
        this.afterPicture = afterPicture;
    }

    public int getDocID() {
        return docID;
    }

    public void setDocID(int docID) {
        this.docID = docID;
    }

    public String getAfterComment() {
        return afterComment;
    }

    public void setAfterComment(String afterComment) {
        this.afterComment = afterComment;
    }

    public String getBeforeComment() {
        return beforeComment;
    }

    public void setBeforeComment(String beforeComment) {
        this.beforeComment = beforeComment;
    }
}
