package com.unioulu.ontime.database_classes;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

// This table is linked to a specific user
@Entity(indices={@Index(value="medicine_name", unique=true)})
public class Medicines {

    @ColumnInfo(name = "medicine_id")
    private int medicine_id;

    @NonNull
    @PrimaryKey
    @ColumnInfo(name = "medicine_name")
    private String medicine_name;

    @ColumnInfo(name = "medicine_amount")
    private String medicine_amount;

    @ColumnInfo(name = "picture_path")
    private String picture_path;

    @ColumnInfo(name = "morningAt")
    private int morningAt;

    @ColumnInfo(name = "afternoonAt")
    private int afternoonAt;

    @ColumnInfo(name = "everingAt")
    private int everingAt;

    @ColumnInfo(name = "customAt")
    private int customAt;

    // Constructors
    @Ignore
    public Medicines(){}    // Empty constructor

    public Medicines(int medicine_id, String medicine_name, String medicine_amount, String picture_path, int morningAt, int afternoonAt, int everingAt, int customAt) {
        this.medicine_id = medicine_id;
        this.medicine_name = medicine_name;
        this.medicine_amount = medicine_amount;
        this.picture_path = picture_path;
        this.morningAt = morningAt;
        this.afternoonAt = afternoonAt;
        this.everingAt = everingAt;
        this.customAt = customAt;
    }

    // Getters
    public int getMedicine_id() {
        return medicine_id;
    }

    public String getMedicine_name() {
        return medicine_name;
    }

    public String getMedicine_amount() {
        return medicine_amount;
    }

    public String getPicture_path() {
        return picture_path;
    }

    public int getMorningAt() {
        return morningAt;
    }

    public int getAfternoonAt() {
        return afternoonAt;
    }

    public int getEveringAt() {
        return everingAt;
    }

    public int getCustomAt() {
        return customAt;
    }

    // Setters
    public void setMedicine_id(int medicine_id) {
        this.medicine_id = medicine_id;
    }

    public void setMedicine_name(String medicine_name) {
        this.medicine_name = medicine_name;
    }

    public void setMedicine_amount(String medicine_amount) {
        this.medicine_amount = medicine_amount;
    }

    public void setPicture_path(String picture_path) {
        this.picture_path = picture_path;
    }

    public void setMorningAt(int morningAt) {
        this.morningAt = morningAt;
    }

    public void setAfternoonAt(int afternoonAt) {
        this.afternoonAt = afternoonAt;
    }

    public void setEveringAt(int everingAt) {
        this.everingAt = everingAt;
    }

    public void setCustomAt(int customAt) {
        this.customAt = customAt;
    }

    // To String

    @Override
    public String toString() {
        return "Medicines{" +
                "id=" + medicine_id +
                ", medicine_name='" + medicine_name + '\'' +
                ", picture_path='" + picture_path + '\'' +
                ", morningAt='" + morningAt + '\'' +
                ", afternoonAt='" + afternoonAt + '\'' +
                ", everingAt='" + everingAt + '\'' +
                ", customAt='" + customAt + '\'' +
                '}';
    }
}
