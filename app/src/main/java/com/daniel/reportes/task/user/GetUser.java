package com.daniel.reportes.task.user;

import android.os.AsyncTask;

import com.daniel.reportes.data.User;
import com.daniel.reportes.task.API;
import com.dnieln7.httprequest.HttpSession;
import com.dnieln7.httprequest.exception.ResponseException;
import com.google.gson.Gson;

import java.util.logging.Level;
import java.util.logging.Logger;

public class GetUser extends AsyncTask<String, Void, User> {

    private HttpSession session;

    public GetUser() {
        this.session = new HttpSession(API.MAIN + "user");
    }

    @Override
    protected User doInBackground(String... params) {
        try {
            return new Gson().fromJson(session.getById(params[0]), User.class);
        }
        catch (ResponseException e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, e.getCode() + " " + e.getDescription(), e);
            return null;
        }
    }
}
