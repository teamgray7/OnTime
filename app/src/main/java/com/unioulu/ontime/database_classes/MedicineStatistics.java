package com.unioulu.ontime.database_classes;


import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity
public class MedicineStatistics {
    @NonNull
    @PrimaryKey
    private int id; // UserID

    @ColumnInfo(name = "medicine_name")
    private String medicine_name;

    @ColumnInfo(name = "date")
    private Long date;

    @ColumnInfo(name = "status")
    private String status;


    // Constructors
    @Ignore
    public MedicineStatistics() {
    } // Empty constructor

    public MedicineStatistics(int id, String medicine_name, Long date, String status) {
        this.id = id;
        this.medicine_name = medicine_name;
        this.date = date;
        this.status = status;
    }


    //Getters


    public int getId() {
        return id;
    }

    public String getMedicine_name() {
        return medicine_name;
    }

    public Long getDate() {
        return date;
    }

    public String getStatus() {
        return status;
    }

    // Setters


    public void setId(int id) {
        this.id = id;
    }

    public void setMedicine_name(String medicine_name) {
        this.medicine_name = medicine_name;
    }

    public void setDate(Long date) {
        this.date = date;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    // ToString
    @Override
    public String toString() {
        return "MedicineStatistics{" +
                "id=" + id +
                ", medicine_name='" + medicine_name + '\'' +
                ", date=" + date +
                ", status='" + status + '\'' +
                '}';
    }
}