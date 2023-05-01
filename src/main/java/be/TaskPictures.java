package be;

import javafx.scene.image.Image;

public class TaskPictures {

    private int pictureID;

    private Image beforePicture;

    private Image afterPicture;

    private int docID;

    private String pictureComment;

    public TaskPictures(int pictureID, Image afterPicture, Image beforePicture, String pictureComment, int docID) {
        this.pictureID = pictureID;
        this.beforePicture = beforePicture;
        this.afterPicture = afterPicture;
        this.docID = docID;
        this.pictureComment = pictureComment;
    }

    public TaskPictures(Image afterPicture, Image beforePicture, String pictureComment, int docID) {
        this.beforePicture = beforePicture;
        this.afterPicture = afterPicture;
        this.docID = docID;
        this.pictureComment = pictureComment;
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

    public String getPictureComment() {
        return pictureComment;
    }

    public void setPictureComment(String pictureComment) {
        this.pictureComment = pictureComment;
    }
}
