package com.daniel.reportes.task.user;

import android.os.AsyncTask;

import com.daniel.reportes.data.User;
import com.daniel.reportes.task.API;
import com.daniel.reportes.task.TaskListener;
import com.dnieln7.http.request.HttpSession;
import com.dnieln7.http.request.exception.ResponseException;
import com.google.gson.Gson;

public class PostUser extends AsyncTask<User, Void, TaskListener> {

    private HttpSession session;
    private TaskListener<User> listener;

    public PostUser(TaskListener<User> listener) {
        this.session = new HttpSession(API.MAIN + "user");
        this.listener = listener;
    }

    @Override
    protected TaskListener<User> doInBackground(User... params) {

        try {
            listener.setResult(new Gson().fromJson(session.post(params[0]), User.class));
        }
        catch (ResponseException e) {
            listener.setException(e);
        }

        return listener;
    }
}
