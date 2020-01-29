package com.daniel.reportes.task;

import com.dnieln7.httprequest.exception.ResponseException;

public abstract class TaskListener<T> {

    protected ResponseException exception;
    private T result;


    public void setException(ResponseException exception) {
        this.exception = exception;
    }

    public void setResult(T result) {
        this.result = result;
        this.exception = null;
    }

    public T getResult() {
        return result;
    }

    public boolean success() {
        return this.exception == null;
    }
}
