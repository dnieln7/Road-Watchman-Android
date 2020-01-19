package com.daniel.reportes.ui.signup;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.daniel.reportes.R;
import com.daniel.reportes.ui.other.IFragmentListener;

public class Verify extends Fragment {


    //Var
    private String code;

    //Objects
    private View root;
    private SignUpViewModel viewModel;
    private IFragmentListener listener;

    // Widgets
    private EditText codeIn;
    private Button verify;
    private Button back;

    public Verify(String code, IFragmentListener listener) {
        this.code = code;
        this.listener = listener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_verify, container, false);
        viewModel = ViewModelProviders.of(getActivity()).get(SignUpViewModel.class);

        initVar();
        initWidgets();
        initListeners();

        return root;
    }

    private void initVar() {
        viewModel.getCode().observe(this, s -> code = s);
    }

    private void initWidgets() {
        codeIn = root.findViewById(R.id.codeIn);
        verify = root.findViewById(R.id.verify);
        back = root.findViewById(R.id.back);
    }

    private void initListeners() {
        verify.setOnClickListener(v -> verify());
        back.setOnClickListener(v -> goBack());
    }

    private void verify() {

        String code = codeIn.getText().toString();

        if (code.trim().isEmpty()) {
            codeIn.setError("Requerido!");
        }
        else {
            if (code.equals(this.code)) {
                viewModel.setVerified(true);
                listener.showFragment(1);
            }
            else {
                codeIn.setError("Código incorrecto!");
            }
        }
    }

    private void goBack() {
        listener.showFragment(1);
    }
}
