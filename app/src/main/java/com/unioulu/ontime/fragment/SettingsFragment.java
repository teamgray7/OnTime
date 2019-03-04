package com.unioulu.ontime.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.unioulu.ontime.R;

public class SettingsFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";

    // The interaction listener is defined.
    private OnFragmentInteractionListener mListener;

    public SettingsFragment() {
        // Empty constructor
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static SettingsFragment newInstance(int sectionNumber) {
        SettingsFragment fragment = new SettingsFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_settings, container, false);

        Button btnBluetooth = (Button) rootView.findViewById(R.id.bluetooth_settings_btn);
        Button btnEmergency = (Button) rootView.findViewById(R.id.emergency_settings_btn);
        Button btnOtherSettings = (Button) rootView.findViewById(R.id.other_settings_btn);

        btnBluetooth.setOnClickListener(bluetoothBtnClickListener);
        btnEmergency.setOnClickListener(emergencyBtnClickListener);
        btnOtherSettings.setOnClickListener(otherSettingsBtnClickListener);

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

    private View.OnClickListener bluetoothBtnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            startActivity(new Intent(android.provider.Settings.ACTION_BLUETOOTH_SETTINGS));
        }
    };

    private View.OnClickListener emergencyBtnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mListener.settingsEmergencyAsAdmin();
        }
    };

    private View.OnClickListener otherSettingsBtnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mListener.otherSettingsAsAdmin();
        }
    };

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
        void settingsEmergencyAsAdmin();
        void otherSettingsAsAdmin();
    }
}