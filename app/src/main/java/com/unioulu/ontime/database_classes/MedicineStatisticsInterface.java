package com.unioulu.ontime.database_classes;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface MedicineStatisticsInterface {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertDateAndStatus(MedicineStatistics medicineStatisticsElement);

    @Query("SELECT * FROM MedicineStatistics WHERE id = :id")
    List<MedicineStatistics> fetchMedicineStatisticsByID(int id);

    @Query("SELECT * FROM MedicineStatistics WHERE id = :id AND medicine_name= :medicine_name")
    List<MedicineStatistics> getMedicineStatistics(int id, String medicine_name);

    @Query("DELETE FROM MedicineStatistics")
    void deleteMedicinesStatisticsTable();

}
