package com.dnieln7.roadwatchman.task.user;

import android.os.AsyncTask;

import com.dnieln7.http.request.HttpSession;
import com.dnieln7.roadwatchman.data.model.User;
import com.dnieln7.roadwatchman.data.model.VerificationResponse;
import com.dnieln7.roadwatchman.task.API;
import com.dnieln7.roadwatchman.task.ITaskListener;
import com.google.gson.Gson;

public class VerifyTask extends AsyncTask<String, Void, VerificationResponse> {

    private HttpSession session;
    private ITaskListener<VerificationResponse> listener;

    public VerifyTask(ITaskListener<VerificationResponse> listener) {
        this.session = new HttpSession(API.MAIN + "auth/verification");
        this.listener = listener;
    }

    @Override
    protected VerificationResponse doInBackground(String... params) {
        User email = new User();
        email.setEmail(params[0]);

        return new Gson().fromJson(session.post(email, 200), VerificationResponse.class);
    }

    @Override
    protected void onPostExecute(VerificationResponse verificationResponse) {
        super.onPostExecute(verificationResponse);

        if (verificationResponse != null) {
            listener.onSuccess(verificationResponse);
        }
        else {
            listener.onFail();
        }
    }
}
