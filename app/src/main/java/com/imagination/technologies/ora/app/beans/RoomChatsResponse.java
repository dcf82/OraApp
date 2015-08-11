package com.imagination.technologies.ora.app.beans;

import java.util.ArrayList;

public class RoomChatsResponse {
    private boolean success;
    private ArrayList<RoomChat> data;
    private Pagination pagination;

    public Pagination getPagination() {
        return pagination;
    }

    public void setPagination(Pagination pagination) {
        this.pagination = pagination;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public ArrayList<RoomChat> getData() {
        return data;
    }

    public void setData(ArrayList<RoomChat> data) {
        this.data = data;
    }
}
