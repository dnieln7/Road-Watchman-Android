package com.dnieln7.roadwatchman.task;

import java.util.List;

public interface ITaskListener<T> {

    default public void onSuccess() {
        throw new UnsupportedOperationException();
    }

    default public void onFail() {
        throw new UnsupportedOperationException();
    }

    default public void onSuccess(List<T> list) {
        throw new UnsupportedOperationException();
    }

    default public void onSuccess(T object) {
        throw new UnsupportedOperationException();
    }
}
