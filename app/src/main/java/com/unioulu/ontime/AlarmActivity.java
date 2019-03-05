package com.unioulu.ontime;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.unioulu.ontime.fragment.PillAlarmFragment;
import com.unioulu.ontime.fragment.PillTakenFragment;

public class AlarmActivity extends AppCompatActivity
    implements PillAlarmFragment.OnFragmentInteractionListener,
    PillTakenFragment.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);

        PillAlarmFragment loginPasswordFragment = PillAlarmFragment.newInstance();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.alarm_content, loginPasswordFragment)
                .commit();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }
}
