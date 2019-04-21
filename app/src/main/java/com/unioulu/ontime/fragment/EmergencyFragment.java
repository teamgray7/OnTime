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
import android.widget.Toast;

import com.unioulu.ontime.R;
import com.unioulu.ontime.database_classes.AppDatabase;
import com.unioulu.ontime.database_classes.DataHolder;
import com.unioulu.ontime.database_classes.EmergencySettingsTable;

import java.util.List;

public class EmergencyFragment extends Fragment {

    private static final String ARG_ADMIN_USER = "user_admin";
    private static final int GALLERY_REQUEST = 1;

    private ImageView ivEmergency;
    private String ivUri = "NULL";

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
    public static EmergencyFragment newInstance(boolean user_admin) {
        EmergencyFragment fragment = new EmergencyFragment();
        Bundle args = new Bundle();
        args.putBoolean(ARG_ADMIN_USER, user_admin);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_emergency, container, false);
        isAdmin = getArguments().getBoolean(ARG_ADMIN_USER);

        final ImageButton callButton = (ImageButton) rootView.findViewById(R.id.callButton);
        final Button btnSaveEmergency = (Button) rootView.findViewById(R.id.btn_saveEmergency);
        final ImageView ivEmergency = (ImageView) rootView.findViewById(R.id.iv_emergency);

        btnSaveEmergency.setOnClickListener(btnSaveEmergencyClickListener);
        callButton.setOnClickListener(callButtonClickListener);
        ivEmergency.setOnClickListener(emergencyImageClickListener);
        this.ivEmergency = ivEmergency;

        // Creation of appDatabase instance
        final AppDatabase appDatabase = DataHolder.getInstance().getAppDatabase();

        // Admin user can edit info and picture, normal user is not able to edit anything.
        if(isAdmin) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    List<String> active_user = appDatabase.usersTableInterface().getActiveUsers(true);
                    final int active_user_id = appDatabase.usersTableInterface().getUserIdByName(active_user.get(active_user.size()-1)); // ID of last active user

                    List<EmergencySettingsTable> emergencySettingsContacts = appDatabase.emergencySettingsInterface().fetchAllEmergencyContacts(active_user_id);
                    if (emergencySettingsContacts.size() == 0){
                        emergencySettingsContacts = appDatabase.emergencySettingsInterface().fetchAllEmergencyContacts(appDatabase.usersTableInterface().getUserIdByName(active_user.get(0)));
                    }

                    final EmergencySettingsTable contact = emergencySettingsContacts.get(emergencySettingsContacts.size()-1);
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            EditText etPersonName = (EditText) rootView.findViewById(R.id.name_field);
                            EditText etPersonPhone = (EditText) rootView.findViewById(R.id.number_field);

                            etPersonName.setText(contact.getContact_name());
                            etPersonPhone.setText(contact.getPhone_number());

                            String picture_path = contact.getPicture_url();
                            if (picture_path == null || picture_path.equals("NULL")) {
                                ivEmergency.setImageDrawable(getResources().getDrawable(R.drawable.avatar_icon));
                            } else {
                                ivUri = picture_path;
                                ivEmergency.setImageURI(Uri.parse(picture_path));
                            }

                            callButton.setVisibility(View.GONE);
                        }
                    });
                }
            }).start();
        } else {
            final TextView tvPersonName = rootView.findViewById(R.id.name_field);
            final TextView tvPersonPhone = rootView.findViewById(R.id.number_field);

            new Thread(new Runnable() {
                @Override
                public void run() {
                    List<String> active_user = appDatabase.usersTableInterface().getActiveUsers(true);
                    final int active_user_id = appDatabase.usersTableInterface().getUserIdByName(active_user.get(active_user.size()-1)); // ID of last active user

                    List<EmergencySettingsTable> emergencySettingsContacts = appDatabase.emergencySettingsInterface().fetchAllEmergencyContacts(active_user_id);
                    if (emergencySettingsContacts.size() == 0)
                        emergencySettingsContacts = appDatabase.emergencySettingsInterface().fetchAllEmergencyContacts(appDatabase.usersTableInterface().getUserIdByName(active_user.get(0)));

                    final EmergencySettingsTable contact = emergencySettingsContacts.get(emergencySettingsContacts.size()-1);
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tvPersonName.setText(contact.getContact_name());
                            tvPersonPhone.setText(contact.getPhone_number());

                            String picture_path = contact.getPicture_url();
                            if (picture_path == null || picture_path.equals("NULL") ){
                                ivEmergency.setImageDrawable(getResources().getDrawable(R.drawable.avatar_icon));
                            }else{
                                ivUri = picture_path;
                                ivEmergency.setImageURI(Uri.parse(picture_path));
                            }

                            tvPersonName.setEnabled(false);
                            tvPersonPhone.setEnabled(false);
                            btnSaveEmergency.setVisibility(View.GONE);
                        }
                    });
                }
            }).start();
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
            TextView tvPersonPhone = getView().findViewById(R.id.name_field);
            String phoneNumber = tvPersonPhone.getText().toString();

            mListener.makeCall(phoneNumber);
        }
    };

    private View.OnClickListener btnSaveEmergencyClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            final EditText etPersonName = getActivity().findViewById(R.id.name_field);
            final EditText etPersonPhone = getActivity().findViewById(R.id.number_field);

            new Thread(new Runnable() {
                @Override
                public void run() {
                    EmergencySettingsTable emergencyContact = new EmergencySettingsTable(
                            DataHolder.getInstance().getUser_id(),
                            etPersonName.getText().toString(),
                            etPersonPhone.getText().toString(),
                            ivUri
                    );

                    try{
                        DataHolder.getInstance().getAppDatabase().emergencySettingsInterface().insertEmergencyContact(emergencyContact);
                        Toast.makeText(getContext(), "Contact is saved!", Toast.LENGTH_SHORT).show();
                    }catch (Exception e){
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getContext(), "Contact phone number already exists", Toast.LENGTH_SHORT).show();
                            }
                        });
                        e.printStackTrace();
                    }
                }
            }).start();
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
                ivUri = imgSelected.toString();
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