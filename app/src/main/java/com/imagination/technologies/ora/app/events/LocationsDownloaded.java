package com.imagination.technologies.ora.app.events;

public class LocationsDownloaded {
    private String input;

    public LocationsDownloaded(String input) {
        this.input = input;
    }

    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    }
}
