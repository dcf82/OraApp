package com.imagination.technologies.ora.app.events;

public class LocationsProcessed {
    public  enum RESPONSE {SUCCESSFUL, FAILURE};
    private RESPONSE response;

    public LocationsProcessed(RESPONSE response) {
        this.response = response;
    }

    public RESPONSE getResponse() {
        return response;
    }

    public void setResponse(RESPONSE response) {
        this.response = response;
    }
}
