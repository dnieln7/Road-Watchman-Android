package com.dnieln7.roadwatchman.work.report;

import android.content.Context;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.concurrent.CountDownLatch;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UploadPictureWork extends Worker {
    private String url;

    public UploadPictureWork(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        Uri uri = Uri.parse(getInputData().getString("URI"));
        String name = getInputData().getString("NAME");

        try {
            CountDownLatch countDown = new CountDownLatch(1);

            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference reference = storage.getReference("Reports/").child(name);

            reference.putFile(uri)
                    .addOnFailureListener(error -> logError(UploadReportWork.class, error))
                    .addOnSuccessListener(snapshot -> reference.getDownloadUrl()
                            .addOnCompleteListener(task -> {
                                url = task.getResult() == null ? "" : task.getResult().toString();
                                System.out.println("Work completed, url is >>> " + url);
                                countDown.countDown();
                            })
                    );

            countDown.await();

            return Result.success(createOutputData(url));
        }
        catch (InterruptedException e) {
            e.printStackTrace();
            return Result.failure();
        }
    }

    private Data createOutputData(String url) {
        return new Data.Builder()
                .putString("URL", url)
                .build();
    }

    public void logError(Class clazz, Throwable error) {
        Logger.getLogger(clazz.getName()).log(Level.SEVERE, "There was an error", error);
    }
}
