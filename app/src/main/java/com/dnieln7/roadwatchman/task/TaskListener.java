package com.dnieln7.roadwatchman.task;

import com.dnieln7.http.request.exception.ResponseException;

public abstract class TaskListener<T> {

    protected ResponseException exception;
    private T result;


    public void setException(ResponseException exception) {
        this.exception = exception;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
        this.exception = null;
    }

    public boolean success() {
        return this.exception == null;
    }
}