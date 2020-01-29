package com.daniel.reportes.task.reporte;

import android.os.AsyncTask;

import com.daniel.reportes.data.Reporte;
import com.daniel.reportes.task.API;
import com.dnieln7.httprequest.HttpSession;
import com.dnieln7.httprequest.exception.ResponseException;
import com.google.gson.Gson;

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
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, e.getCode() + " " + e.getDescription(), e);
            return null;
        }
    }
}
