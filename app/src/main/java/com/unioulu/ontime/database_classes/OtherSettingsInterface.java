package com.unioulu.ontime.database_classes;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface OtherSettingsInterface {

    // To be used only if table is empty
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertOtherSettings(OtherSettingsTable otherSettingsTable);

    // To be used from other settings fragment
    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateOtherSettings(OtherSettingsTable otherSettingsTable);

    // Only used for debugging
    @Query("SELECT * FROM OtherSettingsTable WHERE user_id = :user_id")
    List<OtherSettingsTable> fetchAllOtherSettings(int user_id);

    @Query("SELECT morning FROM OtherSettingsTable WHERE user_id = :user_id")
    long fetchMorningTime(int user_id);

    @Query("SELECT afternoon FROM OtherSettingsTable WHERE user_id = :user_id")
    long fetchAfternoonTime(int user_id);

    @Query("SELECT evening FROM OtherSettingsTable WHERE user_id = :user_id")
    long fetchEveningTime(int user_id);

    @Query("DELETE FROM OtherSettingsTable")
    void deleteAllOtherSettings();

}
