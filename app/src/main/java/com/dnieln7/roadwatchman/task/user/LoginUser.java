package com.dnieln7.roadwatchman.task.user;

import android.os.AsyncTask;

import com.dnieln7.roadwatchman.data.model.AppSession;
import com.dnieln7.roadwatchman.data.model.User;
import com.dnieln7.roadwatchman.task.API;
import com.dnieln7.roadwatchman.task.TaskListener;
import com.dnieln7.http.request.HttpSession;
import com.dnieln7.http.request.exception.ResponseException;
import com.google.gson.Gson;

public class LoginUser extends AsyncTask<User, Void, TaskListener<AppSession>> {

    private HttpSession session;
    private TaskListener<AppSession> listener;

    public LoginUser(String type, TaskListener<AppSession> listener) {
        this.session = new HttpSession(API.MAIN + "login?type=" + type);
        this.listener = listener;
    }

    @Override
    protected TaskListener<AppSession> doInBackground(User... params) {
        try {
            listener.setResult(new Gson().fromJson(session.post(params[0]), AppSession.class));
        }
        catch (ResponseException e) {
            listener.setException(e);
        }

        return listener;
    }
}
