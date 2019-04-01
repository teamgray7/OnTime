package com.unioulu.ontime.database_classes;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;
import static android.arch.persistence.room.ForeignKey.CASCADE;


// Each user ID has a single set of saved settings

@Entity(indices={@Index(value="user_id", unique=true)})
public class OtherSettingsTable {
    @NonNull
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "user_id")
    private int user_id;

    @ColumnInfo(name = "morning")
    private Long morning;

    @ColumnInfo(name = "afternoon")
    private Long afternoon;

    @ColumnInfo(name = "evening")
    private Long everning;

    @ColumnInfo(name = "custom")
    private Long custom;

    @ColumnInfo(name = "snooze_time")
    private String snooze_time;


    // Constructors
    @Ignore
    public OtherSettingsTable(){} // Empty constructor

    public OtherSettingsTable(int user_id, Long morning, Long afternoon, Long everning, Long custom, String snooze_time) {
        this.user_id = user_id;
        this.morning = morning;
        this.afternoon = afternoon;
        this.everning = everning;
        this.custom = custom;
        this.snooze_time = snooze_time;
    }


    // Getters
    public int getUser_id() {
        return user_id;
    }

    public Long getMorning() {
        return morning;
    }

    public Long getAfternoon() {
        return afternoon;
    }

    public Long getEverning() {
        return everning;
    }

    public Long getCustom() {
        return custom;
    }

    public String getSnooze_time() {
        return snooze_time;
    }

    // Setters
    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public void setMorning(Long morning) {
        this.morning = morning;
    }

    public void setAfternoon(Long afternoon) {
        this.afternoon = afternoon;
    }

    public void setEverning(Long everning) {
        this.everning = everning;
    }

    public void setCustom(Long custom) {
        this.custom = custom;
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
                ", everning=" + everning +
                ", custom=" + custom +
                ", snooze_time='" + snooze_time + '\'' +
                '}';
    }
}
