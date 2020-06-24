package com.dnieln7.roadwatchman.task.reporte;

import android.os.AsyncTask;

import com.dnieln7.http.request.HttpSession;
import com.dnieln7.http.request.exception.ResponseException;
import com.dnieln7.roadwatchman.data.model.Reporte;
import com.dnieln7.roadwatchman.task.API;
import com.google.gson.Gson;

import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GetReporte extends AsyncTask<String, Void, Reporte> {

    private HttpSession session;

    public GetReporte() {
        this.session = new HttpSession(API.MAIN + "report");
    }

    @Override
    protected Reporte doInBackground(String... params) {
        try {
            return new Gson().fromJson(session.getById(params[0]), Reporte.class);
        }
        catch (ResponseException e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, String.format(Locale.ENGLISH, "%d %s", e.getCode(), e.getDescription()), e);
            return null;
        }
    }
}
