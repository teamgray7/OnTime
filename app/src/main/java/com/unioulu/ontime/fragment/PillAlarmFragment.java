package com.unioulu.ontime.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.unioulu.ontime.R;
import com.unioulu.ontime.helper.AlarmSharedData;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PillAlarmFragment extends Fragment {

    // Needed widgets
    Spinner snoozeSpinner;
    Button snoozeButton;
    TextView medicineName, medicineTime;
    ImageView medicinePic;

    // Needed variables
    ArrayList<String> snoozeTimeList;

    // The interaction listener is defined.
    private OnFragmentInteractionListener mListener;

    public PillAlarmFragment() {
        // Empty constructor
    }

    public static PillAlarmFragment newInstance() {
        PillAlarmFragment fragment = new PillAlarmFragment();
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_pills_alarm, container, false);

        // Initialization of the variables
        snoozeSpinner = rootView.findViewById(R.id.snoozeSpinner);
        snoozeButton  = rootView.findViewById(R.id.snoozeButton );


        // --------------------------------------- Spinner implementation ---------------------------------
        snoozeTimeList = new ArrayList<>();

        // These values should be retrieved from database (Set in settings screen)
        snoozeTimeList.add("5 min");
        snoozeTimeList.add("10 min");
        snoozeTimeList.add("15 min");
        snoozeTimeList.add("30 min");

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(getContext(),
                R.layout.support_simple_spinner_dropdown_item,
                snoozeTimeList);

        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Setting drop down elements
        snoozeSpinner.setAdapter(spinnerAdapter);


        // ------------------------------------ End of spinner implementation -------------------------------------------------

        // ------------------------------------ Snooze button implementation --------------------------------------------------
        snoozeButton = rootView.findViewById(R.id.snoozeButton);

        snoozeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selectedSnoozeTime = snoozeSpinner.getSelectedItem().toString();
                Toast.makeText(getContext(), selectedSnoozeTime + " Snooze time selected !", Toast.LENGTH_SHORT).show();

                // FIXME: Now 60 s hardcoded snooze, use snoozeSpinner instead.
                Date now = new Date();
                long snoozeAlarm = now.getTime() + 60000;
                mListener.snooze(snoozeAlarm);
                // Will change alarm fragment to taken fragment..
                getActivity().finish();
            }
        });

        // ------------------------------------ End of snooze button implementation --------------------------------------------

        // ------------------------------------ Text view text update from shared preferences -------------------------------

        medicineTime = rootView.findViewById(R.id.medicineTime);
        medicineName = rootView.findViewById(R.id.medicineName);

        switch (AlarmSharedData.getInstance().getPillTime())
        {
            case 0:
                {
                    medicineTime.setText(AlarmSharedData.getInstance().getMorningTime().getHours()+":"+AlarmSharedData.getInstance().getMorningTime().getMinutes());
                    medicineName.setText(medicineNameText(AlarmSharedData.getInstance().getMedicineName()));
                    break;
                }
            case 1:
            {
                medicineTime.setText(AlarmSharedData.getInstance().getAfternoonTime().getHours()+":"+AlarmSharedData.getInstance().getAfternoonTime().getMinutes());
                medicineName.setText(medicineNameText(AlarmSharedData.getInstance().getMedicineName()));
                break;
            }
            case 2:
            {
                medicineTime.setText(AlarmSharedData.getInstance().getEveningTime().getHours()+":"+AlarmSharedData.getInstance().getEveningTime().getMinutes());
                medicineName.setText(medicineNameText(AlarmSharedData.getInstance().getMedicineName()));
                break;
            }

            default: medicineTime.setText("");
        }

        return rootView;
    }

    private String medicineNameText(List<String> medicineNames)
    {
        String appended_names = "";
        for (String name: medicineNames)
        {
            appended_names+= name + "\n";
        }

        return appended_names;
    }

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
        // Define functions to make snooze and everything...
        void snooze(long snoozeDate);
    }
}