package com.unioulu.ontime.database_classes;


import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface MedicineDBInterface {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertOnlySingleMedicine(Medicines medicine);

    @Query("SELECT * FROM Medicines")
    LiveData<List<Medicines>> fetchAllMedicines();

    @Query("SELECT * FROM Medicines WHERE medicine_name = :medicine_name")
    Medicines fetchOneMedicineByName(String medicine_name);

    @Query("SELECT picture_path FROM Medicines WHERE medicine_name = :medicine_name")
    String fetchPicturePathByName(String medicine_name);

    @Query("SELECT morningAt FROM Medicines WHERE medicine_name = :medicine_name")
    String fetchMorningAtByName(String medicine_name);

    @Query("SELECT afternoonAt FROM Medicines WHERE medicine_name = :medicine_name")
    String fetchAfternoonAtByName(String medicine_name);

    @Query("SELECT everingAt FROM Medicines WHERE medicine_name = :medicine_name")
    String fetchEveningAtByName(String medicine_name);

    @Query("SELECT customAt FROM Medicines WHERE medicine_name = :medicine_name")
    String fetchCustomAtByName(String medicine_name);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateMedicine(Medicines medicine);

    @Delete
    void deleteMedicine(Medicines medicine);

    @Query("DELETE FROM MEDICINES WHERE medicine_name = :medicine_name")
    void deleteMedicineByName(String medicine_name);

    @Query("DELETE FROM MEDICINES")
    void deleteMedicinesTable();

}
