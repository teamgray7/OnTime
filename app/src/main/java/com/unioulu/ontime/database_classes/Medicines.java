package com.unioulu.ontime.database_classes;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.util.Date;

import static android.arch.persistence.room.ForeignKey.CASCADE;
@Entity
public class Medicines {

    @NonNull
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "medicine_id")
    private int id;

    @ColumnInfo(name = "medicine_name")
    private String medicine_name;

    @ColumnInfo(name = "picture_path")
    private String picture_path;

    @ColumnInfo(name = "morningAt")
    private String morningAt;

    @ColumnInfo(name = "afternoonAt")
    private String afternoonAt;

    @ColumnInfo(name = "everingAt")
    private String everingAt;

    @ColumnInfo(name = "customAt")
    private String customAt;

    // Constructors
    @Ignore
    public Medicines(){}    // Empty constructor

    public Medicines(String medicine_name, String picture_path, String morningAt, String afternoonAt, String everingAt, String customAt) {
        this.medicine_name = medicine_name;
        this.picture_path = picture_path;
        this.morningAt = morningAt;
        this.afternoonAt = afternoonAt;
        this.everingAt = everingAt;
        this.customAt = customAt;
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getMedicine_name() {
        return medicine_name;
    }

    public String getPicture_path() {
        return picture_path;
    }

    public String getMorningAt() {
        return morningAt;
    }

    public String getAfternoonAt() {
        return afternoonAt;
    }

    public String getEveringAt() {
        return everingAt;
    }

    public String getCustomAt() {
        return customAt;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setMedicine_name(String medicine_name) {
        this.medicine_name = medicine_name;
    }

    public void setPicture_path(String picture_path) {
        this.picture_path = picture_path;
    }

    public void setMorningAt(String morningAt) {
        this.morningAt = morningAt;
    }

    public void setAfternoonAt(String afternoonAt) {
        this.afternoonAt = afternoonAt;
    }

    public void setEveringAt(String everingAt) {
        this.everingAt = everingAt;
    }

    public void setCustomAt(String customAt) {
        this.customAt = customAt;
    }

    // To String

    @Override
    public String toString() {
        return "Medicines{" +
                "id=" + id +
                ", medicine_name='" + medicine_name + '\'' +
                ", picture_path='" + picture_path + '\'' +
                ", morningAt='" + morningAt + '\'' +
                ", afternoonAt='" + afternoonAt + '\'' +
                ", everingAt='" + everingAt + '\'' +
                ", customAt='" + customAt + '\'' +
                '}';
    }
}
