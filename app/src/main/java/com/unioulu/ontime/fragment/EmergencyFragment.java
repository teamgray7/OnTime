package com.unioulu.ontime.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.unioulu.ontime.R;

public class EmergencyFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";
    private static final String ARG_ADMIN_USER = "user_admin";
    private static final int GALLERY_REQUEST = 1;

    private ImageView ivEmergency;

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

        ImageButton callButton = (ImageButton) rootView.findViewById(R.id.callButton);
        Button btnSaveEmergency = (Button) rootView.findViewById(R.id.btn_saveEmergency);

        // TODO : Replace static image with the one saved into database...
        ivEmergency = (ImageView) rootView.findViewById(R.id.iv_emergency);
        ivEmergency.setImageDrawable(getResources().getDrawable(R.drawable.avatar_icon));

        // Admin user can edit info and picture, normal user is not able to edit anything.
        if(isAdmin) {
            EditText etPersonName = (EditText) rootView.findViewById(R.id.name_field);
            EditText etPersonPhone = (EditText) rootView.findViewById(R.id.number_field);

            // TODO : Replace static information with ones retrieved from database...
            etPersonName.setText("Berke Esmer");
            etPersonPhone.setText("+358403600652");

            etPersonName.setVisibility(View.VISIBLE);
            etPersonPhone.setVisibility(View.VISIBLE);

            callButton.setVisibility(View.GONE);
            btnSaveEmergency.setOnClickListener(btnSaveEmergencyClickListener);

            ivEmergency.setOnClickListener(emergencyImageClickListener);
        } else {
            TextView tvPersonName = (TextView) rootView.findViewById(R.id.tv_name_field);
            TextView tvPersonPhone = (TextView) rootView.findViewById(R.id.tv_number_field);

            // TODO : Replace static information with ones retrieved from database...
            tvPersonName.setText("Berke Esmer");
            tvPersonPhone.setText("+358403600652");

            tvPersonName.setVisibility(View.VISIBLE);
            tvPersonPhone.setVisibility(View.VISIBLE);

            btnSaveEmergency.setVisibility(View.GONE);
            callButton.setOnClickListener(callButtonClickListener);
        }

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
            TextView tvPersonPhone = (TextView) getView().findViewById(R.id.tv_number_field);
            String phoneNumber = tvPersonPhone.getText().toString();

            mListener.makeCall(phoneNumber);
        }
    };

    private View.OnClickListener btnSaveEmergencyClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // TODO : Fill here with the function that would save data (both image and info) into database..
        }
    };

    private View.OnClickListener emergencyImageClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
            photoPickerIntent.setType("image/*");

            startActivityForResult(photoPickerIntent, GALLERY_REQUEST);
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == Activity.RESULT_OK) {
            if(requestCode == 1) {
                final Uri imgSelected = data.getData();
                ivEmergency.setImageURI(imgSelected);
            }
        }
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
        void makeCall(String phoneNumber);
    }
}