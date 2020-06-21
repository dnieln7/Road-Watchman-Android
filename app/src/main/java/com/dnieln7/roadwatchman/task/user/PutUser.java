package com.dnieln7.roadwatchman.task.user;

import android.os.AsyncTask;

import com.dnieln7.roadwatchman.data.User;
import com.dnieln7.roadwatchman.task.API;
import com.dnieln7.roadwatchman.task.TaskListener;
import com.dnieln7.http.request.HttpSession;
import com.dnieln7.http.request.exception.ResponseException;
import com.google.gson.Gson;

public class PutUser extends AsyncTask<User, Void, TaskListener> {

    private HttpSession session;
    private TaskListener<User> listener;

    public PutUser(String id, TaskListener<User> listener) {
        this.session = new HttpSession(API.MAIN + "user/" + id);
        this.listener = listener;
    }

    @Override
    protected TaskListener<User> doInBackground(User... params) {

        try {
            listener.setResult(new Gson().fromJson(session.put(params[0]), User.class));
        }
        catch (ResponseException e) {
            listener.setException(e);
        }

        return listener;
    }
}
