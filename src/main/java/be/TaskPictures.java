package be;

import javafx.scene.image.Image;

public class TaskPictures {

    private int pictureID;
    private Image picture;
    private int docID;
    private String deviceName;
    private String password;
    private String pictureAbsolute;


    public TaskPictures(int docID, String deviceName, String password, String pictureAbsolute) {
        this.docID = docID;
        this.deviceName = deviceName;
        this.password = password;
        this.pictureAbsolute = pictureAbsolute;
    }

    public TaskPictures (int pictureID, int docID, String deviceName, String password, String pictureAbsolute) {
        this.pictureID = pictureID;
        this.docID = docID;
        this.deviceName = deviceName;
        this.password = password;
        this.pictureAbsolute = pictureAbsolute;
    }

    public TaskPictures (int pictureID, int docID, String deviceName, String password, Image picture) {
        this.pictureID = pictureID;
        this.docID = docID;
        this.deviceName = deviceName;
        this.password = password;
        this.picture = picture;
    }




    public int getPictureID() {
        return pictureID;
    }

    public void setPictureID(int pictureID) {
        this.pictureID = pictureID;
    }

    public int getDocID() {
        return docID;
    }

    public void setDocID(int docID) {
        this.docID = docID;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPictureAbsolute() {
        return pictureAbsolute;
    }

    public void setPictureAbsolute(String pictureAbsolute) {
        this.pictureAbsolute = pictureAbsolute;
    }

    public Image getPicture() {
        return picture;
    }

    public void setPicture(Image picture) {
        this.picture = picture;
    }
}
