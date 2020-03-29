package com.daniel.reportes.data;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.daniel.reportes.data.dao.Converters;
import com.daniel.reportes.data.dao.ReporteDao;

@Database(entities = Reporte.class, version = 1, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class ReportesDatabase extends RoomDatabase {
    public abstract ReporteDao reporteDao();
}
