package com.unioulu.ontime.helper;

import java.util.Date;
import java.util.List;

public class AlarmSharedData {
    private List<String> medicine_name;
    private boolean isDataChanged = false, isDataRetrieved=false, isSnoozeSet=false;
    private int pillTime = 4; // 0: morning, 1: afternoon, 2: evening
    private Date morningTime, afternoonTime, eveningTime;


    public int getPillTime() {
        return pillTime;
    }

    public void setPillTime(int pillTime) {
        this.pillTime = pillTime;
    }

    public Date getMorningTime() {
        return morningTime;
    }

    public Date getAfternoonTime() {
        return afternoonTime;
    }

    public Date getEveningTime() {
        return eveningTime;
    }

    public void setMorningTime(Date morningTime) {
        this.morningTime = morningTime;
    }

    public void setAfternoonTime(Date afternoonTime) {
        this.afternoonTime = afternoonTime;
    }

    public void setEveningTime(Date eveningTime) {
        this.eveningTime = eveningTime;
    }

    public List<String> getMedicineName() {
            return medicine_name;
        }

        public boolean getIsDataChanged() {
            return isDataChanged;
        }
        public boolean getIsDataRetrieved() {
            return isDataRetrieved;
        }


        public void setMedicineName(List<String> medicine_name) {
            this.medicine_name = medicine_name;
        }

        public void setIsDataChanged(boolean isDataChanged) {
            this.isDataChanged = isDataChanged;
        }
        public void setIsDataRetrieved(boolean isDataRetrieved) {
            this.isDataRetrieved= isDataRetrieved;
        }

        private static final AlarmSharedData alarmSharedData = new AlarmSharedData();
        public static AlarmSharedData getInstance() {return alarmSharedData;}

}
