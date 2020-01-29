package com.daniel.reportes.task.user;

import android.os.AsyncTask;

import com.daniel.reportes.data.User;
import com.daniel.reportes.task.API;
import com.dnieln7.httprequest.HttpSession;
import com.dnieln7.httprequest.exception.ResponseException;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GetAllUsers extends AsyncTask<Void, Void, List<User>> {

    private HttpSession session;

    public GetAllUsers(String role) {
        this.session = new HttpSession(API.MAIN + "user?role=" + role);
    }

    @Override
    protected List<User> doInBackground(Void... params) {
        try {
            return Arrays.asList(new Gson().fromJson(session.get(), User[].class));
        }
        catch (ResponseException e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, e.getCode() + " " + e.getDescription(), e);
            return new ArrayList<>();
        }
    }
}
