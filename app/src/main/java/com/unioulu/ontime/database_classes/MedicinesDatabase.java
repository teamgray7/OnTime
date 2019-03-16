package com.unioulu.ontime.database_classes;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

@Database(entities = {Medicines.class}, version = 1, exportSchema = false)
public abstract class MedicinesDatabase extends RoomDatabase {
    public abstract MedicineDBInterface medicineDBInterface();
}

