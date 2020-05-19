package com.dnieln7.roadwatchman.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.dnieln7.roadwatchman.data.Reporte;

import java.util.List;
import java.util.Optional;

@Dao
public interface ReporteDao {

    @Query("SELECT * FROM reportes")
    LiveData<List<Reporte>> get();

    @Query("SELECT * FROM reportes WHERE id=:id")
    LiveData<Optional<Reporte>> getById(int id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(List<Reporte> reportes);
}
