package com.imagination.technologies.ora.app.network;

public class Result {
    private int mResponseCode = -1;
    private int mServiceCode;
    private String mOutput;

    public int getServiceCode() {
        return mServiceCode;
    }

    public void setServiceCode(int mServiceCode) {
        this.mServiceCode = mServiceCode;
    }

    public int getResponseCode() {
        return mResponseCode;
    }

    public void setResponseCode(int mResponseCode) {
        this.mResponseCode = mResponseCode;
    }

    public String getOutput() {
        return mOutput;
    }

    public void setOutput(String mOutput) {
        this.mOutput = mOutput;
    }
}
