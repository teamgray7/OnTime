package com.unioulu.ontime.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
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

import java.util.ArrayList;

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
        snoozeSpinner = (Spinner) rootView.findViewById(R.id.snoozeSpinner);
        snoozeButton  = (Button) rootView.findViewById(R.id.snoozeButton );


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

        // Spinner on Item click listener
        snoozeSpinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Getting selected item
                String item = parent.getItemAtPosition(position).toString();

                // Showing selected spinner item
                Toast.makeText(getContext(), item+" selected !", Toast.LENGTH_SHORT).show();
            }
        });


        // ------------------------------------ End of spinner implementation -------------------------------------------------

        // ------------------------------------ Snooze button implementation --------------------------------------------------
        snoozeButton = (Button) rootView.findViewById(R.id.snoozeButton);

        snoozeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Snooze button clicked !", Toast.LENGTH_SHORT).show();
                // Will change alarm fragment to taken fragment..
            }
        });

        // ------------------------------------ End of snooze button implementation --------------------------------------------

        return rootView;
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
    }
}