package com.daniel.reportes.task.user;

import android.os.AsyncTask;

import com.daniel.reportes.data.AppSession;
import com.daniel.reportes.data.User;
import com.daniel.reportes.task.API;
import com.daniel.reportes.task.TaskListener;
import com.dnieln7.httprequest.HttpSession;
import com.dnieln7.httprequest.exception.ResponseException;
import com.google.gson.Gson;

public class LoginUser extends AsyncTask<User, Void, TaskListener> {

    private HttpSession session;
    private TaskListener<AppSession> listener;

    public LoginUser(String type, TaskListener<AppSession> listener) {
        this.session = new HttpSession(API.MAIN + "login?type=" + type);
        this.listener = listener;
    }

    @Override
    protected TaskListener doInBackground(User... params) {
        try {
            listener.setResult(new Gson().fromJson(session.post(params[0]), AppSession.class));
        }
        catch (ResponseException e) {
            listener.setException(e);
        }

        return listener;
    }
}
