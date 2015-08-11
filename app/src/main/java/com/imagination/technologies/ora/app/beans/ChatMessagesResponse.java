package com.imagination.technologies.ora.app.beans;

import java.util.ArrayList;

public class ChatMessagesResponse {
    private boolean success;
    private ArrayList<Message> data;
    private Pagination pagination;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public ArrayList<Message> getData() {
        return data;
    }

    public void setData(ArrayList<Message> data) {
        this.data = data;
    }

    public Pagination getPagination() {
        return pagination;
    }

    public void setPagination(Pagination pagination) {
        this.pagination = pagination;
    }
}
