package com.daniel.reportes.ui.app.fragment;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.daniel.reportes.data.AppSession;

public class AppViewModel extends ViewModel {

    private MutableLiveData<AppSession> appSession;

    public AppViewModel() {
        appSession = new MutableLiveData<>();
    }

    public void setAppSession(AppSession appSession) {
        this.appSession.setValue(appSession);
    }

    public MutableLiveData<AppSession> getAppSession() {
        return appSession;
    }
}
