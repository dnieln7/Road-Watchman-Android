package com.daniel.reportes.ui.app.fragment.reportes;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.daniel.reportes.R;
import com.daniel.reportes.data.AppSession;
import com.daniel.reportes.ui.app.fragment.AppViewModel;
import com.daniel.reportes.ui.app.fragment.reportes.create.ReporteCreator;
import com.daniel.reportes.utils.NetworkMonitor;
import com.daniel.reportes.utils.Printer;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ReportesFragment extends Fragment {

    // Objects
    private boolean expanded;
    private ReporteDataService reporteDataService;
    private AppViewModel appViewModel;
    private AppSession appSession;

    // Widgets
    private View root;

    private ListView reportesList;

    private TextView reportesListMessage;
    private TextView createText;
    private TextView refreshText;

    private FloatingActionButton reportesMenu;
    private FloatingActionButton reportesCreate;
    private FloatingActionButton reportesRefresh;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        expanded = false;
        appViewModel = new ViewModelProvider(getActivity()).get(AppViewModel.class);
        reporteDataService = new ViewModelProvider(getActivity()).get(ReporteDataService.class);
        appSession = (AppSession) getActivity().getIntent().getSerializableExtra("session");

        appViewModel.getAppSession().observe(getActivity(), appSession1 -> {
            if (appSession1 != null) {
                appSession.setUser(appSession1.getUser());
                appSession.setToken(appSession1.getToken());
            }
            else {
                appViewModel.setAppSession(appSession);
            }
        });
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_reportes, container, false);

        initWidgets();
        initListeners();
        loadData();

        return root;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == ReporteCreator.REQUEST_CODE) {
            if(resultCode == Activity.RESULT_OK) {
                Printer.okDialog(getContext(), "Exito", "Su reporte se ha enviado");
                refresh();
            }
        }
    }

    private void initWidgets() {
        reportesList = root.findViewById(R.id.reportesList);
        reportesListMessage = root.findViewById(R.id.reportesListMessage);

        createText = root.findViewById(R.id.createText);
        refreshText = root.findViewById(R.id.refreshText);

        reportesMenu = root.findViewById(R.id.reportesMenu);
        reportesCreate = root.findViewById(R.id.reportesCreate);
        reportesRefresh = root.findViewById(R.id.reportesRefresh);
    }

    private void initListeners() {
        reportesMenu.setOnClickListener(v -> expandMenu());
        reportesCreate.setOnClickListener(v -> createReporte());
        reportesRefresh.setOnClickListener(v -> refresh());
    }

    private void createReporte() {
        expandMenu();

        Intent creatorIntent = new Intent(getActivity(), ReporteCreator.class);
        creatorIntent.putExtra("user", appSession.getUser());
        startActivityForResult(creatorIntent, ReporteCreator.REQUEST_CODE);
    }

    private void loadData() {
        reporteDataService.getReportes().observe(getActivity(), reportes -> {
            reportesList.setAdapter(new ReporteAdapter(getContext(), reportes));
            ((ReporteAdapter) reportesList.getAdapter()).notifyDataSetChanged();
            reportesList.invalidateViews();
            reportesList.refreshDrawableState();
            showList(reportes.isEmpty());
        });
    }

    private void refresh() {
        expandMenu();

        boolean connected;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            connected = NetworkMonitor.getMonitor(getContext()).hasNetwork();
        }
        else {
            connected = NetworkMonitor.hasNetwork(getContext());
        }

        if (connected) {
            reporteDataService.fetchFromNetwork(String.valueOf(appSession.getUser().getId()));
            loadData();
        }
        else {
            Printer.okDialog(getContext(), "Alerta", "Se requiere conexión a internet para cargar los reportes");
        }
    }

    private void showList(boolean isEmpty) {
        if (isEmpty) {
            reportesListMessage.setVisibility(View.VISIBLE);
            reportesList.setVisibility(View.GONE);
        }
        else {
            reportesListMessage.setVisibility(View.GONE);
            reportesList.setVisibility(View.VISIBLE);
        }
    }

    private void expandMenu() {

        if (expanded) {
            reportesMenu.animate().rotationBy(-180);
            reportesCreate.animate().translationY(0);
            reportesRefresh.animate().translationY(0);
            createText.animate().translationY(0);
            refreshText.animate().translationY(0);

            reportesCreate.hide();
            reportesRefresh.hide();
            createText.setVisibility(View.INVISIBLE);
            refreshText.setVisibility(View.INVISIBLE);

            expanded = false;
        }
        else {
            reportesMenu.animate().rotationBy(180);
            reportesCreate.animate().translationY(-150);
            reportesRefresh.animate().translationY(-300);
            createText.animate().translationY(-150);
            refreshText.animate().translationY(-300);

            reportesCreate.show();
            reportesRefresh.show();
            createText.setVisibility(View.VISIBLE);
            refreshText.setVisibility(View.VISIBLE);

            expanded = true;
        }
    }
}