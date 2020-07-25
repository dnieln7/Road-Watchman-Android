package com.dnieln7.roadwatchman.ui.app.pages;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.dnieln7.roadwatchman.data.model.User;

public class AppViewModel extends ViewModel {

    private MutableLiveData<User> user;

    public AppViewModel() {
        user = new MutableLiveData<>();
    }

    public MutableLiveData<User> getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user.postValue(user);
    }
}
