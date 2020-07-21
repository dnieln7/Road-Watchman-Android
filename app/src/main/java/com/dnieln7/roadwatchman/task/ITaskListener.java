package com.dnieln7.roadwatchman.task;

import java.util.List;

public interface ITaskListener<T> {

    public void onSuccess();

    public void onSuccess(List<T> input);
}
