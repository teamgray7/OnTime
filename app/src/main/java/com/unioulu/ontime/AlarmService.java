package com.unioulu.ontime;

import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

import com.unioulu.ontime.database_classes.AppDatabase;
import com.unioulu.ontime.database_classes.DataHolder;
import com.unioulu.ontime.database_classes.Medicines;

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
    Handler handler;

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

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 0:
                        Bundle bundle = msg.getData();
                        Log.d(TAG, "longtime: " + bundle.getLong("AlarmTime") + " request: " + bundle.getInt("RequestCode"));
                        Intent intent = new Intent(getApplicationContext(), AlarmActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                        AlarmManager alarmMgr = (AlarmManager)getSystemService(ALARM_SERVICE);

                        long diff = bundle.getLong("AlarmTime")-System.currentTimeMillis();
                        Log.d(TAG, "TIMEDIFF: " + String.valueOf(diff));

                        int requestCode = bundle.getInt("RequestCode");
                        boolean alarmSet = (PendingIntent.getActivity(getApplicationContext(), requestCode, intent, PendingIntent.FLAG_NO_CREATE) != null);

                        if (!alarmSet) {
                            PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), requestCode, intent, 0);

                            if (Build.VERSION.SDK_INT < 23)
                                alarmMgr.setExact(AlarmManager.RTC_WAKEUP, bundle.getLong("AlarmTime"), pendingIntent);
                            else
                                alarmMgr.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, bundle.getLong("AlarmTime"), pendingIntent);
                            Log.d(TAG, "Alarm has been set.");
                        }
                        else {
                            Log.d(TAG, "Alarm already set!");
                        }
                        break;
                    default:
                        Log.d(TAG, "Unhandled message!");
                }
            }
        };

        final UpdateAlarmsThread updateThread = new UpdateAlarmsThread(handler);
        updateThread.start();

        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not implemented");
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
                UpdateAlarmsThread.sleep(60000);
            }
            catch (InterruptedException ie) {
                Log.d(TAG, "Interrupt received.");
            }

            Log.d(TAG, "Setting alarms...");

            final AppDatabase appDatabase = DataHolder.getInstance().getAppDatabase();

            List<String> active_user = appDatabase.usersTableInterface().getActiveUsers(true);
            if (active_user.size() > 0) {
                final int active_user_id = appDatabase.usersTableInterface().getUserIdByName(active_user.get(active_user.size() - 1));

                long currentTime = System.currentTimeMillis();
                long startOfDay = getStartofDay();
                long morningTime = appDatabase.otherSettingsInterface().fetchMorningTime(active_user_id) + startOfDay;
                long afternoonTime = appDatabase.otherSettingsInterface().fetchAfternoonTime(active_user_id) + startOfDay;
                long eveningTime = appDatabase.otherSettingsInterface().fetchEveningTime(active_user_id) + startOfDay;
                long alarmTime;

                // Time for testing
/*                Date date = new GregorianCalendar(2019, Calendar.APRIL, 24, 13, 49).getTime();
                morningTime = date.getTime();
                afternoonTime = morningTime + 120000;
                eveningTime = afternoonTime + 120000;*/

                // Serves as requestcode for PendingIntent
                int requestCode;

                if (currentTime < morningTime) {
                    requestCode = 0;
                    alarmTime = morningTime;
                } else if (currentTime > morningTime && currentTime < afternoonTime) {
                    requestCode = 1;
                    alarmTime = afternoonTime;
                } else if (currentTime > afternoonTime && currentTime < eveningTime) {
                    requestCode = 2;
                    alarmTime = eveningTime;
                } else {
                    requestCode = 3;
                    alarmTime = 0;
                    Log.d(TAG, "No alarms for this day.");
                }

                String medicineName;

                List<String> medicines;
                if (requestCode == 0) {
                    medicines = appDatabase.medicineDBInterface().fetchMorningPills(active_user_id);
                } else if (requestCode == 1) {
                    medicines = appDatabase.medicineDBInterface().fetchAfternoonPills(active_user_id);
                } else if (requestCode == 2) {
                    medicines = appDatabase.medicineDBInterface().fetchEveningPills(active_user_id);
                }
                else {
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
        }
    }
}