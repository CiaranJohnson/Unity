package com.example.ciaranjohnson.weunite.Model;

public class Help {

    private String displayName;
    private String description;
    private Double latitude;
    private Double longitude;
    private int offerCounter;

    public Help(String displayName, String description, Double latitude, Double longitude, int offerCounter) {
        this.displayName = displayName;
        this.description = description;
        this.latitude = latitude;
        this.longitude = longitude;
        this.offerCounter = offerCounter;
    }

    public Help() {
    }

    public int getOfferCounter() {
        return offerCounter;
    }

    public void setOfferCounter(int offerCounter) {
        this.offerCounter = offerCounter;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
}
