package com.unioulu.ontime.fragment;

import android.app.Application;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
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
import java.util.concurrent.TimeUnit;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OtherSettingsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link OtherSettingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OtherSettingsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // Database variable
    private AppDatabase appDatabase;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    // Fragment's buttons !
    private Button morningBtn, afternoonBtn, eveningBtn, customBtn, save, cancel;
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
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment OtherSettingsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static OtherSettingsFragment newInstance(String param1, String param2) {
        OtherSettingsFragment fragment = new OtherSettingsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_other_settings, container, false);


        // Initialization of buttons
        morningBtn   = rootView.findViewById(R.id.morningBtn);
        afternoonBtn = rootView.findViewById(R.id.afternoonBtn);
        eveningBtn   = rootView.findViewById(R.id.eveningBtn);
        customBtn    = rootView.findViewById(R.id.customBtn);
        save         = rootView.findViewById(R.id.saveBtn);
        cancel       = rootView.findViewById(R.id.cancelBtn);

        // Initialization of radio buttons
        snoozeRbtn = new RadioButton[]{
                rootView.findViewById(R.id.s5Rbtn),
                rootView.findViewById(R.id.s10Rbtn),
                rootView.findViewById(R.id.s15Rbtn),
                rootView.findViewById(R.id.s20Rbtn),
                rootView.findViewById(R.id.s25Rbtn),
        };

        // TODO : retrieve settings from database and update fragment
        // fetching data from DB
        // Creation of appDatabase instance
        final DataHolder holder = DataHolder.getInstance();
        final AppDatabase appDatabase = holder.getAppDatabase();

        new Thread(new Runnable() {
            @Override
            public void run() {

                int user_id = holder.getUser_id();
                Log.d("OtherSettings", "user settings id: " + user_id);
                List<OtherSettingsTable> user_settingsList = appDatabase.otherSettingsInterface().fetchAllOtherSettings(user_id);
                Log.d("OtherSettings", "user settings list length: "+ user_settingsList.size());
                final OtherSettingsTable user_settings = user_settingsList.get(user_settingsList.size()-1); // Use last saved settings

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        // parsing data
                        final String morning = parseLongTime(user_settings.getMorning());

                        final String afternoon = parseLongTime(user_settings.getAfternoon());
                        morningBtn.setText(afternoon);

                        final String evening = parseLongTime(user_settings.getEverning());
                        morningBtn.setText(evening);

                        final String custom = parseLongTime(user_settings.getCustom());
                        morningBtn.setText(custom);

                        final int snoozeTime = Integer.valueOf(user_settings.getSnooze_time());

                        morningBtn.setText(morning);
                        afternoonBtn.setText(afternoon);
                        eveningBtn.setText(evening);
                        customBtn.setText(custom);

                        snoozeRbtn[(snoozeTime / 5) -1].toggle();
                    }

                    private String parseLongTime(Long time) {
                        long m = (time / 60) % 60;
                        long h = (time / (60 * 60)) % 24;
                        return String.format("%d:%02d", h,m);
                        }
                });
            }
        }).start();

        // OnClick listeners
        morningBtn.setOnClickListener(timeBtnOnClickListener);
        afternoonBtn.setOnClickListener(timeBtnOnClickListener);
        eveningBtn.setOnClickListener(timeBtnOnClickListener);
        customBtn.setOnClickListener(timeBtnOnClickListener);

        save.setOnClickListener(save_cancer_Clicked);
        cancel.setOnClickListener(save_cancer_Clicked);

        for (RadioButton rButton : snoozeRbtn){
            rButton.setOnClickListener(radioButtonClickListener);
        }

        return rootView;
    }

    // Time pickers !
    private View.OnClickListener timeBtnOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // Time picker parameters (Current time)
            final View view = v;
            Calendar mcurrentTime = Calendar.getInstance();
            int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
            int minute = mcurrentTime.get(Calendar.MINUTE);
            TimePickerDialog mTimePicker; // Time picker dialog
            final DataHolder holder = DataHolder.getInstance();
            final AppDatabase appDatabase = holder.getAppDatabase();

            mTimePicker = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker timePicker, final int selectedHour, int selectedMinute) {
                    if (R.id.morningBtn == view.getId() ) {
                        Log.d("OtherSettings", "morning");
                        Log.d("OtherSettings", selectedHour + ":" + selectedMinute);
                        morningBtn.setText(String.valueOf(selectedHour) + ":" +String.valueOf(selectedMinute));
                    }
                    else if (R.id.afternoonBtn == view.getId()) {

                        Log.d("OtherSettings", "Afternoon");

                        Log.d("OtherSettings", selectedHour + ":" + selectedMinute);

                        afternoonBtn.setText(String.valueOf(selectedHour) + ":" +String.valueOf(selectedMinute));

                    }else if (R.id.eveningBtn == view.getId()){
                        Log.d("OtherSettings", "Evening");

                        Log.d("OtherSettings", selectedHour + ":" + selectedMinute);

                        eveningBtn.setText(String.valueOf(selectedHour) + ":" +String.valueOf(selectedMinute));


                    }else if (R.id.customBtn == view.getId()){
                        Log.d("OtherSettings", "Custom");

                        Log.d("OtherSettings", selectedHour + ":" + selectedMinute);

                        customBtn.setText(String.valueOf(selectedHour) + ":" +String.valueOf(selectedMinute));
                    }
                }
            }, hour, minute, true);//Yes 24 hour time
            mTimePicker.setTitle(R.string.SelectMorningTime);
            mTimePicker.show();
        }
    };

    private View.OnClickListener save_cancer_Clicked = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            if (R.id.saveBtn == v.getId()){
                // TODO : save other settings to database
                Log.d("OtherSettings", "Save");
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
                        // TODO: Date parsing gives tedious results !
                        Long[] long_time = new Long[4];
                        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm");

                        // printing string time
                        for (String x:string_time)
                            Log.d("OtherSettings", "Time: " + x);

                        try{
                            for (int i=0; i < string_time.length; i++)
                            {
                                Date date = sdf.parse(string_time[i]);
                                long_time[i] = date.getTime();
                            }
                        }catch (ParseException e){
                            e.printStackTrace();
                        }

                        String snoozeTime="5";
                        // Getting selected Snooze time
                        for (int i=0; i<snoozeRbtn.length; i++){
                            if (snoozeRbtn[i].isChecked())
                                snoozeTime = String.valueOf( ( i + 1 ) * 5 );
                        }

                        OtherSettingsTable settings = new OtherSettingsTable(
                            DataHolder.getInstance().getUser_id(),
                            long_time[0],
                            long_time[1],
                            long_time[2],
                            long_time[3],
                            snoozeTime
                        );

                        Log.d("OtherSettings", settings.toString());
                        try{
                            DataHolder.getInstance().getAppDatabase().otherSettingsInterface().updateOtherSettings(settings);
                            }catch (Exception e){e.printStackTrace();}
                    }
                }).start();

            }else if (R.id.cancelBtn == v.getId()){
                Log.d("OtherSettings", "Cancel");
            }
        }
    };

    private View.OnClickListener radioButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            RadioButton rButton = getActivity().findViewById(v.getId());
            if (rButton.isChecked()){
                rButton.toggle();

                Log.d("OtherSettings", "Radio button: " + rButton.getText() + " clicked ! ");
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
        // Define functions later...
    }
}
