package com.unioulu.ontime.database_classes;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;


@Database(entities = {Medicines.class, MedicineStatistics.class}, version = 3, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract MedicineDBInterface medicineDBInterface();
    public abstract MedicineStatisticsInterface medicineStatisticsInterface();
}

