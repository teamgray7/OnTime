package com.unioulu.ontime.database_classes;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(indices = {@Index(value = {"email"}, unique = true)})
public class UsersTable {

    @NonNull
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "user_id")
    private int user_id;


    @ColumnInfo(name = "username")
    private String username;

    @ColumnInfo(name = "email")
    private String email;

    @ColumnInfo(name = "password")
    private String password;

    @ColumnInfo(name = "active")
    private Boolean isUserActive;

    // Constructor
    @Ignore
    public UsersTable(){}

    public UsersTable(String username, String email, String password, Boolean isUserActive) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.isUserActive = isUserActive;
    }

    // Getters

    public int getUser_id() {
        return user_id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public Boolean getUserActive() {
        return isUserActive;
    }
    // Setters

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUserActive(Boolean userActive) {
        isUserActive = userActive;
    }
    // Object To string (for debugging)


    @Override
    public String toString() {
        return "UsersTable{" +
                "user_id=" + user_id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", isUserActive=" + isUserActive +
                '}';
    }
}
