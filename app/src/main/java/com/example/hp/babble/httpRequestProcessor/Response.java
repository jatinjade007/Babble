package com.example.hp.babble.httpRequestProcessor;

/**
 * Created by HP on 4/21/2017.
 */

public class Response {
    String jsonResponseString;
    int responseCode;

    public String getJsonResponseString() {
        return jsonResponseString;
    }

    public void setJsonResponseString(String jsonResponseString) {
        this.jsonResponseString = jsonResponseString;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }
}
