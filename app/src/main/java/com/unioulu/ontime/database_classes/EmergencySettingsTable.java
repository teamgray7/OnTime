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
        childColumns = "emer_contact_id"))
public class EmergencySettingsTable {
    @NonNull
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "emer_contact_id")
    private int emer_contact_id;

    @ColumnInfo(name = "contact_name")
    private String contact_name;

    @ColumnInfo(name = "phone_number")
    private String phone_number;

    @ColumnInfo(name = "picture_url")
    private String picture_url;

    // Constructors
    @Ignore
    public EmergencySettingsTable(){}

    public EmergencySettingsTable(String contact_name, String phone_number, String picture_url) {
        this.contact_name = contact_name;
        this.phone_number = phone_number;
        this.picture_url = picture_url;
    }

    // Getters

    public int getEmer_contact_id() {
        return emer_contact_id;
    }

    public String getContact_name() {
        return contact_name;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public String getPicture_url() {
        return picture_url;
    }

    // Setters

    public void setEmer_contact_id(int emer_contact_id) {
        this.emer_contact_id = emer_contact_id;
    }

    public void setContact_name(String contact_name) {
        this.contact_name = contact_name;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public void setPicture_url(String picture_url) {
        this.picture_url = picture_url;
    }

    // Object to string for debugging

    @Override
    public String toString() {
        return "EmergencySettingsTable{" +
                "emer_contact_id=" + emer_contact_id +
                ", contact_name='" + contact_name + '\'' +
                ", phone_number='" + phone_number + '\'' +
                ", picture_url='" + picture_url + '\'' +
                '}';
    }
}
