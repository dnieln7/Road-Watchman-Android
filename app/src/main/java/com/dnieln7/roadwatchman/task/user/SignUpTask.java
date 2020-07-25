package com.dnieln7.roadwatchman.task.user;

import android.os.AsyncTask;

import com.dnieln7.http.request.HttpSession;
import com.dnieln7.roadwatchman.data.model.AuthResponse;
import com.dnieln7.roadwatchman.data.model.User;
import com.dnieln7.roadwatchman.task.API;
import com.dnieln7.roadwatchman.task.ITaskListener;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class SignUpTask extends AsyncTask<User, Void, AuthResponse> {

    private ITaskListener<AuthResponse> listener;
    private HttpSession session;


    public SignUpTask(ITaskListener<AuthResponse> listener) {
        this.session = new HttpSession(API.MAIN + "auth/sign-up");
        this.listener = listener;
    }

    @Override
    protected AuthResponse doInBackground(User... params) {
        return new Gson().fromJson(session.post(params[0], 201), AuthResponse.class);
    }

    @Override
    protected void onPostExecute(AuthResponse authResponse) {
        super.onPostExecute(authResponse);

        if (listener != null) {
            if (authResponse != null) {
                listener.onSuccess(authResponse);
            }
            else {
                listener.onFail();
            }
        }
    }
}
