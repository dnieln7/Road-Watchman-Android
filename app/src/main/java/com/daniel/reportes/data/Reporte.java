package com.daniel.reportes.data;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "reportes")
public class Reporte {

    @PrimaryKey
    private int id;
    @ColumnInfo(name = "description")
    private String description;
    @ColumnInfo(name = "date")
    private String date;
    @ColumnInfo(name = "location")
    private double[] location;
    @ColumnInfo(name = "picture")
    private String picture;
    @ColumnInfo(name = "UserId")
    private int UserId;

    public Reporte() {
    }

    @Ignore
    public Reporte(String date, double[] location, int UserId) {
        this.description = "";
        this.date = date;
        this.location = location;
        this.picture = "";
        this.UserId = UserId;
    }

    @Ignore
    public Reporte(String description, String date, double[] location, int userId) {
        this.description = description;
        this.date = date;
        this.location = location;
        this.picture = "";
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

    public double[] getLocation() {
        return location;
    }

    public void setLocation(double[] location) {
        this.location = location;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public int getUserId() {
        return UserId;
    }

    public void setUserId(int userId) {
        UserId = userId;
    }
}
