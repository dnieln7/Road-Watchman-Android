package com.daniel.reportes.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.daniel.reportes.data.Reporte;

import java.util.List;
import java.util.Optional;

@Dao
public interface ReporteDao {

    @Query("SELECT * FROM reportes")
    public LiveData<List<Reporte>> get();

    @Query("SELECT * FROM reportes WHERE id=:id")
    public LiveData<Optional<Reporte>> getById(int id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insert(List<Reporte> reportes);
}
