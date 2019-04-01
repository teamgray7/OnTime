package com.unioulu.ontime.database_classes;

public class DataHolder {
    private AppDatabase appDatabase;
    public AppDatabase getAppDatabase() {return appDatabase;}
    public void setAppDatabase(AppDatabase appDatabase) {this.appDatabase= appDatabase;}

    private static final DataHolder holder = new DataHolder();
    public static DataHolder getInstance() {return holder;}
}