package com.dnieln7.roadwatchman.work.report;

import android.content.Context;

import androidx.work.BackoffPolicy;
import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import com.dnieln7.roadwatchman.data.model.Reporte;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Helper class to create and start works for uploading reports.
 *
 * @author dnieln7
 */
public class ReportWorkManager {
    public static final String PICTURE_WORK_TAG = "UPLOAD_PICTURE";
    public static final String REPORT_WORK_TAG = "UPLOAD_REPORT";

    private Constraints constraints;
    private OneTimeWorkRequest pictureRequest;
    private OneTimeWorkRequest reportRequest;

    /**
     * Creates a new instance of {@link ReportWorkManager}
     *
     * @param networkType The network connection to be required in order to start the work, see {@link NetworkType}
     */
    public ReportWorkManager(NetworkType networkType) {
        this.constraints = new Constraints.Builder()
                .setRequiredNetworkType(networkType)
                .setRequiresBatteryNotLow(true)
                .build();
    }

    public static Data createOutputData(Map<String, Object> values) {
        return new Data.Builder().putAll(values).build();
    }

    public void prepareUploadWork(Reporte report, String pictureUri, String pictureName) {
        String reportJSON = new Gson().toJson(report);

        Data pictureData = new Data
                .Builder()
                .putString("URI", pictureUri)
                .putString("NAME", pictureName)
                .build();

        pictureRequest = new OneTimeWorkRequest
                .Builder(UploadPictureWork.class)
                .addTag(PICTURE_WORK_TAG)
                .setInputData(pictureData)
                .setConstraints(constraints)
                .setBackoffCriteria(
                        BackoffPolicy.LINEAR,
                        OneTimeWorkRequest.MIN_BACKOFF_MILLIS,
                        TimeUnit.MILLISECONDS
                ).build();

        Data reportData = new Data
                .Builder()
                .putString("REPORT", reportJSON)
                .build();

        reportRequest = new OneTimeWorkRequest
                .Builder(UploadReportWork.class)
                .addTag(REPORT_WORK_TAG)
                .setInputData(reportData)
                .setConstraints(constraints)
                .setBackoffCriteria(
                        BackoffPolicy.LINEAR,
                        OneTimeWorkRequest.MIN_BACKOFF_MILLIS,
                        TimeUnit.MILLISECONDS
                ).build();
    }

    public void startWork(Context context) {
        WorkManager.getInstance(context)
                .beginWith(pictureRequest)
                .then(reportRequest)
                .enqueue();
    }

    public void prepareAndStartUploadWork(Context context, Reporte report) {
        String reportJSON = new Gson().toJson(report);

        Data reportData = new Data
                .Builder()
                .putString("REPORT", reportJSON)
                .putString("URL", "")
                .build();

        reportRequest = new OneTimeWorkRequest
                .Builder(UploadReportWork.class)
                .addTag(REPORT_WORK_TAG)
                .setInputData(reportData)
                .setConstraints(constraints)
                .setBackoffCriteria(
                        BackoffPolicy.LINEAR,
                        OneTimeWorkRequest.MIN_BACKOFF_MILLIS,
                        TimeUnit.MILLISECONDS
                ).build();

        WorkManager.getInstance(context).enqueue(reportRequest);
    }

    public List<WorkInfo> getWorkInfo(Context context, String tag) {
        List<WorkInfo> works = new ArrayList<>();

        try {
            works = WorkManager.getInstance(context).getWorkInfosByTag(tag).get();
        }
        catch (ExecutionException | InterruptedException e) {
            Logger.getLogger(ReportWorkManager.class.getName()).log(Level.SEVERE, "There was an error", e);
        }

        return works;
    }

    public void cancelWork(Context context, String tag) {
        WorkManager.getInstance(context).cancelAllWorkByTag(tag);
    }
}
