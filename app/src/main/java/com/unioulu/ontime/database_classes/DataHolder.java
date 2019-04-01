package com.unioulu.ontime.database_classes;

public class DataHolder {
    private AppDatabase appDatabase;
    private String username;
    private int user_id;

    public AppDatabase getAppDatabase() {return appDatabase;}

    public String getUsername() {
        return username;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setAppDatabase(AppDatabase appDatabase) {this.appDatabase= appDatabase;}

    public void setUsername(String username) {
        this.username = username;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    private static final DataHolder holder = new DataHolder();
    public static DataHolder getInstance() {return holder;}
}