package com.example.momskitchen;

public class Users {
    String mail, userName, userId;
    OrderData order;

    public Users(String userId, String userName, String mail) {
        this.mail = mail;
        this.userName = userName;
        this.userId = userId;
    }

    public Users(){}

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
