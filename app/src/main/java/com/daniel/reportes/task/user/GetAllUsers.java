package com.daniel.reportes.task.user;

import android.os.AsyncTask;

import com.daniel.reportes.data.User;
import com.daniel.reportes.task.API;
import com.dnieln7.httprequest.HttpSession;
import com.google.gson.Gson;

import java.util.Arrays;
import java.util.List;

public class GetAllUsers extends AsyncTask<Void, Void, List<User>> {

    private HttpSession session;

    public GetAllUsers(String role) {
        this.session = new HttpSession(API.MAIN + "user?role=" + role);
    }

    @Override
    protected List<User> doInBackground(Void... params) {
        return Arrays.asList(new Gson().fromJson(session.get(), User[].class));
    }
}
