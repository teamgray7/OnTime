package com.unioulu.ontime;

import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

import com.unioulu.ontime.database_classes.AppDatabase;
import com.unioulu.ontime.database_classes.DataHolder;
import com.unioulu.ontime.database_classes.Medicines;
import com.unioulu.ontime.helper.AlarmSharedData;
import com.unioulu.ontime.helper.DateTimeConverter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class AlarmService extends Service {
    final private static String TAG = "AlarmService";
    private final IBinder binder = new AlarmServiceBinder();
    Handler handler;

    class AlarmServiceBinder extends Binder {
        AlarmService getService() {
            return AlarmService.this;
        }
    }

    public AlarmService() {
    }

    static boolean isRunning(ActivityManager mgr) {
        Class<AlarmService> serviceClass = AlarmService.class;
        String name = serviceClass.getName();
        boolean classFound = false;

        for(ActivityManager.RunningServiceInfo srv: mgr.getRunningServices(Integer.MAX_VALUE)) {
            if (name.equals(srv.service.getClassName())) {
                Log.d(TAG, "Service " + name + " is running already");
                classFound = true;
            }
        }
        return classFound;
    }

    /* Set alarm that launches AlarmActivity.
     * alarmTime sets when alarm goes off. Times pointing to the past goes off immediately.
     * requestCode differentiates between alarms, 0, 1, 2 are reserved for morning, afternoon and evening. */
    public boolean setAlarm(long alarmTime, int requestCode) {
        Intent intent = new Intent(getApplicationContext(), AlarmActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        AlarmManager alarmMgr = (AlarmManager)getSystemService(ALARM_SERVICE);

        long diff = alarmTime-System.currentTimeMillis();
        Log.d(TAG, "TIMEDIFF: " + String.valueOf(diff));

        boolean alarmSet = (PendingIntent.getActivity(getApplicationContext(), requestCode, intent, PendingIntent.FLAG_NO_CREATE) != null);

        if (!alarmSet) {
            PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), requestCode, intent, 0);

            if (Build.VERSION.SDK_INT < 23)
                alarmMgr.setExact(AlarmManager.RTC_WAKEUP, alarmTime, pendingIntent);
            else
                alarmMgr.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, alarmTime, pendingIntent);

            Log.d(TAG, "Alarm has been set.");
        }
        else {
            Log.d(TAG, "Alarm already set!");
        }

        return alarmSet;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                switch (msg.what) {
                    case 0:
                        Bundle bundle = msg.getData();
                        Log.d(TAG, "longtime: " + bundle.getLong("AlarmTime") + " request: " + bundle.getInt("RequestCode"));
                        long alarmTime = bundle.getLong("AlarmTime");
                        int requestCode = bundle.getInt("RequestCode");

                        setAlarm(alarmTime, requestCode);
                        break;
                    default:
                        Log.d(TAG, "Unhandled message!");
                }
                return false;
            }
        });

        final UpdateAlarmsThread updateThread = new UpdateAlarmsThread(handler);
        updateThread.start();

        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }
}

class UpdateAlarmsThread extends Thread {
    final private static String TAG = "UpdateAlarmsThread";
    private Runnable serviceRunnable = null;
    private Handler messageHandler;

    public UpdateAlarmsThread(Handler handler) {
        messageHandler = handler;
    }

    private long getStartofDay() {
        Calendar calendar = Calendar.getInstance();
        int y = calendar.get(Calendar.YEAR);
        int m = calendar.get(Calendar.MONTH);
        int d = calendar.get(Calendar.DATE);

        calendar.set(y, m, d,0, 0, 0);

        return calendar.getTime().getTime();
    }

    @Override
    public void run() {

        while (true) {
            try {
                UpdateAlarmsThread.sleep(10000);
            }
            catch (InterruptedException ie) {
                Log.d(TAG, "Interrupt received.");
            }

            Log.d(TAG, "Setting alarms...");

            final AppDatabase appDatabase = DataHolder.getInstance().getAppDatabase();

            List<String> active_user = appDatabase.usersTableInterface().getActiveUsers(true);
            if (active_user.size() > 0) {
                final int active_user_id = appDatabase.usersTableInterface().getUserIdByName(active_user.get(active_user.size() - 1));

                Date cDateMorning = new Date();
                Date cDateAfternoon = new Date();
                Date cDateEvening = new Date();
                Date currentTime = new Date();
                Date morningTime, afternoonTime, eveningTime;
                // If the data is not yet retrieved from DB or it was changed, get it again from DB and update the shared preferences
                if (AlarmSharedData.getInstance().getIsDataChanged() || !(AlarmSharedData.getInstance().getIsDataRetrieved()))
                {

                    Log.d(TAG, "Retrieving data from DB: changed: "+
                            String.valueOf(AlarmSharedData.getInstance().getIsDataChanged()) +
                            " Data retrieved: " + String.valueOf(AlarmSharedData.getInstance().getIsDataRetrieved()));

                    morningTime = DateTimeConverter.fromTimestamp(appDatabase.otherSettingsInterface().fetchMorningTime(active_user_id));
                    afternoonTime = DateTimeConverter.fromTimestamp(appDatabase.otherSettingsInterface().fetchAfternoonTime(active_user_id));
                    eveningTime = DateTimeConverter.fromTimestamp(appDatabase.otherSettingsInterface().fetchEveningTime(active_user_id));
                    // Update the shared preferences
                    AlarmSharedData.getInstance().setMorningTime(morningTime);
                    AlarmSharedData.getInstance().setAfternoonTime(afternoonTime);
                    AlarmSharedData.getInstance().setEveningTime(eveningTime);
                    AlarmSharedData.getInstance().setIsDataChanged(false);
                    AlarmSharedData.getInstance().setIsDataRetrieved(true);
                }
                else // Data is retrieved from DB, had not been changed and a snooze might be set !
                {
                    Log.d(TAG, "Retrieving data from shared preferences");
                    morningTime = AlarmSharedData.getInstance().getMorningTime();
                    afternoonTime = AlarmSharedData.getInstance().getAfternoonTime();
                    eveningTime = AlarmSharedData.getInstance().getEveningTime();
                }

                // Shield against non-existant user settings
                if (morningTime != null && afternoonTime != null && eveningTime != null) {
                    cDateMorning.setHours(morningTime.getHours());
                    cDateMorning.setMinutes(morningTime.getMinutes());
                    cDateAfternoon.setHours(afternoonTime.getHours());
                    cDateAfternoon.setMinutes(afternoonTime.getMinutes());
                    cDateEvening.setHours(eveningTime.getHours());
                    cDateEvening.setMinutes(eveningTime.getMinutes());

                    long alarmTime;

                    // Time for testing
               /* Date date = new GregorianCalendar(2019, Calendar.APRIL, 24, 13, 49).getTime();
                morningTime = date.getTime();
                afternoonTime = morningTime + 120000;
                eveningTime = afternoonTime + 120000;*/

                    // Serves as requestcode for PendingIntent
                    int requestCode;

                    Log.d(TAG, cDateEvening.toString());

                    if (currentTime.getHours() < morningTime.getHours() ||
                            (currentTime.getHours() == morningTime.getHours() &&
                                    currentTime.getMinutes() < morningTime.getMinutes())) {
                        requestCode = 0;
                        alarmTime = cDateMorning.getTime();
                        // Update shared preferences with pill time
                        AlarmSharedData.getInstance().setPillTime(requestCode);
                    } else if (currentTime.getHours() < afternoonTime.getHours() ||
                            (currentTime.getHours() == afternoonTime.getHours() &&
                                    currentTime.getMinutes() < afternoonTime.getMinutes())) {
                        requestCode = 1;
                        alarmTime = cDateAfternoon.getTime();
                        // Update shared preferences with pill time
                        AlarmSharedData.getInstance().setPillTime(requestCode);
                    } else if (currentTime.getHours() < eveningTime.getHours() ||
                            (currentTime.getHours() == eveningTime.getHours() &&
                                    currentTime.getMinutes() < eveningTime.getMinutes())) {
                        requestCode = 2;
                        alarmTime = cDateEvening.getTime();
                        // Update shared preferences with pill time
                        AlarmSharedData.getInstance().setPillTime(requestCode);
                    } else {
                        requestCode = 3;
                        alarmTime = 0;
                        Log.d(TAG, "No alarms for this day.");
                    }


                    String medicineName;

                    List<String> medicines;
                    if (requestCode == 0) {
                        medicines = appDatabase.medicineDBInterface().fetchMorningPills(active_user_id);
                        // Set the shared preferences
                        AlarmSharedData.getInstance().setMedicineName(medicines);

                        Log.d(TAG, "Amount : " + String.valueOf(medicines.size()));
                    } else if (requestCode == 1) {
                        medicines = appDatabase.medicineDBInterface().fetchAfternoonPills(active_user_id);
                        // Set the shared preferences
                        AlarmSharedData.getInstance().setMedicineName(medicines);

                    } else if (requestCode == 2) {
                        medicines = appDatabase.medicineDBInterface().fetchEveningPills(active_user_id);
                        // Set the shared preferences
                        AlarmSharedData.getInstance().setMedicineName(medicines);

                    } else {
                        // Initialize list in any case
                        medicines = Arrays.asList(new String("Emptylist"));
                    }

                    // Execute only if alarms left for the day
                    if (requestCode < 3 && alarmTime >= 0) {
                        // Print meds
                        for (String medicine : medicines) {
                            Log.d(TAG, "MED: " + medicine);

                            Medicines med = appDatabase.medicineDBInterface().fetchOneMedicineByName(medicine, active_user_id);
                            int morning = med.getMorningAt();

                            Log.d(TAG, "Morning int: " + morning + " Alarmtime: " + String.valueOf(alarmTime));
                        }
                        Bundle bundle = new Bundle();
                        bundle.putLong("AlarmTime", alarmTime);
                        bundle.putInt("RequestCode", requestCode);
                        Message msg = new Message();
                        msg.what = 0;
                        msg.setData(bundle);
                        messageHandler.sendMessage(msg);

                    }
                }
                else
                    Log.d(TAG, "BUG! User has no default morning, afternoon or evening times.");

            }
        }
    }
}