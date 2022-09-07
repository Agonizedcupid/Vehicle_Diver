package com.regin.reginald.vehicleanddrivers.Aariyan.Model;

public class DummyLocations {
    private double lat, lng;
    String title;

    public DummyLocations() {
    }

    public DummyLocations(double lat, double lng, String title) {
        this.lat = lat;
        this.lng = lng;
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }
}
