package com.dnieln7.roadwatchman.ui.app.fragment.reportes;

import android.app.Application;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.room.Room;

import com.dnieln7.roadwatchman.data.Reporte;
import com.dnieln7.roadwatchman.data.ReportesDatabase;
import com.dnieln7.roadwatchman.data.dao.ReporteDao;
import com.dnieln7.roadwatchman.task.reporte.GetAllReportes;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ReporteDataService extends AndroidViewModel {

    private ReporteDao reporteDao;
    private LiveData<List<Reporte>> reportes;

    public ReporteDataService(@NonNull Application application) {
        super(application);

        reporteDao = Room.databaseBuilder(application, ReportesDatabase.class, "Reportes-Data").build().reporteDao();
        reportes = reporteDao.get();
    }

    public LiveData<List<Reporte>> getReportes() {
        return reportes;
    }

    public void fetchFromNetwork(String userId) {
        try {
            List<Reporte> reportes = new GetAllReportes(userId).execute().get();
            new InsertTask(reporteDao, reportes).execute();

        }
        catch (ExecutionException | InterruptedException e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, e);

        }
    }

    public Optional<Reporte> getReporteById(int id) {
        return reporteDao.getById(id).getValue();
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
}
