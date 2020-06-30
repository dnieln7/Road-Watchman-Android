package com.dnieln7.roadwatchman.data.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "reportes")
public class Reporte implements Serializable {

    @PrimaryKey
    private int id;
    @ColumnInfo(name = "description")
    private String description;
    @ColumnInfo(name = "date")
    private String date;
    @ColumnInfo(name = "fixed")
    private boolean fixed;
    @ColumnInfo(name = "picture")
    private String picture;
    @ColumnInfo(name = "location")
    private double[] location;
    @ColumnInfo(name = "location_description")
    private String location_description;
    @ColumnInfo(name = "UserId")
    private int UserId;

    public Reporte() {
    }

    @Ignore
    public Reporte(String date, double[] location, int userId) {
        this.description = "";
        this.date = date;
        this.fixed = false;
        this.picture = "";
        this.location = location;
        this.location_description = "";
        this.UserId = userId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public boolean isFixed() {
        return fixed;
    }

    public void setFixed(boolean fixed) {
        this.fixed = fixed;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public double[] getLocation() {
        return location;
    }

    public void setLocation(double[] location) {
        this.location = location;
    }

    public String getLocation_description() {
        return location_description;
    }

    public void setLocation_description(String location_description) {
        this.location_description = location_description;
    }

    public int getUserId() {
        return UserId;
    }

    public void setUserId(int userId) {
        UserId = userId;
    }
}
