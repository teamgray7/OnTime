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
    @Insert
    void insertOtherSettings(OtherSettingsTable otherSettingsTable);

    // To be used from other settings fragment
    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateOtherSettings(OtherSettingsTable otherSettingsTable);

    // Only used for debugging
    @Query("SELECT * FROM OtherSettingsTable")
    List<OtherSettingsTable> fetchAllOtherSettings();

    @Query("DELETE FROM OtherSettingsTable")
    void deleteAllOtherSettings();

}
