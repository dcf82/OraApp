package com.imagination.technologies.ora.app.network;

import com.imagination.technologies.ora.app.controller.Config;
import com.imagination.technologies.ora.app.interfaces.NotificationTask;

import java.util.Map;

public class Service {
    private String mBaseURL = Config.BASE_URL;
    private String mServiceName = "";
    private String mOutput;

    private int mServiceType;
    private int mServiceCode;
    private int mResponseCode = -1;

    private Map mServiceInput;
    private Map mHeaders;
    private NotificationTask mNotificationTask;

    public Map getHeaders() {
        return mHeaders;
    }

    public void setHeaders(Map mHeaders) {
        this.mHeaders = mHeaders;
    }

    public void setServiceInput(Map mServiceInput) {
        this.mServiceInput = mServiceInput;
    }

    public Map getServiceInput() {
        return mServiceInput;
    }

    public void setServiceType(int mServiceType) {
        this.mServiceType = mServiceType;
    }

    public int getServiceType() {
        return mServiceType;
    }

    public void setServiceCode(int mServiceCode) {
        this.mServiceCode = mServiceCode;
    }

    public int getServiceCode() {
        return mServiceCode;
    }

    public void setNotificationTask(NotificationTask fragment2Service) {
        this.mNotificationTask = fragment2Service;
    }

    public NotificationTask getNotificationTask() {
        return mNotificationTask;
    }

    public String getOutput() {
        return mOutput;
    }

    public void setOutput(String mOutput) {
        this.mOutput = mOutput;
    }

    public String getBaseURL() {
        return mBaseURL;
    }

    public void setBaseURL(String mBaseURL) {
        this.mBaseURL = mBaseURL;
    }

    public void setServiceName(String mServiceName) {
        this.mServiceName = mServiceName;
    }

    public String getServiceName() {
        return mServiceName;
    }

    public int getResponseCode() {
        return mResponseCode;
    }

    public void setResponseCode(int mResponseCode) {
        this.mResponseCode = mResponseCode;
    }
}
