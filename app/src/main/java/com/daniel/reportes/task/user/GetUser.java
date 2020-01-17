package com.daniel.reportes.task.user;

import android.os.AsyncTask;

import com.daniel.reportes.data.User;
import com.daniel.reportes.task.API;
import com.dnieln7.httprequest.HttpSession;
import com.google.gson.Gson;

public class GetUser extends AsyncTask<String, Void, User> {

    private HttpSession session;

    public GetUser() {
        this.session = new HttpSession(API.MAIN + "user");
    }

    @Override
    protected User doInBackground(String... params) {
        return new Gson().fromJson(session.getById(params[0]), User.class);
    }
}
