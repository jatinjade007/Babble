package com.example.hp.babble.bean;

/**
 * Created by HP on 4/9/2017.
 */

public class ChatRow {
    private int image;
    private String friendName,message,friendId;

    public ChatRow( String friendName,String friendId) {
        this.friendId=friendId;

        this.friendName = friendName;
    }

    public ChatRow(int image, String friendName, String message) {
        this.image = image;
        this.friendName = friendName;
        this.message = message;
    }

    public String getFriendId() {
        return friendId;
    }

    public int getImage() {
        return image;
    }

    public String getFriendName() {
        return friendName;
    }

    public String getMessage() {
        return message;
    }
}
