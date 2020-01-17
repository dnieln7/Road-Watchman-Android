package com.daniel.reportes.task.user;

import android.os.AsyncTask;

import com.daniel.reportes.data.User;
import com.daniel.reportes.task.API;
import com.dnieln7.httprequest.HttpSession;
import com.dnieln7.httprequest.response.ErrorResponse;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class PostUser extends AsyncTask<User, Void, Object> {

    private HttpSession session;

    public PostUser() {
        this.session = new HttpSession(API.MAIN + "user");
    }

    @Override
    protected Object doInBackground(User... params) {

        JsonObject json = session.post(params[0]);

        if(json.has("ERROR_CODE")) {
            return new Gson().fromJson(json, ErrorResponse.class);
        }
        else {
            return new Gson().fromJson(json, User.class);
        }
    }
}
