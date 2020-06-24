package com.dnieln7.roadwatchman.data.dao;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.dnieln7.roadwatchman.data.model.Reporte;

@Database(entities = Reporte.class, version = 1, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class ReportesDatabase extends RoomDatabase {
    public abstract ReporteDao reporteDao();
}
