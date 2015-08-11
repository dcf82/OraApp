package com.imagination.technologies.ora.app.beans;

public class AddNewChatRoom {
    private boolean success;
    private RoomChat data;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public RoomChat getData() {
        return data;
    }

    public void setData(RoomChat data) {
        this.data = data;
    }
}
