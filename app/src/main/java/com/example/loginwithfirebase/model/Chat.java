package com.example.loginwithfirebase.model;

public class Chat {

    private String sender;
    private String receiver;
    private String message;
    private String clickImage;
    private String reply;
    private String username;
    private String chatposition;
    private String chatpositionimage;
    private boolean isseen;
    private int replyposition;

    public Chat(String sender, String receiver, String message, String clickImage, String reply, String username, String chatposition, String chatpositionimage, boolean isseen, int replyposition) {
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
        this.clickImage = clickImage;
        this.reply = reply;
        this.username = username;
        this.chatposition = chatposition;
        this.chatpositionimage = chatpositionimage;
        this.isseen = isseen;
        this.replyposition = replyposition;
    }

    public Chat(){

     }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getClickImage() {
        return clickImage;
    }

    public void setClickImage(String clickImage) {
        this.clickImage = clickImage;
    }

    public String getReply() {
        return reply;
    }

    public void setReply(String reply) {
        this.reply = reply;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getChatposition() {
        return chatposition;
    }

    public void setChatposition(String chatposition) {
        this.chatposition = chatposition;
    }

    public String getChatpositionimage() {
        return chatpositionimage;
    }

    public void setChatpositionimage(String chatpositionimage) {
        this.chatpositionimage = chatpositionimage;
    }

    public boolean isIsseen() {
        return isseen;
    }

    public void setIsseen(boolean isseen) {
        this.isseen = isseen;
    }

    public int getReplyposition() {
        return replyposition;
    }

    public void setReplyposition(int replyposition) {
        this.replyposition = replyposition;
    }
}
