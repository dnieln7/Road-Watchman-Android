package com.daniel.reportes.ui.app.fragment;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.daniel.reportes.data.AppSession;

public class AppViewModel extends ViewModel {

    private MutableLiveData<AppSession> appSession;

    public void setAppSession(MutableLiveData<AppSession> appSession) {
        this.appSession = appSession;
    }

    public void setAppSessionValue(AppSession appSession) {
        this.appSession.setValue(appSession);
    }

    public void updateToken(String token) {
        this.appSession.getValue().setToken(token);
    }

    public MutableLiveData<AppSession> getAppSession() {
        return appSession;
    }
}
