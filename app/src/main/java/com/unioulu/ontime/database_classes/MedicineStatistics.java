package com.unioulu.ontime.database_classes;


import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(foreignKeys = @ForeignKey(onDelete = CASCADE,
        entity = Medicines.class, parentColumns = "medicine_id", childColumns = "id"))
public class MedicineStatistics {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name="date")
    private Long date;

    @ColumnInfo(name = "status")
    private String status;


    // Constructors
    @Ignore
    public MedicineStatistics(){} // Empty constructor

    public MedicineStatistics(Long date, String status) {
        this.date = date;
        this.status = status;
    }


    //Getters


    public int getId() {
        return id;
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
                ", date=" + date +
                ", status='" + status + '\'' +
                '}';
    }
}
