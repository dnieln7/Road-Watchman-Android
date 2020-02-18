package com.daniel.reportes.data;

import java.io.Serializable;

public class AppSession implements Serializable {
    private String token;
    private int UserId;
    private User user;

    public AppSession(String token, int userId, User user) {
        this.token = token;
        UserId = userId;
        this.user = user;
    }

    public AppSession(String token, int userId) {
        this.token = token;
        UserId = userId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getUserId() {
        return UserId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
