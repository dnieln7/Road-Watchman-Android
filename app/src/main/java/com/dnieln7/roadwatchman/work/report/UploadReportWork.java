package com.dnieln7.roadwatchman.work.report;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.dnieln7.http.request.HttpSession;
import com.dnieln7.roadwatchman.data.model.Reporte;
import com.dnieln7.roadwatchman.task.API;
import com.google.gson.Gson;

public class UploadReportWork extends Worker {

    public UploadReportWork(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        Result result = Result.failure();

        String pictureURL = getInputData().getString("URL");
        String reportJSON = getInputData().getString("REPORT");

        if (reportJSON != null) {
            HttpSession session = new HttpSession(API.MAIN + "report");
            Reporte report = new Gson().fromJson(reportJSON, Reporte.class);
            Reporte response = null;

            report.setPicture(pictureURL == null ? "" : pictureURL);

            response = new Gson().fromJson(session.post(report, 201), Reporte.class);

            if (response != null) {
                result = Result.success();
            }
        }
        else {
            result = Result.failure();
        }

        return result;
    }
}
