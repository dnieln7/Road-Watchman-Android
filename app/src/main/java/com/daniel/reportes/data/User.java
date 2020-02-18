package com.daniel.reportes.data;

import java.io.Serializable;

public class User implements Serializable {
    private int id;
    private String username;
    private String email;
    private String password;
    private String googleId;
    private String role;

    public User(int id, String username, String email, String googleId, String role) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = "";
        this.googleId = googleId;
        this.role = role;
    }

    public User(String username, String email, String password, String googleId, String role) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.googleId = googleId;
        this.role = role;
    }

    public User(String email, String password, String role) {
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGoogleId() {
        return googleId;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
