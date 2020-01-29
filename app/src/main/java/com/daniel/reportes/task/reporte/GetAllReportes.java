package com.daniel.reportes.task.reporte;

import android.os.AsyncTask;

import com.daniel.reportes.data.Reporte;
import com.daniel.reportes.task.API;
import com.dnieln7.httprequest.HttpSession;
import com.dnieln7.httprequest.exception.ResponseException;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GetAllReportes extends AsyncTask<Void, Void, List<Reporte>> {

    private HttpSession session;

    public GetAllReportes(String id) {
        this.session = new HttpSession(API.MAIN + "report?user=" + id);
    }

    @Override
    protected List<Reporte> doInBackground(Void... params) {
        try {
            return Arrays.asList(new Gson().fromJson(session.get(), Reporte[].class));
        }
        catch (ResponseException e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, e.getCode() + " " + e.getDescription(), e);
            return new ArrayList<>();
        }
    }
}
