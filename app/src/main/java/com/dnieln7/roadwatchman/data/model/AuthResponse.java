package com.dnieln7.roadwatchman.data.model;

import org.jetbrains.annotations.NotNull;

public class AuthResponse {
    private int code;
    private String message;
    private User result;

    public AuthResponse() {
    }

    public AuthResponse(int code, String message, User result) {
        this.code = code;
        this.message = message;
        this.result = result;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public User getResult() {
        return result;
    }

    public void setResult(User result) {
        this.result = result;
    }

    @NotNull
    @Override
    public String toString() {
        return "AuthResponse{" +
                "code=" + code +
                ", message='" + message + '\'' +
                ", result=" + result +
                '}';
    }
}
