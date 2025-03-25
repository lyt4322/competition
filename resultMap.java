package com.example.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class resultMap {
    private Integer id;
    private String name;
    private String description;
    private double distance;
    private String img;
    private String lon;
    private String lat;






    public String getLat() {
        return lat;
    }
    public String getLon() {
        return lon;
    }
    public void setLon(String lon) {
        this.lon = lon;
    }
    public void setLat(String lat) {
        this.lat = lat;
    }
    public double getDistance() {
        return distance;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getImg() {
        return img;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setImg(String img) {
        this.img = img;
    }
    @Override
    public String toString(){
        return "id:"+id+"{name:"+name+",description:"+description+",distance:"+distance+",img:"+img+",lon:"+lon+",lat"+lat+"}";
    }
}
