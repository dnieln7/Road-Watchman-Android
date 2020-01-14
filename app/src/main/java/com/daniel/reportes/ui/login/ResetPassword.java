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

    // Widgets
    private EditText code;

    private Button restore;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_reset_password, container, false);

        initWidgets();
        initListeners();

        return root;
    }

    private void initWidgets() {
        code = root.findViewById(R.id.restoreCode);

        restore = root.findViewById(R.id.restore);
    }

    private void initListeners() {
        restore.setOnClickListener(v -> reset());
    }

    private void reset() {
        if (code.getText().toString().trim().equals("")) {
            code.setError("Requerido!");
        }
        else {
            this.getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
        }
    }
}
