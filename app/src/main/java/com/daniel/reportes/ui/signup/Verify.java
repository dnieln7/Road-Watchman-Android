package com.daniel.reportes.ui.signup;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.daniel.reportes.R;
import com.daniel.reportes.ui.other.IFragmentListener;
import com.daniel.reportes.utils.Utils;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class Verify extends Fragment {

    //Objects
    private String code;

    private SignUpViewModel viewModel;
    private IFragmentListener listener;

    // Widgets
    private View root;

    private TextInputEditText verifyCode;

    private MaterialButton verifyOk;
    private MaterialButton verifyBack;

    public Verify(String code, IFragmentListener listener) {
        this.code = code;
        this.listener = listener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_verify, container, false);
        viewModel = ViewModelProviders.of(getActivity()).get(SignUpViewModel.class);

        initObjects();
        initWidgets();
        initListeners();

        return root;
    }

    private void initObjects() {
        viewModel.getCode().observe(this, s -> code = s);
    }

    private void initWidgets() {
        verifyCode = root.findViewById(R.id.verifyCode);

        verifyOk = root.findViewById(R.id.verifyOk);
        verifyBack = root.findViewById(R.id.verifyBack);
    }

    private void initListeners() {
        verifyOk.setOnClickListener(v -> verify());
        verifyBack.setOnClickListener(v -> goBack());
    }

    private void verify() {
        Utils.hideKeyboard(getActivity());

        String code = verifyCode.getText().toString();

        if (code.trim().isEmpty()) {
            verifyCode.setError("Requerido!");
        }
        else {
            if (code.equals(this.code)) {
                viewModel.setVerified(true);
                listener.showFragment(1);
            }
            else {
                verifyCode.setError("Código incorrecto!");
            }
        }
    }

    private void goBack() {
        listener.showFragment(1);
    }
}
