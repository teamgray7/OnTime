package com.unioulu.ontime.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;

import com.bumptech.glide.Glide;
import com.unioulu.ontime.R;
import com.unioulu.ontime.database_classes.AppDatabase;
import com.unioulu.ontime.database_classes.DataHolder;
import com.unioulu.ontime.database_classes.Medicines;

import java.util.List;

public class AddPillScreenFragment extends Fragment {

    private static final int GALLERY_REQUEST = 1;
    private String imgPill;

    private ImageView ivPill;
    private EditText etPill;
    private EditText etPillAmount;
    private RadioButton rbMorning;
    private RadioButton rbAfternoon;
    private RadioButton rbEvening;
    private Button btnPillSave;
    private Button btnPillCancel;
    private Button btnPillDelete;

    // The interaction listener is defined.
    private OnFragmentInteractionListener mListener;

    public AddPillScreenFragment() {
        // Empty constructor
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static AddPillScreenFragment newInstance() {
        return new AddPillScreenFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_pill, container, false);

        btnPillSave = (Button) rootView.findViewById(R.id.btnPillSave);
        btnPillSave.setOnClickListener(btnPillSaveClickListener);

        btnPillCancel = (Button) rootView.findViewById(R.id.btnPillCancel);
        btnPillCancel.setOnClickListener(btnPillCancelClickListener);

        btnPillDelete = (Button) rootView.findViewById(R.id.btnPillDelete);
        btnPillDelete.setOnClickListener(btnPillDeleteClickListener);

        ivPill = (ImageView) rootView.findViewById(R.id.iv_pill);
        ivPill.setOnClickListener(pillImageClickListener);

        etPill = (EditText) rootView.findViewById(R.id.et_pill);
        etPillAmount = (EditText) rootView.findViewById(R.id.et_pillAmount);

        rbMorning = (RadioButton) rootView.findViewById(R.id.rb_pillMorning);
        rbAfternoon = (RadioButton) rootView.findViewById(R.id.rb_pillAfternoon);
        rbEvening = (RadioButton) rootView.findViewById(R.id.rb_pillEvening);

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

    public void setFragmentDetails(String pillName, String pillImage) {
        this.imgPill = pillImage;
        etPill.setText(pillName);

        btnPillCancel.setVisibility(View.GONE);
        btnPillDelete.setVisibility(View.VISIBLE);
    }

    private View.OnClickListener btnPillSaveClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            final EditText etPill_th = etPill;
            final EditText etPillAmount_th = etPillAmount;
            final RadioButton rbMorning_th = rbMorning, rbAfternoon_th = rbAfternoon, rbEvening_th = rbEvening;
            final String imgPill_th = imgPill;

            new Thread(new Runnable() {
                @Override
                public void run() {
                    // TODO : Use this three lines below for SERVICES
                    final AppDatabase appDatabase = DataHolder.getInstance().getAppDatabase();

                    List<String> active_user = appDatabase.usersTableInterface().getActiveUsers(true);
                    final int active_user_id = appDatabase.usersTableInterface().getUserIdByName(active_user.get(active_user.size() - 1)); // ID of last active user

                    List<Medicines> all_medicines = appDatabase.medicineDBInterface().fetchAllMedicines(active_user_id);
                    int nextMedicineID = all_medicines.size() + 1;

                    Log.d("NEXT PILL ID : ", String.valueOf(nextMedicineID));
                    Log.d("NEXT PILL NAME: ", etPill_th.getText().toString());
                    Log.d("NEXT PILL AMOUNT: ", etPillAmount.getText().toString());

                    Medicines newPill = new Medicines(
                            active_user_id,
                            etPill_th.getText().toString(),
                            etPillAmount_th.getText().toString(),
                            imgPill_th,
                            rbMorning_th.isChecked() ? 1:0,
                            rbAfternoon_th.isChecked() ? 1:0,
                            rbEvening_th.isChecked() ? 1:0,
                            0
                    );

                    try {
                        Log.d("BERKE: ", newPill.toString());
                        DataHolder.getInstance().getAppDatabase().medicineDBInterface().insertOnlySingleMedicine(newPill);
                    } catch (Exception e) {
                        Log.v("ERROR on saving pill", "There is an error... God knows what..");
                        Log.v("ERROR: ", e.getMessage());
                    }

                    // TODO: For SERVICES
                    List<String> all_medicines2 = appDatabase.medicineDBInterface().fetchMorningPills(active_user_id);
                    long morning = appDatabase.otherSettingsInterface().fetchMorningTime(active_user_id);
                    for(String medicines: all_medicines2) {
                        Log.d("Medicine: ", medicines);
                    }

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // Cleaning the data
                            ivPill.setImageDrawable(getResources().getDrawable(R.drawable.ic_pill_icon));
                            etPill.setText("");
                            etPillAmount.setText("");
                            rbMorning.setChecked(false);
                            rbAfternoon.setChecked(false);
                            rbEvening.setChecked(false);

                            mListener.pillSaved();
                        }
                    });
                }
            }).start();
        }
    };

    private View.OnClickListener btnPillCancelClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // Cleaning the data
            ivPill.setImageDrawable(getResources().getDrawable(R.drawable.ic_pill_icon));
            etPill.setText("");
            etPillAmount.setText("");
            rbMorning.setChecked(false);
            rbAfternoon.setChecked(false);
            rbEvening.setChecked(false);

            mListener.pillCancel();
        }
    };

    private View.OnClickListener btnPillDeleteClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // TODO: Remove the pill from database.
        }
    };

    private View.OnClickListener pillImageClickListener = new View.OnClickListener() {
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
                imgPill = String.valueOf(imgSelected);
                ivPill.setImageURI(imgSelected);
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
        void pillSaved();
        void pillCancel();
    }
}