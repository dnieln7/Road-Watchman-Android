package com.daniel.reportes.ui.fake;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.daniel.reportes.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class Reports extends AppCompatActivity {

    // Var
    boolean expanded;

    // Objects

    // Widgets
    TextView withPictureLabel;
    TextView withoutPictureLabel;

    FloatingActionButton newReporte;
    FloatingActionButton withPicture;
    FloatingActionButton withoutPicture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reports);

        initVar();
        initWidgets();
    }

    private void initVar() {
        expanded = false;
    }

    private void initObjects() {

    }

    private void initWidgets() {
        withPictureLabel = findViewById(R.id.withPictureLabel);
        withoutPictureLabel = findViewById(R.id.withoutPictureLabel);

        newReporte = findViewById(R.id.newReporte);
        withPicture = findViewById(R.id.withPicture);
        withoutPicture = findViewById(R.id.withoutPicture);
    }

    private void initListeners() {

    }

    public void expandMenu(View view) {

        if (expanded) {
            newReporte.animate().rotationBy(-180);
            withPicture.animate().translationY(0);
            withoutPicture.animate().translationY(0);
            withPictureLabel.animate().translationY(0);
            withoutPictureLabel.animate().translationY(0);

            withPicture.hide();
            withoutPicture.hide();
            withPictureLabel.setVisibility(View.INVISIBLE);
            withoutPictureLabel.setVisibility(View.INVISIBLE);

            expanded = false;
        }
        else {
            newReporte.animate().rotationBy(180);
            withPicture.animate().translationY(-150);
            withoutPicture.animate().translationY(-300);
            withPictureLabel.animate().translationY(-150);
            withoutPictureLabel.animate().translationY(-300);

            withPicture.show();
            withoutPicture.show();
            withPictureLabel.setVisibility(View.VISIBLE);
            withoutPictureLabel.setVisibility(View.VISIBLE);

            expanded = true;
        }
    }
}
