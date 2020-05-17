package com.daniel.reportes.task.user;

import android.os.AsyncTask;

import com.daniel.reportes.task.API;
import com.daniel.reportes.task.TaskListener;
import com.dnieln7.http.request.HttpSession;
import com.dnieln7.http.request.exception.ResponseException;
import com.google.gson.JsonObject;

public class VerifyEmail extends AsyncTask<String, Void, TaskListener> {

    private HttpSession session;
    private TaskListener<Integer> listener;

    public VerifyEmail(TaskListener<Integer> listener) {
        this.session = new HttpSession(API.MAIN + "verification");
        this.listener = listener;
    }

    @Override
    protected TaskListener doInBackground(String... params) {
        try {
            JsonObject email = new JsonObject();

            email.addProperty("email", params[0]);

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
