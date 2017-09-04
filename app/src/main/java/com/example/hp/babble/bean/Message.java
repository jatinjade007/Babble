package com.example.hp.babble.bean;

/**
 * Created by admin on 5/3/2017.
 */

public class Message {
    String name, messages;
    Boolean isLoggedInUser;

    public Message() {

    }

    public Message(String name, String messages, Boolean isLoggedInUser) {
        this.name = name;
        this.messages = messages;
        this.isLoggedInUser = isLoggedInUser;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMessages() {
        return messages;
    }

    public void setMessages(String messages) {
        this.messages = messages;
    }

    public Boolean getLoggedInUser() {
        return isLoggedInUser;
    }

    public void setLoggedInUser(Boolean loggedInUser) {
        isLoggedInUser = loggedInUser;
    }
}
