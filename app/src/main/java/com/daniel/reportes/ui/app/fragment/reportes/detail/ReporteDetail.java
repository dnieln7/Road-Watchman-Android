package com.daniel.reportes.ui.app.fragment.reportes.detail;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.daniel.reportes.R;
import com.daniel.reportes.data.Reporte;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.squareup.picasso.Picasso;

import org.joda.time.LocalDateTime;

import java.util.Locale;

public class ReporteDetail extends AppCompatActivity {

    private Reporte reporte;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reporte_detail);

        reporte = (Reporte) getIntent().getSerializableExtra("reporte");

        initWidgets();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_reporte_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.detail_back) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void initWidgets() {
        setSupportActionBar(findViewById(R.id.detail_toolbar));

        ImageView picture = findViewById(R.id.detail_picture);
        TextView date = findViewById(R.id.detail_date);
        TextView description = findViewById(R.id.detail_description);
        TextView location = findViewById(R.id.detail_location);
        MaterialCheckBox fixed = findViewById(R.id.detail_fixed);

        if (!reporte.getPicture().equals("")) {
            Picasso.get()
                    .load(reporte.getPicture())
                    .error(R.drawable.reportes)
                    .placeholder(R.drawable.reportes)
                    .resize(300, 300)
                    .onlyScaleDown()
                    .into(picture);
        }

        date.setText(
                new LocalDateTime(reporte.getDate())
                        .toString("dd MMMM, yyyy", Locale.forLanguageTag("MX"))
        );
        description.setText(reporte.getDescription());
        location.setText(reporte.getLocation_description());
        fixed.setActivated(reporte.isFixed());
        fixed.setText(fixed.isActivated()
                ? getString(R.string.reports_fixed)
                : getString(R.string.reports_awaiting)
        );
    }
}
