package com.daniel.reportes.task.reporte;

import android.os.AsyncTask;

import com.daniel.reportes.data.Reporte;
import com.daniel.reportes.task.API;
import com.daniel.reportes.task.TaskListener;
import com.dnieln7.http.request.HttpSession;
import com.dnieln7.http.request.exception.ResponseException;
import com.google.gson.Gson;

public class PostReporte extends AsyncTask<Reporte, Integer, TaskListener> {

    private HttpSession session;
    private TaskListener<Reporte> listener;

    public PostReporte(TaskListener<Reporte> listener) {
        this.session = new HttpSession(API.MAIN + "report");
        this.listener = listener;
    }

    @Override
    protected TaskListener<Reporte> doInBackground(Reporte... params) {
        try {
            listener.setResult(new Gson().fromJson(session.post(params[0]), Reporte.class));
        }
        catch (ResponseException e) {
            listener.setException(e);
        }

        return listener;
    }
}
