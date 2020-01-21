package com.daniel.reportes.task.user;

import android.os.AsyncTask;

import com.daniel.reportes.data.User;
import com.daniel.reportes.task.API;
import com.dnieln7.httprequest.HttpSession;
import com.google.gson.Gson;

public class PutUser extends AsyncTask<User, Void, User> {

    private HttpSession session;

    public PutUser(String id) {
        this.session = new HttpSession(API.MAIN + "user/" + id);
    }

    @Override
    protected User doInBackground(User... params) {
        return new Gson().fromJson(session.put(params[0]), User.class);
    }
}
