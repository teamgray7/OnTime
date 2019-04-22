package com.unioulu.ontime.database_classes;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

// Each user ID has a single set of saved settings
@Entity(indices={@Index(value="user_id", unique=true)})
public class OtherSettingsTable {

    @NonNull
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "user_id")
    private int user_id;

    @ColumnInfo(name = "morning")
    private String morning;

    @ColumnInfo(name = "afternoon")
    private String afternoon;

    @ColumnInfo(name = "evening")
    private String evening;

    @ColumnInfo(name = "snooze_time")
    private String snooze_time;

    @Ignore
    public OtherSettingsTable(){} // Empty constructor

    public OtherSettingsTable(int user_id, String morning, String afternoon, String evening, String snooze_time) {
        this.user_id = user_id;
        this.morning = morning;
        this.afternoon = afternoon;
        this.evening = evening;
        this.snooze_time = snooze_time;
    }

    // Getters
    public int getUser_id() {
        return user_id;
    }

    public String getMorning() {
        return morning;
    }

    public String getAfternoon() {
        return afternoon;
    }

    public String getEvening() {
        return evening;
    }

    public String getSnooze_time() {
        return snooze_time;
    }

    // Setters
    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public void setMorning(String morning) {
        this.morning = morning;
    }

    public void setAfternoon(String afternoon) {
        this.afternoon = afternoon;
    }

    public void setEvening(String everning) {
        this.evening = everning;
    }

    public void setSnooze_time(String snooze_time) {
        this.snooze_time = snooze_time;
    }

    // Object string
    @Override
    public String toString() {
        return "OtherSettingsTable{" +
                "user id=" + user_id +
                ", morning=" + morning +
                ", afternoon=" + afternoon +
                ", everning=" + evening +
                ", snooze_time='" + snooze_time + '\'' +
                '}';
    }
}
