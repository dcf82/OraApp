package com.imagination.technologies.ora.app.beans;

public class RegistrationResponse {
    private boolean success;
    private UserData data;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public UserData getData() {
        return data;
    }

    public void setData(UserData data) {
        this.data = data;
    }

}
