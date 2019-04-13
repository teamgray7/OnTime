package com.unioulu.ontime.fragment;

import android.annotation.SuppressLint;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TimePicker;
import com.unioulu.ontime.R;
import com.unioulu.ontime.database_classes.AppDatabase;
import com.unioulu.ontime.database_classes.DataHolder;
import com.unioulu.ontime.database_classes.OtherSettingsTable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OtherSettingsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link OtherSettingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OtherSettingsFragment extends Fragment {

    // Fragment's buttons !
    private Button morningBtn, afternoonBtn, eveningBtn, customBtn, saveBtn, cancelBtn;

    // Fragment's radioButtons
    private RadioButton[] snoozeRbtn; // 5 buttons in total

    private OnFragmentInteractionListener mListener;

    public OtherSettingsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment OtherSettingsFragment.
     */
    public static OtherSettingsFragment newInstance() {
        return new OtherSettingsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_other_settings, container, false);

        // Initialization of buttons
        morningBtn   = rootView.findViewById(R.id.morningBtn);
        afternoonBtn = rootView.findViewById(R.id.afternoonBtn);
        eveningBtn   = rootView.findViewById(R.id.eveningBtn);
        customBtn    = rootView.findViewById(R.id.customBtn);
        saveBtn      = rootView.findViewById(R.id.saveBtn);
        cancelBtn    = rootView.findViewById(R.id.cancelBtn);

        // Initialization of radio buttons
        snoozeRbtn = new RadioButton[]{
                rootView.findViewById(R.id.s5Rbtn),
                rootView.findViewById(R.id.s10Rbtn),
                rootView.findViewById(R.id.s15Rbtn),
                rootView.findViewById(R.id.s20Rbtn),
                rootView.findViewById(R.id.s25Rbtn),
        };

        // Creation of appDatabase instance
        final DataHolder holder = DataHolder.getInstance();
        final AppDatabase appDatabase = holder.getAppDatabase();

        // Fetching from database
        new Thread(new Runnable() {
            @Override
            public void run() {
                int user_id = holder.getUser_id();

                List<OtherSettingsTable> user_settingsList = appDatabase.otherSettingsInterface().fetchAllOtherSettings(user_id);
                if(user_settingsList.size() == 0) {
                    // Getting active user's settings list.
                    List<String> active_users = appDatabase.usersTableInterface().getActiveUsers(true);
                    Log.v("AMK: ", String.valueOf(active_users.size()));
                    user_settingsList = appDatabase.otherSettingsInterface().fetchAllOtherSettings(
                            appDatabase.usersTableInterface().getUserIdByName(active_users.get(0)));

                    Log.v("AMK: ", String.valueOf(user_settingsList.size()));
                }

                // Retrieving latest user settings
                final OtherSettingsTable user_settings = user_settingsList.get(user_settingsList.size() - 1);

                // Updating the fragment
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // Parsing data
                        final String morningTime = parseLongTime(user_settings.getMorning());
                        final String afternoonTime = parseLongTime(user_settings.getAfternoon());
                        final String eveningTime = parseLongTime(user_settings.getEverning());
                        final String customTime = parseLongTime(user_settings.getCustom());
                        final int snoozeTime = Integer.valueOf(user_settings.getSnooze_time());

                        morningBtn.setText(morningTime);
                        afternoonBtn.setText(afternoonTime);
                        eveningBtn.setText(eveningTime);
                        customBtn.setText(customTime);

                        snoozeRbtn[(snoozeTime / 5) - 1].toggle();
                    }

                    @SuppressLint("DefaultLocale")
                    private String parseLongTime(Long time) {
                        long m = (time / 60) % 60;
                        long h = (time / (60 * 60)) % 24;

                        return String.format("%d:%02d", h, m);
                    }
                });
            }
        }).start();

        // OnClick listeners are set.
        morningBtn.setOnClickListener(timeBtnOnClickListener);
        afternoonBtn.setOnClickListener(timeBtnOnClickListener);
        eveningBtn.setOnClickListener(timeBtnOnClickListener);
        customBtn.setOnClickListener(timeBtnOnClickListener);

        saveBtn.setOnClickListener(save_cancel_Clicked);
        cancelBtn.setOnClickListener(save_cancel_Clicked);

        for (RadioButton rButton : snoozeRbtn){
            rButton.setOnClickListener(radioButtonClickListener);
        }

        return rootView;
    }

    // Time pickers !
    private View.OnClickListener timeBtnOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            final View view = v;

            // Time picker parameters (Current time)
            Calendar mCurrentTime = Calendar.getInstance();
            int hour = mCurrentTime.get(Calendar.HOUR_OF_DAY);
            int minute = mCurrentTime.get(Calendar.MINUTE);

            TimePickerDialog mTimePicker = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker timePicker, final int selectedHour, int selectedMinute) {
                    String sTime = selectedHour + ":" + selectedMinute;

                    if (R.id.morningBtn == view.getId() ) {
                        morningBtn.setText(sTime);
                    } else if (R.id.afternoonBtn == view.getId()) {
                        afternoonBtn.setText(sTime);
                    } else if (R.id.eveningBtn == view.getId()){
                        eveningBtn.setText(sTime);
                    } else if (R.id.customBtn == view.getId()){
                        customBtn.setText(sTime);
                    }
                }
            }, hour, minute, true); // Yes 24h time

            mTimePicker.setTitle(R.string.SelectMorningTime);
            mTimePicker.show();
        }
    };

    private View.OnClickListener save_cancel_Clicked = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (R.id.saveBtn == v.getId()) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        // Getting selected time
                        String[] string_time = new String[4];
                        string_time[0] = morningBtn.getText().toString();
                        string_time[1] = afternoonBtn.getText().toString();
                        string_time[2] = eveningBtn.getText().toString();
                        string_time[3] = customBtn.getText().toString();

                        // Parsing selected time to Long
                        Long[] long_time = new Long[4];
                        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm");

                        try {
                            for (int i = 0; i < string_time.length; i++) {
                                Date date = sdf.parse(string_time[i]);
                                long_time[i] = date.getTime();
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        // Default snoozeTime is 5.
                        String snoozeTime = "5";

                        // Getting selected snoozeTime
                        for(int i = 0; i < snoozeRbtn.length; i++){
                            if (snoozeRbtn[i].isChecked()) {
                                snoozeTime = String.valueOf((i + 1) * 5);
                            }
                        }

                        OtherSettingsTable settings = new OtherSettingsTable (
                            DataHolder.getInstance().getUser_id(),
                            long_time[0],
                            long_time[1],
                            long_time[2],
                            long_time[3],
                            snoozeTime
                        );

                        Log.d("OtherSettings", settings.toString());

                        try {
                            DataHolder.getInstance().getAppDatabase().otherSettingsInterface().insertOtherSettings(settings);
                        } catch (Exception e) {
                            Log.v("AMK", "Senin amk");
                            e.printStackTrace();
                        }
                    }
                }).start();
            } else if (R.id.cancelBtn == v.getId()){
                // Return to settings
                mListener.settingsAsAdmin();
            }
        }
    };

    private View.OnClickListener radioButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            RadioButton rButton = getActivity().findViewById(v.getId());

            if (rButton.isChecked()){
                rButton.toggle();
            }
        }
    };


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        void settingsAsAdmin();
    }
}
