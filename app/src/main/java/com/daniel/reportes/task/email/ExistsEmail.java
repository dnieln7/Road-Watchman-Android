package com.daniel.reportes.task.email;

import android.os.AsyncTask;

import com.daniel.reportes.data.User;
import com.daniel.reportes.task.API;
import com.dnieln7.httprequest.HttpSession;

public class ExistsEmail extends AsyncTask<String, Void, Boolean> {

    private HttpSession session;

    public ExistsEmail() {
        this.session = new HttpSession(API.MAIN + "email");
    }

    @Override
    protected Boolean doInBackground(String... params) {
        return session.post(new User(params[0], "", "")).get("exist").getAsBoolean();
    }
}
