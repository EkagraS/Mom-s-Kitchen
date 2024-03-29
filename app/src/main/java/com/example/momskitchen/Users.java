package com.example.momskitchen;

public class Users {
    String mail, userName, password, userId;
    OrderData order;

    public Users(String userId, String userName, String mail, String password) {
        this.mail = mail;
        this.userName = userName;
        this.password = password;
        this.userId = userId;
    }

    public Users(String userId, String userName, String mail, String password, OrderData order) {
        this.mail = mail;
        this.userName = userName;
        this.password = password;
        this.userId = userId;
        this.order = order;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
