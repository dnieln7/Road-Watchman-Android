package com.daniel.reportes.ui.signup;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SignUpViewModel extends ViewModel {

    private MutableLiveData<String> code;
    private MutableLiveData<Boolean> verified;

    public SignUpViewModel() {
        code = new MutableLiveData<>();
        verified = new MutableLiveData<>();
    }

    public MutableLiveData<String> getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code.setValue(code);
    }

    public MutableLiveData<Boolean> getVerified() {
        return verified;
    }

    public void setVerified(Boolean verified) {
        this.verified.setValue(verified);
    }
}
