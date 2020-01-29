package com.daniel.reportes.task.email;

import android.os.AsyncTask;

import com.daniel.reportes.data.User;
import com.daniel.reportes.task.API;
import com.dnieln7.httprequest.HttpSession;
import com.dnieln7.httprequest.exception.ResponseException;

import java.util.logging.Level;
import java.util.logging.Logger;

public class ExistsEmail extends AsyncTask<String, Void, Boolean> {

    private HttpSession session;

    public ExistsEmail() {
        this.session = new HttpSession(API.MAIN + "email");
    }

    @Override
    protected Boolean doInBackground(String... params) {
        try {
            return session.post(new User(params[0], "", "")).get("exist").getAsBoolean();
        }
        catch (ResponseException e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, e.getCode() + " " + e.getDescription(), e);
            return true;
        }
    }
}
