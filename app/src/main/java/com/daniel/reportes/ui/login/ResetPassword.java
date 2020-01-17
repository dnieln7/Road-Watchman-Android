package com.daniel.reportes.ui.login;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.Fragment;

import com.daniel.reportes.R;

public class ResetPassword extends Fragment {

    private View root;

    //Var
    private String code;

    // Widgets
    private EditText codeIn;

    private Button restore;

    public ResetPassword(String code) {
        this.code = code;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_reset_password, container, false);

        initWidgets();
        initListeners();

        return root;
    }

    private void initWidgets() {
        codeIn = root.findViewById(R.id.codeIn);

        restore = root.findViewById(R.id.verify);
    }

    private void initListeners() {
        restore.setOnClickListener(v -> reset());
    }

    private void reset() {

        String code = codeIn.getText().toString();

        if (code.trim().isEmpty()) {
            codeIn.setError("Requerido!");
        }
        else {
            if(code.equals(this.code)) {

                this.getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
            }
            else {
                codeIn.setError("Código incorrecto!");
            }
        }
    }
}
