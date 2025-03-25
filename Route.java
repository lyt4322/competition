package com.example.pojo;

public class Route {
    private String startLon;
    private String startLat;
    private String endLat;

    private String endLon;
    private String distance;
    public void setStartLon(String lon) {
        this.startLon =lon;
    }
    public String getStartLon() {
        return startLon;
    }
    public void setStartLat(String lat) {
        this.startLat = lat;
    }
    public String getStartLat() {
        return startLat;
    }
    public void setEndLat(String lat) {
        this.endLat = lat;
    }
    public String getEndLat() {
        return endLat;
    }
    public void setEndLon(String lon) {
        this.endLon = lon;
    }
    public String getEndLon() {
        return endLon;
    }

    public void setdistance(String distance) {
        this.distance = distance;
    }
    public Route() {
    }

    public String getdistance(String distance) {
        return distance;
    }



}
