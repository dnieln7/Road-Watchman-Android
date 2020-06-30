package com.dnieln7.roadwatchman.ui.app.pages.reports;

import android.app.Application;
import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.room.Room;

import com.dnieln7.roadwatchman.data.dao.ReporteDao;
import com.dnieln7.roadwatchman.data.dao.ReportesDatabase;
import com.dnieln7.roadwatchman.data.model.Reporte;
import com.dnieln7.roadwatchman.task.reporte.GetAllReportes;
import com.dnieln7.roadwatchman.utils.Printer;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class ReportDataService extends AndroidViewModel {

    private ReporteDao reportDao;
    private LiveData<List<Reporte>> reports;

    public ReportDataService(@NonNull Application application) {
        super(application);

        reportDao = Room.databaseBuilder(application, ReportesDatabase.class, "Reports-Data").build().reporteDao();
        reports = reportDao.get();
    }

    public LiveData<List<Reporte>> getReports() {
        return reports;
    }

    public void fetchFromNetwork(String userId) {
        try {
            List<Reporte> fetchedReports = new GetAllReportes(userId).execute().get();
            new InsertTask(reportDao, fetchedReports).execute();

        }
        catch (ExecutionException | InterruptedException e) {
            Printer.logError(ReportDataService.class.getName(), e);
        }
    }

    public Reporte getReportByIndex(int index) {
        return reports.getValue().get(index);
    }

    public static void deleteAll(Context context) {
        ReporteDao reporteDao = Room.databaseBuilder(context, ReportesDatabase.class, "Reports-Data")
                .build()
                .reporteDao();

        try {
            new DeleteTask(reporteDao).execute().get();
        }
        catch (ExecutionException | InterruptedException e) {
            Printer.logError(ReportDataService.class.getName(), e);
        }
    }

    private static class InsertTask extends AsyncTask<Void, Void, Void> {

        private ReporteDao reporteDao;
        private List<Reporte> reportes;

        InsertTask(ReporteDao reporteDao, List<Reporte> reportes) {
            this.reporteDao = reporteDao;
            this.reportes = reportes;
        }

        @Override
        protected final Void doInBackground(Void... params) {
            reporteDao.insert(reportes);

            return null;
        }
    }

    private static class DeleteTask extends AsyncTask<Void, Void, Void> {

        private ReporteDao reporteDao;

        DeleteTask(ReporteDao reporteDao) {
            this.reporteDao = reporteDao;
        }

        @Override
        protected final Void doInBackground(Void... params) {
            reporteDao.delete();

            return null;
        }
    }
}
