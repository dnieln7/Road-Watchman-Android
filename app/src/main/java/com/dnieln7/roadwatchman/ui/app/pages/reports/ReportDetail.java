package com.dnieln7.roadwatchman.ui.app.pages.reports;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.dnieln7.roadwatchman.R;
import com.dnieln7.roadwatchman.data.model.Reporte;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.squareup.picasso.Picasso;

import org.joda.time.LocalDateTime;

import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 */
public class ReportDetail extends Fragment {

    private Reporte reporte;

    private View root;

    public ReportDetail() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        int reportIndex = ReportDetailArgs.fromBundle(requireArguments()).getReportIndex();

        reporte = new ViewModelProvider(requireActivity()).get(ReportDataService.class).getReportByIndex(reportIndex);
        root = inflater.inflate(R.layout.fragment_report_detail, container, false);

        initWidgets();

        return root;
    }

    private void initWidgets() {
        ImageView picture = root.findViewById(R.id.detail_picture);
        TextView date = root.findViewById(R.id.detail_date);
        TextView description = root.findViewById(R.id.detail_description);
        TextView location = root.findViewById(R.id.detail_location);
        MaterialCheckBox fixed = root.findViewById(R.id.detail_fixed);

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
