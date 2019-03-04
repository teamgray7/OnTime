package com.unioulu.ontime.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.unioulu.ontime.R;

public class EmergencyFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";
    private static final String ARG_ADMIN_USER = "user_admin";

    boolean isAdmin;

    // The interaction listener is defined.
    private OnFragmentInteractionListener mListener;

    public EmergencyFragment() {
        // Empty constructor
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static EmergencyFragment newInstance(int sectionNumber, boolean user_admin) {
        EmergencyFragment fragment = new EmergencyFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        args.putBoolean(ARG_ADMIN_USER, user_admin);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_emergency, container, false);
        isAdmin = getArguments().getBoolean(ARG_ADMIN_USER);

        if(isAdmin) {
            EditText etPersonName = (EditText) rootView.findViewById(R.id.name_field);
            EditText etPersonPhone = (EditText) rootView.findViewById(R.id.number_field);

            etPersonName.setVisibility(View.VISIBLE);
            etPersonPhone.setVisibility(View.VISIBLE);
        } else {
            TextView tvPersonName = (TextView) rootView.findViewById(R.id.tv_name_field);
            TextView tvPersonPhone = (TextView) rootView.findViewById(R.id.tv_number_field);

            tvPersonName.setVisibility(View.VISIBLE);
            tvPersonPhone.setVisibility(View.VISIBLE);
        }

        ImageButton callButton = (ImageButton) rootView.findViewById(R.id.callButton);
        callButton.setOnClickListener(callButtonClickListener);

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

    private View.OnClickListener callButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mListener.makeCall("+358403600652");
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
        void makeCall(String phoneNumber);
    }
}