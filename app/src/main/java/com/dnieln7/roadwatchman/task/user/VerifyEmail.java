package com.dnieln7.roadwatchman.task.user;

import android.os.AsyncTask;

import com.dnieln7.roadwatchman.task.API;
import com.dnieln7.roadwatchman.task.TaskListener;
import com.dnieln7.http.request.HttpSession;
import com.dnieln7.http.request.exception.ResponseException;
import com.google.gson.JsonObject;

public class VerifyEmail extends AsyncTask<String, Void, TaskListener<Integer>> {

    private HttpSession session;
    private TaskListener<Integer> listener;

    public VerifyEmail(TaskListener<Integer> listener) {
        this.session = new HttpSession(API.MAIN + "verification");
        this.listener = listener;
    }

    @Override
    protected TaskListener<Integer> doInBackground(String... params) {
        try {
            JsonObject email = new JsonObject();

            email.addProperty("email_address", params[0]);

            listener.setResult(session.execute(
                    email,
                    200,
                    "POST",
                    true
            ).getAsJsonObject().get("code").getAsInt());
        }
        catch (ResponseException e) {
            listener.setException(e);
        }

        return listener;
    }
}
