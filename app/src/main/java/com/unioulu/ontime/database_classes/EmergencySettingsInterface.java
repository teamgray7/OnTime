package com.unioulu.ontime.database_classes;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface EmergencySettingsInterface {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertEmergencyContact(EmergencySettingsTable emergencySettingsTable);

    @Delete
    void deleteEmergencyContact(EmergencySettingsTable emergencySettingsTable);

    @Query("SELECT * FROM EmergencySettingsTable")
    List<EmergencySettingsTable> fetchAllEmergencyContacts();

    @Query("DELETE FROM EmergencySettingsTable")
    void deleteAllEmergencyContacts();
}
