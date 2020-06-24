package com.dnieln7.roadwatchman.ui.app.fragment;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.dnieln7.roadwatchman.data.model.AppSession;
import com.dnieln7.roadwatchman.data.model.User;

public class AppViewModel extends ViewModel {

    private MutableLiveData<AppSession> appSession;

    public AppViewModel() {
        appSession = new MutableLiveData<>();
    }

    public void setUser(User user) {
        this.appSession.getValue().setUser(user);
    }

    public MutableLiveData<AppSession> getAppSession() {
        return appSession;
    }

    public void setAppSession(AppSession appSession) {
        this.appSession.setValue(appSession);
    }
}
