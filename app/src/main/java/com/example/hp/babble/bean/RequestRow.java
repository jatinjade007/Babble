package com.example.hp.babble.bean;

/**
 * Created by HP on 4/10/2017.
 */

public class RequestRow {

    private String friendName;


    public RequestRow( String friendName) {

        this.friendName = friendName;
    }

    public void setFriendName(String friendName) {
        this.friendName = friendName;
    }


    public String getFriendName() {
        return friendName;
    }
}
