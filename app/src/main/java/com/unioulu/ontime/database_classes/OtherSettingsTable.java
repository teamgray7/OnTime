package com.unioulu.ontime.database_classes;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;
import static android.arch.persistence.room.ForeignKey.CASCADE;


@Entity(foreignKeys = @ForeignKey(onDelete = CASCADE,
        entity = UsersTable.class, parentColumns = "user_id",
        childColumns = "id"))
public class OtherSettingsTable {
    @NonNull
    @PrimaryKey(autoGenerate = true)
    private int id;

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

    public OtherSettingsTable(Long morning, Long afternoon, Long everning, Long custom, String snooze_time) {
        this.morning = morning;
        this.afternoon = afternoon;
        this.everning = everning;
        this.custom = custom;
        this.snooze_time = snooze_time;
    }


    // Getters
    public int getId() {
        return id;
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
    public void setId(int id) {
        this.id = id;
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
                "id=" + id +
                ", morning=" + morning +
                ", afternoon=" + afternoon +
                ", everning=" + everning +
                ", custom=" + custom +
                ", snooze_time='" + snooze_time + '\'' +
                '}';
    }
}
