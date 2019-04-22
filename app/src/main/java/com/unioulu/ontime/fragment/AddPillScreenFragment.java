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
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.unioulu.ontime.R;
import com.unioulu.ontime.database_classes.AppDatabase;
import com.unioulu.ontime.database_classes.DataHolder;
import com.unioulu.ontime.database_classes.Medicines;

import java.util.List;

public class AddPillScreenFragment extends Fragment {

    private static final int GALLERY_REQUEST = 1;
    private String imgPill;
    private String imgName;

    private ImageView ivPill;
    private EditText etPill;
    private EditText etPillAmount;
    private RadioButton rbMorning;
    private RadioButton rbAfternoon;
    private RadioButton rbEvening;
    private Button btnPillSave;
    private Button btnPillEdit;
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

        etPill = (EditText) rootView.findViewById(R.id.et_pill);
        etPillAmount = (EditText) rootView.findViewById(R.id.et_pillAmount);

        btnPillSave = (Button) rootView.findViewById(R.id.btnPillSave);
        btnPillSave.setOnClickListener(btnPillSaveClickListener);

        btnPillEdit = (Button) rootView.findViewById(R.id.btnPillEdit);
        btnPillEdit.setOnClickListener(btnPillEditClickListener);

        btnPillCancel = (Button) rootView.findViewById(R.id.btnPillCancel);
        btnPillCancel.setOnClickListener(btnPillCancelClickListener);

        btnPillDelete = (Button) rootView.findViewById(R.id.btnPillDelete);
        btnPillDelete.setOnClickListener(btnPillDeleteClickListener);

        ivPill = (ImageView) rootView.findViewById(R.id.iv_pill);
        ivPill.setOnClickListener(pillImageClickListener);

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

    private View.OnClickListener btnPillSaveClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            final EditText etPill_th = etPill;
            final EditText etPillAmount_th = etPillAmount;
            final RadioButton rbMorning_th = rbMorning;
            final RadioButton rbAfternoon_th = rbAfternoon;
            final RadioButton rbEvening_th = rbEvening;
            final String imgPill_th = imgPill;

            new Thread(new Runnable() {
                @Override
                public void run() {
                    final AppDatabase appDatabase = DataHolder.getInstance().getAppDatabase();

                    List<String> active_user = appDatabase.usersTableInterface().getActiveUsers(true);
                    final int active_user_id = appDatabase.usersTableInterface().getUserIdByName(active_user.get(active_user.size() - 1)); // ID of last active user

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
                        DataHolder.getInstance().getAppDatabase().medicineDBInterface().insertOnlySingleMedicine(newPill);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // Cleaning the data
                            etPill.setText("");
                            etPillAmount.setText("");
                            rbMorning.setChecked(false);
                            rbAfternoon.setChecked(false);
                            rbEvening.setChecked(false);
                            ivPill.setImageDrawable(getResources().getDrawable(R.drawable.ic_pill_icon));
                            imgPill = "";

                            String message = "New pill is saved.";
                            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                            mListener.pillSaved();
                        }
                    });
                }
            }).start();
        }
    };

    private View.OnClickListener btnPillEditClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            final EditText etPill_th = etPill;
            final EditText etPillAmount_th = etPillAmount;
            final RadioButton rbMorning_th = rbMorning;
            final RadioButton rbAfternoon_th = rbAfternoon;
            final RadioButton rbEvening_th = rbEvening;
            final String imgPill_th = imgPill;
            final String imgName_th = imgName;

            new Thread(new Runnable() {
                @Override
                public void run() {
                    final AppDatabase appDatabase = DataHolder.getInstance().getAppDatabase();

                    List<String> active_user = appDatabase.usersTableInterface().getActiveUsers(true);
                    final int active_user_id = appDatabase.usersTableInterface().getUserIdByName(active_user.get(active_user.size() - 1)); // ID of last active user

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
                        DataHolder.getInstance().getAppDatabase().medicineDBInterface().deleteMedicineByName(imgName_th);
                        DataHolder.getInstance().getAppDatabase().medicineDBInterface().insertOnlySingleMedicine(newPill);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // Cleaning the data
                            etPill.setText("");
                            etPillAmount.setText("");
                            rbMorning.setChecked(false);
                            rbAfternoon.setChecked(false);
                            rbEvening.setChecked(false);
                            ivPill.setImageDrawable(getResources().getDrawable(R.drawable.ic_pill_icon));
                            imgPill = "";

                            String message = "The pill is edited.";
                            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                            mListener.pillSaved();
                        }
                    });
                }
            }).start();

            btnPillSave.setVisibility(View.VISIBLE);
            btnPillEdit.setVisibility(View.GONE);
            btnPillCancel.setVisibility(View.VISIBLE);
            btnPillDelete.setVisibility(View.GONE);
        }
    };

    private View.OnClickListener btnPillCancelClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // Cleaning the data
            etPill.setText("");
            etPillAmount.setText("");
            rbMorning.setChecked(false);
            rbAfternoon.setChecked(false);
            rbEvening.setChecked(false);
            ivPill.setImageDrawable(getResources().getDrawable(R.drawable.ic_pill_icon));

            mListener.pillCancel();
        }
    };

    private View.OnClickListener btnPillDeleteClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mListener.pillDelete(etPill.getText().toString());
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

    public void pillDeleteConfirmed(final String pillName) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    DataHolder.getInstance().getAppDatabase().medicineDBInterface().deleteMedicineByName(pillName);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // Cleaning the data
                        etPill.setText("");
                        etPillAmount.setText("");
                        rbMorning.setChecked(false);
                        rbAfternoon.setChecked(false);
                        rbEvening.setChecked(false);
                        ivPill.setImageDrawable(getResources().getDrawable(R.drawable.ic_pill_icon));
                        imgPill = "";

                        String message = "The pill is deleted.";
                        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                        mListener.pillSaved();
                    }
                });
            }
        }).start();

        btnPillSave.setVisibility(View.VISIBLE);
        btnPillEdit.setVisibility(View.GONE);
        btnPillCancel.setVisibility(View.VISIBLE);
        btnPillDelete.setVisibility(View.GONE);
    }

    public void setFragmentDetails(String pillName, String pillImage, String pillAmount, int morning, int afternoon, int evening) {
        this.imgName = pillName;

        etPill.setText(pillName);
        etPillAmount.setText(pillAmount);

        if(morning == 1) {
            rbMorning.setChecked(true);
        }

        if(afternoon == 1) {
            rbAfternoon.setChecked(true);
        }

        if(evening == 1) {
            rbEvening.setChecked(true);
        }

        if(pillImage != null && !pillImage.equals("")) {
            imgPill = pillImage;
            ivPill.setImageURI(Uri.parse(pillImage));
        } else {
            imgPill = "";
            ivPill.setImageDrawable(getResources().getDrawable(R.drawable.ic_pill_icon));
        }

        btnPillSave.setVisibility(View.GONE);
        btnPillEdit.setVisibility(View.VISIBLE);
        btnPillCancel.setVisibility(View.GONE);
        btnPillDelete.setVisibility(View.VISIBLE);
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
        void pillDelete(String pillName);
    }
}