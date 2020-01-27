package com.daniel.reportes.task.reporte;

import android.os.AsyncTask;

import com.daniel.reportes.data.Reporte;
import com.daniel.reportes.task.API;
import com.dnieln7.httprequest.HttpSession;
import com.google.gson.Gson;

public class GetReporte extends AsyncTask<String, Void, Reporte> {

    private HttpSession session;

    public GetReporte() {
        this.session = new HttpSession(API.MAIN + "report");
    }

    @Override
    protected Reporte doInBackground(String... params) {
        return new Gson().fromJson(session.getById(params[0]), Reporte.class);
    }
}
