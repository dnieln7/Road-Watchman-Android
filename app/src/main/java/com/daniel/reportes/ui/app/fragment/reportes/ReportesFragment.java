package com.daniel.reportes.ui.app.fragment.reportes;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.daniel.reportes.R;
import com.daniel.reportes.data.AppSession;
import com.daniel.reportes.ui.app.fragment.AppViewModel;
import com.daniel.reportes.ui.app.fragment.reportes.create.ReporteCreator;
import com.daniel.reportes.utils.NetworkMonitor;
import com.daniel.reportes.utils.PreferencesHelper;
import com.daniel.reportes.utils.Printer;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ReportesFragment extends Fragment {

    // Objects
    private boolean expanded;
    private ReporteDataService reporteDataService;
    private AppSession appSession;

    // Widgets
    private View root;

    private RecyclerView listView;

    private TextView listMessage;
    private TextView createLabel;
    private TextView refreshLabel;

    private FloatingActionButton menu;
    private FloatingActionButton create;
    private FloatingActionButton refresh;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        expanded = false;
        AppViewModel appViewModel = new ViewModelProvider(getActivity()).get(AppViewModel.class);
        reporteDataService = new ViewModelProvider(getActivity()).get(ReporteDataService.class);
        appSession = PreferencesHelper.getInstance(getActivity()).isUserLoggedIn();

        appViewModel.setAppSession(appSession);
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
        if (requestCode == ReporteCreator.REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                Printer.okDialog(getContext(), "Exito", "Su reporte se ha enviado");
                refresh();
            }
        }
    }

    private void initWidgets() {
        listView = root.findViewById(R.id.reportes_view);
        listMessage = root.findViewById(R.id.reportesListMessage);

        createLabel = root.findViewById(R.id.fab_create_label);
        refreshLabel = root.findViewById(R.id.fab_refresh_label);

        menu = root.findViewById(R.id.fab_menu);
        create = root.findViewById(R.id.fab_create);
        refresh = root.findViewById(R.id.fab_refresh);

        listView.setHasFixedSize(true);
        listView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private void initListeners() {
        menu.setOnClickListener(v -> expandMenu());
        create.setOnClickListener(v -> createReporte());
        refresh.setOnClickListener(v -> refresh());
    }

    private void createReporte() {
        expandMenu();

        Intent creatorIntent = new Intent(getActivity(), ReporteCreator.class);
        creatorIntent.putExtra("user", appSession.getUser());
        startActivityForResult(creatorIntent, ReporteCreator.REQUEST_CODE);
    }

    private void loadData() {
        reporteDataService.getReportes().observe(getActivity(), reportes -> {
            listView.setAdapter(new ReporteAdapter(reportes));
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
            listMessage.setVisibility(View.VISIBLE);
            listView.setVisibility(View.GONE);
        }
        else {
            listMessage.setVisibility(View.GONE);
            listView.setVisibility(View.VISIBLE);
        }
    }

    private void expandMenu() {
        Point point = new Point();

        getActivity().getWindowManager().getDefaultDisplay().getSize(point);

        float createHeight = point.y * 0.10F;
        float refreshHeight = point.y * 0.18F;

        if (expanded) {
            menu.animate().rotationBy(-180);

            create.animate().translationY(0);
            createLabel.animate().translationY(0);

            refresh.animate().translationY(0);
            refreshLabel.animate().translationY(0);

            create.hide();
            refresh.hide();
            createLabel.setVisibility(View.INVISIBLE);
            refreshLabel.setVisibility(View.INVISIBLE);

            expanded = false;
        }
        else {
            menu.animate().rotationBy(180);

            create.animate().translationY(-createHeight);
            createLabel.animate().translationY(-createHeight);

            refresh.animate().translationY(-refreshHeight);
            refreshLabel.animate().translationY(-refreshHeight);

            create.show();
            refresh.show();
            createLabel.setVisibility(View.VISIBLE);
            refreshLabel.setVisibility(View.VISIBLE);

            expanded = true;
        }
    }
}