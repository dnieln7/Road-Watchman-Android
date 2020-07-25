package com.dnieln7.roadwatchman.task.reporte;

import android.os.AsyncTask;

import com.dnieln7.http.request.HttpSession;
import com.dnieln7.roadwatchman.data.model.Reporte;
import com.dnieln7.roadwatchman.task.API;
import com.dnieln7.roadwatchman.task.ITaskListener;
import com.google.gson.Gson;

import java.util.Arrays;
import java.util.List;

public class GetReportsTask extends AsyncTask<Void, Void, List<Reporte>> {

    private HttpSession session;
    private ITaskListener<Reporte> listener;

    public GetReportsTask(String id, ITaskListener<Reporte> listener) {
        this.session = new HttpSession(API.MAIN + "report?user=" + id);
        this.listener = listener;
    }

    @Override
    protected List<Reporte> doInBackground(Void... params) {
        return Arrays.asList(new Gson().fromJson(session.get(200), Reporte[].class));
    }

    @Override
    protected void onPostExecute(List<Reporte> reportes) {
        super.onPostExecute(reportes);

        if (reportes != null) {
            listener.onSuccess(reportes);
        }
        else {
            listener.onFail();
        }
    }
}
