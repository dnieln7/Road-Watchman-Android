package com.dnieln7.roadwatchman.task.reporte;

import android.os.AsyncTask;

import com.dnieln7.http.request.HttpSession;
import com.dnieln7.http.request.exception.ResponseException;
import com.dnieln7.roadwatchman.data.model.Reporte;
import com.dnieln7.roadwatchman.task.API;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
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
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, String.format(Locale.ENGLISH, "%d %s", e.getCode(), e.getDescription()), e);
            return new ArrayList<>();
        }
    }
}
