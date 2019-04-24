package com.unioulu.ontime;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.unioulu.ontime.fragment.PillAlarmFragment;
import com.unioulu.ontime.fragment.PillMissedFragment;
import com.unioulu.ontime.fragment.PillTakenFragment;


public class AlarmActivity extends AppCompatActivity
    implements PillAlarmFragment.OnFragmentInteractionListener,
    PillTakenFragment.OnFragmentInteractionListener,
    PillMissedFragment.OnFragmentInteractionListener {

    private Intent alarmSrvIntent;
    private AlarmService alarmSrv = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);

        PillAlarmFragment pillAlarmFragment = PillAlarmFragment.newInstance();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.alarm_content, pillAlarmFragment)
                .commit();

        alarmSrvIntent = new Intent(this, AlarmService.class);
        // If OnTime is not whitelisted in battery settings, service is not running.
        ActivityManager manager = (ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);
        if (!AlarmService.isRunning(manager)) {
            alarmSrv = new AlarmService();
            startService(alarmSrvIntent);
        }
        bindService(alarmSrvIntent, connection, Context.BIND_AUTO_CREATE);

    }

    @Override
    protected void onDestroy() {
        unbindService(connection);
        super.onDestroy();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void snooze(long snoozeDate) {
        if (alarmSrv != null)
            // Using unused requestCode.
            alarmSrv.setAlarm(snoozeDate, 3);
    }

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            AlarmService.AlarmServiceBinder binder = (AlarmService.AlarmServiceBinder)service;
            alarmSrv = binder.getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            alarmSrv = null;
        }
    };
}
