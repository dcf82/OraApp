package com.imagination.technologies.ora.app.beans;

public class ChatMessageResponse {
    private boolean success;
    private Message data;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Message getData() {
        return data;
    }

    public void setData(Message data) {
        this.data = data;
    }
}
