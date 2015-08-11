package com.imagination.technologies.ora.app.events;

public class Message {
    public enum OPERATION {
        LOCATIONS,
    }

    private String response;
    private OPERATION operationCode;
    private int responseCode;

    public OPERATION getOperationCode() {
        return operationCode;
    }

    public void setOperationCode(OPERATION operationCode) {
        this.operationCode = operationCode;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }
}
