package com.unioulu.ontime.fragment;

import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TimePicker;
import android.widget.Toast;

import com.unioulu.ontime.R;
import com.unioulu.ontime.database_classes.AppDatabase;
import com.unioulu.ontime.database_classes.DataHolder;
import com.unioulu.ontime.database_classes.OtherSettingsTable;
import com.unioulu.ontime.helper.DateTimeConverter;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OtherSettingsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link OtherSettingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OtherSettingsFragment extends Fragment {

    // Fragment's buttons
    private Button morningBtn, afternoonBtn, eveningBtn, saveBtn, cancelBtn;

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
        morningBtn = rootView.findViewById(R.id.morningBtn);
        afternoonBtn = rootView.findViewById(R.id.afternoonBtn);
        eveningBtn = rootView.findViewById(R.id.eveningBtn);
        saveBtn = rootView.findViewById(R.id.saveBtn);
        cancelBtn = rootView.findViewById(R.id.cancelBtn);

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
                    user_settingsList = appDatabase.otherSettingsInterface().fetchAllOtherSettings(
                            appDatabase.usersTableInterface().getUserIdByName(active_users.get(0)));
                }

                // Retrieving latest user settings
                final OtherSettingsTable user_settings = user_settingsList.get(user_settingsList.size() - 1);

                // Updating the fragment
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // Parsing data
                        final Date morningTime = DateTimeConverter.fromTimestamp(user_settings.getMorning());
                        final Date afternoonTime = DateTimeConverter.fromTimestamp(user_settings.getAfternoon());
                        final Date eveningTime = DateTimeConverter.fromTimestamp(user_settings.getEvening());
                        final int snoozeTime = Integer.valueOf(user_settings.getSnooze_time());

                        morningBtn.setText(DateTimeConverter.fromDateToStringHoursMinutes(morningTime));
                        afternoonBtn.setText(DateTimeConverter.fromDateToStringHoursMinutes(afternoonTime));
                        eveningBtn.setText(DateTimeConverter.fromDateToStringHoursMinutes(eveningTime));
                        snoozeRbtn[(snoozeTime / 5) - 1].toggle();
                    }
                });
            }
        }).start();

        // OnClick listeners are set.
        morningBtn.setOnClickListener(timeBtnOnClickListener);
        afternoonBtn.setOnClickListener(timeBtnOnClickListener);
        eveningBtn.setOnClickListener(timeBtnOnClickListener);
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
                    Date sDate = new Date();
                    sDate.setHours(selectedHour);
                    sDate.setMinutes(selectedMinute);

                    if (R.id.morningBtn == view.getId() ) {
                        morningBtn.setText(DateTimeConverter.fromDateToStringHoursMinutes(sDate));
                    } else if (R.id.afternoonBtn == view.getId()) {
                        afternoonBtn.setText(DateTimeConverter.fromDateToStringHoursMinutes(sDate));
                    } else if (R.id.eveningBtn == view.getId()){
                        eveningBtn.setText(DateTimeConverter.fromDateToStringHoursMinutes(sDate));
                    }
                }
            }, hour, minute, true); // Yes 24h time

            mTimePicker.setTitle(R.string.selectTime);
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
                        String[] string_time = new String[3];
                        string_time[0] = morningBtn.getText().toString();
                        string_time[1] = afternoonBtn.getText().toString();
                        string_time[2] = eveningBtn.getText().toString();

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
                                string_time[0],
                                string_time[1],
                                string_time[2],
                                snoozeTime
                        );

                        try {
                            DataHolder.getInstance().getAppDatabase().otherSettingsInterface().insertOtherSettings(settings);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                String message = "Settings are saved!";
                                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();

                                mListener.settingsAsAdmin();
                            }
                        });
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
