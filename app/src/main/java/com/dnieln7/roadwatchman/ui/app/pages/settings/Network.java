package com.dnieln7.roadwatchman.ui.app.pages.settings;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.dnieln7.roadwatchman.R;
import com.dnieln7.roadwatchman.ui.app.pages.reportes.background.ReportesService;
import com.dnieln7.roadwatchman.utils.LocationHelper;
import com.dnieln7.roadwatchman.utils.LocationUtils;
import com.dnieln7.roadwatchman.utils.NetworkMonitor;
import com.dnieln7.roadwatchman.utils.PreferencesHelper;
import com.dnieln7.roadwatchman.utils.Printer;
import com.google.android.material.switchmaterial.SwitchMaterial;

/**
 * Controls app's network settings.
 *
 * @author dnieln7
 */
public class Network extends Fragment {

    private View root;

    private SwitchMaterial backgroundReports;

    public Network() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_network, container, false);

        initWidgets();

        return root;
    }

    private void initWidgets() {
        backgroundReports = root.findViewById(R.id.network_background_reportes);
        backgroundReports.setChecked(ReportesService.instance != null);

        backgroundReports.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                showReportServiceWarning();
            }
            else {
                stopReportesService();
            }
        });
    }

    private boolean checkLocation() {
        if (LocationHelper.hasGPSEnabled(requireActivity())) {
            LocationUtils.getGPS(requireActivity());
            startReportsService();
            return true;
        }

        LocationHelper.askActivateLocation(requireActivity());
        return false;
    }

    private boolean checkConnection() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            if (NetworkMonitor.getMonitor(requireContext()).hasNetwork()) {
                return checkLocation();
            }
        }
        else {
            if (NetworkMonitor.hasNetwork(requireContext())) {
                startReportsService();
                return checkLocation();
            }
        }

        Printer.okDialog(requireContext(), getString(R.string.warning), getString(R.string.settings_warning));
        return false;
    }

    private void showReportServiceWarning() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());

        builder.setTitle(R.string.settings_background);
        builder.setMessage(R.string.settings_background_warning);
        builder.setPositiveButton(R.string.settings_continue, (dialog, which) -> backgroundReports.setChecked(checkConnection()));
        builder.setNegativeButton(R.string.settings_cancel, (dialog, which) -> backgroundReports.setChecked(false));
        builder.show();
    }

    private void startReportsService() {
        Intent service = new Intent(requireContext(), ReportesService.class);
        service.putExtra("USER_ID", PreferencesHelper.getInstance(requireActivity()).isUserLoggedIn().getUser().getId());
        requireContext().startService(service);
    }

    private void stopReportesService() {
        Intent service = new Intent(requireContext(), ReportesService.class);
        requireContext().stopService(service);
    }
}
