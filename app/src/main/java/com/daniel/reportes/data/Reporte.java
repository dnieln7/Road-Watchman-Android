package com.daniel.reportes.data;

public class Reporte {
    private int id;
    private String description;
    private String date;
    private double[] location;
    private String picture;
    private int UserId;

    public Reporte(String description, String date, double[] location, String picture, int userId) {
        this.description = description;
        this.date = date;
        this.location = location;
        this.picture = picture;
        this.UserId = userId;
    }

    public Reporte(String date, double[] location) {
        this.description = "";
        this.date = date;
        this.location = location;
        this.picture = "";
        this.UserId = 0;
    }

    public Reporte(String description, String date, double[] location, int userId) {
        this.description = description;
        this.date = date;
        this.location = location;
        this.UserId = userId;
    }

    public int getId() {
        return id;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public String getDate() {
        return date;
    }

    public double[] getLocation() {
        return location;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getPicture() {
        return picture;
    }

    public void setUserId(int userId) {
        UserId = userId;
    }

    public int getUserId() {
        return UserId;
    }
}
