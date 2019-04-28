package com.unioulu.ontime;

import android.app.ActivityManager;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.ParcelUuid;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.unioulu.ontime.fragment.PillAlarmFragment;
import com.unioulu.ontime.fragment.PillMissedFragment;
import com.unioulu.ontime.fragment.PillTakenFragment;

import java.io.IOException;
import java.io.InputStream;
import java.util.Set;
import java.util.UUID;


public class AlarmActivity extends AppCompatActivity
    implements PillAlarmFragment.OnFragmentInteractionListener,
    PillTakenFragment.OnFragmentInteractionListener,
    PillMissedFragment.OnFragmentInteractionListener {

    private Intent alarmSrvIntent;
    private AlarmService alarmSrv = null;

    private PushButtonThread thread=null;

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

        Handler handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                switch (msg.what)
                {
                    case 110:
                        Log.d("ALARMACTIVITY","Success with msg");
                        PillTakenFragment fragment = PillTakenFragment.newInstance();
                        getSupportFragmentManager().beginTransaction()
                                .add(R.id.alarm_content, fragment)
                                .commit();
                        return true;
                    default:
                        Log.d("ALARMACTIVITY", "Not handled msg");
                        return false;
                }
            }
        });
        final int REQUEST_ENABLE_BT = 1;
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            Log.d("ALARMACTIVITY", "No bluetooth adapters");
        }
        else if (!bluetoothAdapter.isEnabled()){
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }
        else {
            Set<BluetoothDevice> paired = bluetoothAdapter.getBondedDevices();
            if (paired.size() > 0) {
                for (BluetoothDevice device: paired) {
                    Log.d("ALARMACTIVITY", "DEVNAME: " + device.getName());
                    Log.d("ALARMACTIVITY", "DEVADDR: " + device.getAddress());
                    thread = new PushButtonThread(bluetoothAdapter, device, handler);
                    thread.start();
                    break;
                }
            }
            else Log.d("ALARMACTIVITY", "No paired devices");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK) {
            Log.d("ALARMACTIVITY","BLUETOOTH enabled.");
        }
        else if (resultCode == RESULT_CANCELED) {
            Log.d("ALARMACTIVITY", "Bluetooth couldn't be enabled.");
        }
        else Log.d("ALARMACTIVITY", "Should not happen?");
    }

    @Override
    protected void onDestroy() {
        unbindService(connection);

        if (thread != null) {
            thread.disconnect();

            try {
                thread.join();
            }
            catch (InterruptedException iexp) {
                Log.d("ALARMACTIVITY", "Bluetooth thread join() interrupted.");
            }

            thread = null;
        }

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

    @Override
    public void onBackPressed() {
// super.onBackPressed();
// Not calling **super**, disables back button in current screen.
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

    @Override
    public void pillTaken() {

        finish();
    }
}

class PushButtonThread extends Thread {
    private static final String TAG = "PUSHBUTTONTHREAD";
    private BluetoothAdapter adapter;
    //private BluetoothDevice device;
    private BluetoothSocket sck;
    private final String uuidStr;
    private Handler handler;

    public PushButtonThread(BluetoothAdapter adapter, BluetoothDevice device, Handler handler) {

        uuidStr = "080aefda-617e-4972-a257-a6cfa0e1c33b";

        this.handler = handler;
        //this.device = device;
        this.adapter = adapter;

        Log.d(TAG, "Device name: " + device.getName());
        ParcelUuid[] uuids = device.getUuids();
        for(int i=0; i < uuids.length; i++)
            Log.d(TAG, "uuid: " + uuids[0].toString());//String.valueOf(uuids.length));

        try {
            sck = device.createRfcommSocketToServiceRecord(UUID.fromString(uuidStr));
        }
        catch (IOException ioe) {
            Log.d(TAG, "Creating rfcomm-socket failed.");
            sck = null;
            return;
        }
        Log.d(TAG, "Socket created.");
    }

    public void disconnect() {
        if (sck != null) {
            try {
                sck.close();
            } catch (IOException ioe) {
                Log.d(TAG, "Close() failed.");
            }
            sck = null;
            Log.d(TAG, "Bluetooth socket closed.");
        }
    }

    @Override
    public void run() {
        InputStream in;

        // Fixme: is sleep() necessary?
        try {
            Thread.sleep(1000);
        }
        catch (InterruptedException intexp) {
            Log.d(TAG, "sleep() interrupted.");
        }

        adapter.cancelDiscovery();

        try {
            sck.connect();
        }
        catch (IOException ioe) {
            Log.d(TAG, "Connecting failed.");

            try {
                sck.close();
            }
            catch (IOException ioe2) {
                Log.d(TAG, "Closing socket failed.");
            }
            return;
        }
        Log.d(TAG, "Connected...");

        try {
            in = sck.getInputStream();
        }
        catch (IOException ioe) {
            Log.d(TAG, "Couldn't get a input stream from socket.");

            try {
                sck.close();
            }
            catch (IOException ioe2) {
                Log.d(TAG, "Closing socket failed.");
            }
            return;
        }

        Log.d(TAG, "Starting to read.");
        int retval=0;

        while(retval != -1) {
            try {
                int value = in.read();

                Log.d(TAG, "Received something.");
                if (value == 1) {
                    Log.d(TAG, "Button pressed!");
                    handler.sendEmptyMessage(110);
                }
            }
            catch (IOException ioe) {
                Log.d(TAG, "Error while reading.");
                break;
            }
        }
        Log.d(TAG, "Proceeding to close the socket.");
        try {
            sck.close();
        }
        catch (IOException ioe) {
            Log.d(TAG, "Closing socket failed.");
        }
        catch (NullPointerException npe) {
            Log.d(TAG, "disconnect called from other thread.");
        }
        sck = null;
    }
}