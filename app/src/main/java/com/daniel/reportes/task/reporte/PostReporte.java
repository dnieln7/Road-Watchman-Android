package com.daniel.reportes.task.reporte;

import android.os.AsyncTask;

import com.daniel.reportes.data.Reporte;
import com.daniel.reportes.task.API;
import com.dnieln7.httprequest.HttpSession;
import com.dnieln7.httprequest.response.ErrorResponse;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class PostReporte extends AsyncTask<Reporte, Integer, Object> {

    private HttpSession session;
    private String pictureUrl;

    public PostReporte(String pictureUrl) {
        this.session = new HttpSession(API.MAIN + "report");;
        this.pictureUrl = pictureUrl;
    }

    @Override
    protected Object doInBackground(Reporte... params) {

        Reporte reporte = params[0];

        reporte.setPicture(pictureUrl);

        JsonObject json = session.post(reporte);

        if(json.has("ERROR_CODE")) {
            return new Gson().fromJson(json, ErrorResponse.class);
        }
        else {
            return new Gson().fromJson(json, Reporte.class);
        }
    }
}
