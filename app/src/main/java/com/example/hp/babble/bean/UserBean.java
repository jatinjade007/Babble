package com.example.hp.babble.bean;

/**
 * Created by HP on 4/27/2017.
 */

public class UserBean {
    private String name,userId;
   // private int userId;
    private String emailId;

    public UserBean(String name, String userId, String emailId) {
        this.name = name;
        this.userId = userId;
        this.emailId = emailId;
    }


    public String getUserId() {
        return userId;
    }

    public String getEmailId() {
        return emailId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }
}
