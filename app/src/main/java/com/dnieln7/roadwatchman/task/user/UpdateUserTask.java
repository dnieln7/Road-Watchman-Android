package com.dnieln7.roadwatchman.task.user;

import android.os.AsyncTask;

import com.dnieln7.http.request.HttpSession;
import com.dnieln7.roadwatchman.data.model.User;
import com.dnieln7.roadwatchman.task.API;
import com.dnieln7.roadwatchman.task.ITaskListener;
import com.google.gson.Gson;

public class UpdateUserTask extends AsyncTask<User, Void, User> {

    private HttpSession session;
    private ITaskListener<User> listener;

    public UpdateUserTask(String id, ITaskListener<User> listener) {
        this.session = new HttpSession(API.MAIN + "user/" + id);
        this.listener = listener;
    }

    @Override
    protected User doInBackground(User... params) {
        return new Gson().fromJson(session.put(params[0], 200), User.class);
    }

    @Override
    protected void onPostExecute(User user) {
        super.onPostExecute(user);

        if (user != null) {
            listener.onSuccess(user);
        }
        else {
            listener.onFail();
        }
    }
}
