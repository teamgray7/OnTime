package com.unioulu.ontime;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class AlarmService extends Service {
    final private static String TAG = "AlarmService";

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

        final UpdateAlarmsThread updateThread = new UpdateAlarmsThread();
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

    @Override
    public void run() {

        while (true) {
            Log.d(TAG, "Setting alarms...");

            try {
                UpdateAlarmsThread.sleep(60000);
            }
            catch (InterruptedException ie) {
                Log.d(TAG, "Interrupt received.");
            }
        }
    }
}