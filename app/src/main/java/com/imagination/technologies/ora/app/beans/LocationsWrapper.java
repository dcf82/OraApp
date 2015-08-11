package com.imagination.technologies.ora.app.beans;

import java.util.ArrayList;

public class LocationsWrapper {

    public ArrayList<MyLocation> getMyLocations() {
        return result;
    }

    public void setMyLocations(ArrayList<MyLocation> myLocations) {
        this.result = myLocations;
    }

    ArrayList<MyLocation> result;
}
