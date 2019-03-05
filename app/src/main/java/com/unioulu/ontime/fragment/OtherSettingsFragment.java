package com.unioulu.ontime.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;

import com.unioulu.ontime.R;


public class OtherSettingsFragment extends Fragment {

    // Fragment's buttons !
    Button morningBtn, afternoonBtn, eveningBtn, customBtn, save, cancel;
    // Fragment's radioButtons
    RadioButton[] snoozeRbtn; // 5 buttons in total


    private static final String ARG_SECTION_NUMBER = "section_number";
    private static final String ARG_ADMIN_USER = "user_admin";


    // Empty constructor
    public OtherSettingsFragment() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static OtherSettingsFragment newInstance(int sectionNumber, boolean user_admin) {
        OtherSettingsFragment fragment = new OtherSettingsFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        args.putBoolean(ARG_ADMIN_USER, user_admin);
        fragment.setArguments(args);
        return fragment;
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




        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_other_settings, container, false);
    }

}
