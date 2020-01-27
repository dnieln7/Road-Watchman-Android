package com.daniel.reportes.ui.app.fragment.reportes;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.daniel.reportes.R;
import com.daniel.reportes.data.AppSession;
import com.daniel.reportes.data.Reporte;
import com.daniel.reportes.task.reporte.GetAllReportes;
import com.daniel.reportes.ui.app.fragment.AppViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ReportesFragment extends Fragment {

    // Objects
    private boolean expanded;
    private AppViewModel appViewModel;
    private AppSession appSession;
    private ReportesAdapter adapter;
    private List<Reporte> reportes;

    // Widgets
    private View root;

    private ListView reportesList;

    private TextView reportesListMessage;
    private TextView createText;
    private TextView refreshText;

    private FloatingActionButton reportesMenu;
    private FloatingActionButton reportesCreate;
    private FloatingActionButton reportesRefresh;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_reportes, container, false);
        appViewModel = ViewModelProviders.of(getActivity()).get(AppViewModel.class);

        init();
        initObjects();
        initWidgets();
        initListeners();
        initList();

        return root;
    }

    private void init() {
        expanded = false;
    }

    private void initObjects() {
        appViewModel.getAppSession().observe(getActivity(), session -> appSession = session);

        if (appSession == null) {
            appSession = (AppSession) getActivity().getIntent().getSerializableExtra("session");
            appViewModel.setAppSession(appSession);
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

    private void initList() {
        try {
            reportes = new GetAllReportes(String.valueOf(appSession.getUser().getId())).execute().get();
        }
        catch (ExecutionException | InterruptedException e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, e);
        }

        adapter = new ReportesAdapter(getContext(), reportes);
        reportesList.setAdapter(adapter);

        showList();
    }

    private void createReporte() {
        expandMenu();

        CreateReporteStep1 step1 = new CreateReporteStep1();

        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.nav_host_fragment, step1, step1.getTag())
                .commit();
    }

    private void refresh() {
        expandMenu();

        try {
            reportes = new GetAllReportes(String.valueOf(appSession.getUser().getId())).execute().get();
        }
        catch (ExecutionException | InterruptedException e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, e);
        }

        adapter.notifyDataSetChanged();
        reportesList.invalidateViews();
        reportesList.refreshDrawableState();
    }

    private void showList() {
        if(reportes.isEmpty()) {
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