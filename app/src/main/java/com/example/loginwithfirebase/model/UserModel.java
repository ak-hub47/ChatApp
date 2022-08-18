package com.example.loginwithfirebase.model;

public class UserModel {

    private String id;
    private String username;
    private String imageURL;
    private String memberId;
    private String status;

    public UserModel(String id, String username, String imageURL, String memberId, String status) {
        this.id = id;
        this.username = username;
        this.imageURL = imageURL;
        this.memberId = memberId;
        this.status = status;
    }

    public UserModel() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
