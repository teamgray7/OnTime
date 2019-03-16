package com.unioulu.ontime.database_classes;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface MedicineStatisticsInterface {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertDateAndStatus(Medicines.MedicineStatisticsTable medicineStatisticsElement);

    @Query("SELECT * FROM MedicineStatisticsTable WHERE medicine_id = :id")
    List<Medicines.MedicineStatisticsTable> fetchMedicineStatisticsByID(int id);
}
